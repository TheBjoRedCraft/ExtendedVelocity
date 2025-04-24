package dev.thebjoredcraft.extendedvelocity.command

import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.ProxyServer
import dev.thebjoredcraft.extendedvelocity.message.MessageBuilder
import dev.thebjoredcraft.extendedvelocity.plugin
import dev.thebjoredcraft.extendedvelocity.util.error
import dev.thebjoredcraft.extendedvelocity.util.sendRawText
import dev.thebjoredcraft.extendedvelocity.util.sendText
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

class ShutdownCommand() : SimpleCommand {
    override fun execute(invocation: SimpleCommand.Invocation) {
        val source = invocation.source()
        val args = invocation.arguments()

        if (args.isEmpty()) {
            this.sendUsage(source)
            return
        }

        val timeArg = args[0]
        val delayInSeconds = parseTime(timeArg)

        if (delayInSeconds == null) {
            source.error("Incorrect time format. Please use 10m, 1h, 30s...")
            return
        }

        val reason = args.drop(1).joinToString(" ").ifEmpty { "Proxy shutting down." }
        source.sendText("Successfully planned a proxy shutdown in ${formatSeconds(delayInSeconds)} with reason: $reason")

        for (i in delayInSeconds downTo 1) {
            val shouldNotify = when {
                i > 30 -> i % 30 == 0
                i > 10 -> i % 10 == 0
                i > 5 -> i % 5 == 0
                else -> true
            }

            if (shouldNotify) {
                plugin.proxy.scheduler.buildTask(plugin, Consumer {
                    val message = MessageBuilder()
                        .newLine()
                        .withPrefix().modernGreen(reason).newLine()
                        .withPrefix().white("Shutdown in ${formatSeconds(i)}...").newLine()
                        .newLine()
                    plugin.proxy.allPlayers.forEach { it.sendRawText(message) }
                }).delay((delayInSeconds - i).toLong(), TimeUnit.SECONDS).schedule()

            }
        }

        plugin.proxy.scheduler.buildTask(plugin, Consumer {
            plugin.proxy.shutdown()
        }).delay(delayInSeconds.toLong(), TimeUnit.SECONDS).schedule()
    }

    override fun hasPermission(invocation: SimpleCommand.Invocation): Boolean {
        return invocation.source().hasPermission("extendedvelocity.command.shutdown")
    }

    private fun sendUsage(source: CommandSource) {
        source.sendRawText(
            MessageBuilder().spacer(" ")
                .withPrefix().modernGreen("Available Arguments/Sub Commands for /shutdown").newLine()
                .withPrefix().newLine()
                .withPrefix().white("/shutdown <zeit> <grund>").newLine()
                .darkSpacer(" - ").modernGreen("Shutdown the proxy with a specific reason and countdown.").newLine()
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
