package dev.thebjoredcraft.extendedvelocity.command

import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.command.SimpleCommand

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

        source.sendText("Successfully broadcasted message to all players on the proxy.")
        plugin.proxy.allPlayers.forEach { it.sendText(MessageBuilder().white(args.joinToString(" "))) }

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