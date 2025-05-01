package dev.thebjoredcraft.extendedvelocity.command.maintenance

import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.command.SimpleCommand
import dev.thebjoredcraft.extendedvelocity.message.MessageBuilder
import dev.thebjoredcraft.extendedvelocity.util.sendRawText

class MaintenanceCommand: SimpleCommand {
    override fun execute(invocation: SimpleCommand.Invocation) {
        val args = invocation.arguments()
        val source = invocation.source()

        if (args.isEmpty()) {
            this.sendUsage(source)
            return
        }

        when (args[0]) {
            "status" -> {

            }
            "enable" -> {

            }
            "disable" -> {

            }
            else -> {
                this.sendUsage(source)
            }
        }
    }

    override fun hasPermission(invocation: SimpleCommand.Invocation): Boolean {
        return invocation.source().hasPermission("extendedvelocity.command.maintenance")
    }

    private fun sendUsage(source: CommandSource) {
        source.sendRawText(MessageBuilder().spacer(" ")
            .withPrefix().modernGreen("Available Arguments/Sub Commands for /maintenance").newLine()
            .withPrefix().newLine()
            .withPrefix().white("/maintenance status").newLine()
            .darkSpacer(" - ").modernGreen("Shows the current maintenance status.").newLine()
            .withPrefix().newLine()
            .withPrefix().white("/maintenance enable").newLine()
            .darkSpacer(" - ").modernGreen("Enables the maintenance.").newLine()
            .withPrefix().newLine()
            .withPrefix().white("/maintenance disable").newLine()
            .darkSpacer(" - ").modernGreen("Disables the maintenance.").newLine()
        )
    }

    override fun suggest(invocation: SimpleCommand.Invocation): List<String> {
        val args = invocation.arguments()

        if (args.size <= 1) {
            return listOf("status", "enable", "disable")
        }

        return emptyList()
    }
}