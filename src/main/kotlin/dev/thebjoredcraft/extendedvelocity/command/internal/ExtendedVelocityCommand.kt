package dev.thebjoredcraft.extendedvelocity.command.internal

import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.command.SimpleCommand

import dev.thebjoredcraft.extendedvelocity.message.MessageBuilder
import dev.thebjoredcraft.extendedvelocity.util.sendRawText
import dev.thebjoredcraft.extendedvelocity.util.sendText
import kotlin.system.measureTimeMillis

class ExtendedVelocityCommand: SimpleCommand {
    override fun execute(invocation: SimpleCommand.Invocation) {
        val source = invocation.source()
        val args = invocation.arguments()

        if (args.isEmpty()) {
            source.sendText(MessageBuilder().modernGreen("This server is running ExtendedVelocity v1.1.0 by TheBjoRedCraft on ${System.getProperty("os.name")} ${System.getProperty("os.version")}"))
            return
        }

        if(args.size > 1) {
            this.sendUsage(source)
            return
        }

        when(args[0].lowercase()) {
            "reload", "rl" -> {
                source.sendText(MessageBuilder().modernGreen("Reloading ExtendedVelocity..."))
                val ms = measureTimeMillis {
                    //TODO: Reload the configs
                }

                source.sendText(MessageBuilder().modernGreen("Successfully reloaded ExtendedVelocity in ${ms}ms!"))
            }

            else -> {
                sendUsage(source)
            }
        }
    }

    override fun suggest(invocation: SimpleCommand.Invocation): List<String> {
        val args = invocation.arguments()

        if (args.size <= 1) {
            return listOf("reload", "rl")
        }

        return emptyList()
    }

    private fun sendUsage(source: CommandSource) {
        source.sendRawText(MessageBuilder().spacer(" ")
            .withPrefix().modernGreen("Available Arguments/Sub Commands for /extendedvelocity").newLine()
            .withPrefix().newLine()
            .withPrefix().white("/extendedvelocity").newLine()
            .darkSpacer(" - ").modernGreen("Shows plugin information.").newLine()
            .withPrefix().newLine()
            .withPrefix().white("/extendedvelocity reload").newLine()
            .darkSpacer(" - ").modernGreen("Reloads the plugin.").newLine()
        )
    }

    override fun hasPermission(invocation: SimpleCommand.Invocation): Boolean {
        return invocation.source().hasPermission("extendedvelocity.command.extendedvelocity")
    }
}