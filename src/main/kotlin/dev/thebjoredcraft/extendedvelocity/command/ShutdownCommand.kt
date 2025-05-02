package dev.thebjoredcraft.extendedvelocity.command

import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.scheduler.ScheduledTask
import dev.thebjoredcraft.extendedvelocity.message.MessageBuilder
import dev.thebjoredcraft.extendedvelocity.plugin
import dev.thebjoredcraft.extendedvelocity.util.error
import dev.thebjoredcraft.extendedvelocity.util.sendRawText
import dev.thebjoredcraft.extendedvelocity.util.sendText
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

class ShutdownCommand : SimpleCommand {

    private var plannedShutdownTime: Long? = null
    private var shutdownReason: String = ""
    private var shutdownTaskId: ScheduledTask? = null
    private val notificationTaskIds: MutableList<ScheduledTask> = mutableListOf()

    override fun execute(invocation: SimpleCommand.Invocation) {
        val source = invocation.source()
        val args = invocation.arguments()

        if (args.isEmpty()) {
            sendUsage(source)
            return
        }

        when (args[0].lowercase()) {
            "plan" -> planShutdown(source, args.toList())
            "cancel" -> cancelShutdown(source)
            "info" -> shutdownInfo(source)
            else -> sendUsage(source)
        }
    }

    private fun planShutdown(source: CommandSource, args: List<String>) {
        if (args.size < 2) {
            sendUsage(source)
            return
        }

        val delayInSeconds = parseTime(args[1]) ?: run {
            source.error("Incorrect time format. Please use 10m, 1h, 30s...")
            return
        }

        shutdownReason = args.drop(2).joinToString(" ").ifEmpty { "Proxy shutting down." }
        source.sendText("Successfully planned a proxy shutdown in ${formatSeconds(delayInSeconds)} with reason: $shutdownReason")

        plannedShutdownTime = System.currentTimeMillis() + delayInSeconds * 1000

        for (i in delayInSeconds downTo 1) {
            val shouldNotify = when {
                i > 30 -> i % 30 == 0
                i > 10 -> i % 10 == 0
                i > 5 -> i % 5 == 0
                else -> true
            }

            if (shouldNotify) {
                val task = plugin.proxy.scheduler.buildTask(plugin, Consumer {
                    val message = MessageBuilder()
                        .newLine()
                        .withPrefix().white("Shutdown in ${formatSeconds(i)}...").newLine()
                        .withPrefix().modernGreen(shutdownReason).newLine()
                        .newLine()
                    plugin.proxy.allPlayers.forEach { it.sendRawText(message) }
                }).delay((delayInSeconds - i).toLong(), TimeUnit.SECONDS).schedule()

                notificationTaskIds.add(task)
            }
        }

        shutdownTaskId = plugin.proxy.scheduler.buildTask(plugin, Consumer {
            plugin.logger.warn("Shutting down the proxy as planned, reason: $shutdownReason")

            plugin.proxy.allPlayers.forEach {
                it.disconnect(MessageBuilder()
                    .newLine()
                    .withPrefix().white("The proxy is shutting down...").newLine()
                    .withPrefix().modernGreen(shutdownReason).newLine()
                    .newLine()
                    .build()
                )
            }

            plugin.proxy.shutdown()
        }).delay(delayInSeconds.toLong(), TimeUnit.SECONDS).schedule()
    }

    private fun cancelShutdown(source: CommandSource) {
        val taskId = shutdownTaskId

        if (plannedShutdownTime == null || taskId == null) {
            source.error("There is no scheduled shutdown to cancel.")
            return
        }

        notificationTaskIds.forEach { it.cancel() }
        notificationTaskIds.clear()

        taskId.cancel()
        source.sendText("The shutdown has been successfully canceled.")

        plugin.proxy.allPlayers.forEach {
            it.sendRawText(MessageBuilder().spacer(" ")
                .withPrefix().newLine()
                .withPrefix().modernGreen("The shutdown has been canceled.").newLine()
                .withPrefix().newLine()
            )
        }

        plannedShutdownTime = null
        shutdownTaskId = null
    }

    private fun shutdownInfo(source: CommandSource) {
        if (plannedShutdownTime == null) {
            source.error("There is currently no planned shutdown.")
            return
        }

        val timeLeft = plannedShutdownTime!! - System.currentTimeMillis()
        val secondsLeft = timeLeft / 1000
        val formattedTimeLeft = formatSeconds(secondsLeft.toInt())

        source.sendText("There is a shutdown planned in $formattedTimeLeft with reason: $shutdownReason")
    }

    override fun hasPermission(invocation: SimpleCommand.Invocation): Boolean {
        return invocation.source().hasPermission("extendedvelocity.shutdown.command")
    }

    override fun suggest(invocation: SimpleCommand.Invocation): List<String> {
        val args = invocation.arguments()

        if (args.size <= 1) {
            return listOf("plan", "cancel", "info")
        }

        return emptyList()
    }

    private fun sendUsage(source: CommandSource) {
        source.sendRawText (
            MessageBuilder().spacer(" ")
                .withPrefix().modernGreen("Available Arguments/Sub Commands for /shutdown").newLine()
                .withPrefix().newLine()
                .withPrefix().white("/shutdown plan <time> <reason>").newLine()
                .darkSpacer(" - ").modernGreen("Plan a proxy shutdown with a specific reason and countdown.").newLine()
                .withPrefix().newLine()
                .withPrefix().white("/shutdown cancel").newLine()
                .darkSpacer(" - ").modernGreen("Cancel the planned shutdown.").newLine()
                .withPrefix().newLine()
                .withPrefix().white("/shutdown info").newLine()
                .darkSpacer(" - ").modernGreen("Show information about the planned shutdown.").newLine()
        )
    }

    private fun parseTime(input: String): Int? {
        val match = Regex("(\\d+)([smh])").matchEntire(input.lowercase()) ?: return null
        val (amountStr, unit) = match.destructured
        val amount = amountStr.toIntOrNull() ?: return null
        return when (unit) {
            "s" -> amount
            "m" -> amount * 60
            "h" -> amount * 3600
            else -> null
        }
    }

    private fun formatSeconds(seconds: Int): String {
        return when {
            seconds >= 3600 -> "${seconds / 3600}h"
            seconds >= 60 -> "${seconds / 60}m"
            else -> "${seconds}s"
        }
    }
}
