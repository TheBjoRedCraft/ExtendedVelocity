package dev.thebjoredcraft.extendedvelocity.command

import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.command.SimpleCommand
import dev.thebjoredcraft.extendedvelocity.message.Colors

import dev.thebjoredcraft.extendedvelocity.message.MessageBuilder
import dev.thebjoredcraft.extendedvelocity.plugin
import dev.thebjoredcraft.extendedvelocity.util.sendRawText
import dev.thebjoredcraft.extendedvelocity.util.sendText

class BroadcastCommand: SimpleCommand {
    override fun execute(invocation: SimpleCommand.Invocation) {
        val source = invocation.source()
        val args = invocation.arguments()

        if(args.isEmpty()) {
            this.sendUsage(source)
            return
        }

        plugin.proxy.allPlayers.forEach { it.sendRawText(MessageBuilder().spacer(" ")
            .withPrefix().newLine()
            .withPrefix().miniMessage("<${Colors.MODERN_GREEN.asHexString()}><b>${args.joinToString(" ")}").newLine()
            .withPrefix().newLine()
        ) }
        source.sendText("Successfully broadcasted message to all players on the proxy.")
    }

    override fun hasPermission(invocation: SimpleCommand.Invocation): Boolean {
        return invocation.source().hasPermission("extendedvelocity.command.broadcast")
    }

    private fun sendUsage(source: CommandSource) {
        source.sendRawText(MessageBuilder().spacer(" ")
            .withPrefix().modernGreen("Available Arguments/Sub Commands for /broadcast").newLine()
            .withPrefix().newLine()
            .withPrefix().white("/broadcast <message>").newLine()
            .darkSpacer(" - ").modernGreen("Broadcast a message to all players on the proxy").newLine()
        )
    }
}