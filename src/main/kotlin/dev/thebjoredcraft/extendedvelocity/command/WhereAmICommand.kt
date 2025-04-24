package dev.thebjoredcraft.extendedvelocity.command

import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.Player
import dev.thebjoredcraft.extendedvelocity.message.MessageBuilder
import dev.thebjoredcraft.extendedvelocity.util.error
import dev.thebjoredcraft.extendedvelocity.util.formatTime
import dev.thebjoredcraft.extendedvelocity.util.sendRawText
import dev.thebjoredcraft.extendedvelocity.util.sendText
import kotlin.jvm.optionals.getOrNull

class WhereAmICommand: SimpleCommand {
    override fun execute(invocation: SimpleCommand.Invocation) {
        val source = invocation.source()
        val args = invocation.arguments()

        if(args.isNotEmpty()) {
            this.sendUsage(source)
            return
        }

        val player = source as? Player ?: run {
            source.error("This command can only be used by players.")
            return
        }

        val connection = player.currentServer.getOrNull() ?: run {
            source.error("You are not connected to a server.")
            return
        }

        player.sendText("You (${player.username}, ${player.uniqueId}) are currently (${
            formatTime(
                System.currentTimeMillis()
            )
        }) on ${connection.serverInfo.name} with a ping of ${player.ping}ms.")
    }

    override fun hasPermission(invocation: SimpleCommand.Invocation): Boolean {
        return invocation.source().hasPermission("extendedvelocity.command.whereami")
    }

    private fun sendUsage(source: CommandSource) {
        source.sendRawText(
            MessageBuilder().spacer(" ")
            .withPrefix().modernGreen("Available Arguments/Sub Commands for /whereami").newLine()
            .withPrefix().newLine()
            .withPrefix().white("/whereami").newLine()
            .darkSpacer(" - ").modernGreen("Show the current server of yourself.").newLine()
        )
    }
}