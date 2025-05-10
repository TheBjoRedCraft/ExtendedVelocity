package dev.thebjoredcraft.extendedvelocity.command

import com.github.shynixn.mccoroutine.velocity.launch
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.command.SimpleCommand
import dev.thebjoredcraft.extendedvelocity.container
import dev.thebjoredcraft.extendedvelocity.database.DatabaseService

import dev.thebjoredcraft.extendedvelocity.message.MessageBuilder
import dev.thebjoredcraft.extendedvelocity.playtime.PlaytimeService
import dev.thebjoredcraft.extendedvelocity.plugin
import dev.thebjoredcraft.extendedvelocity.util.error
import dev.thebjoredcraft.extendedvelocity.util.formatTime
import dev.thebjoredcraft.extendedvelocity.util.sendRawText
import dev.thebjoredcraft.extendedvelocity.util.sendText

import kotlin.jvm.optionals.getOrNull

class LookupCommand: SimpleCommand {
    override fun execute(invocation: SimpleCommand.Invocation) {
        val source = invocation.source()
        val args = invocation.arguments()

        if(args.isEmpty() || args.size > 1) {
            this.sendUsage(source)
            return
        }

        val target = plugin.proxy.getPlayer(args[0]).getOrNull() ?: run {
            source.error("The requested player was not found.")
            return
        }

        val connection = target.currentServer.getOrNull()

        if(connection == null) {
            source.error("The requested player is not connected to a server.")
            return
        }

        source.sendText(MessageBuilder().modernGreen("Requesting data of ${target.username}..."))

        container.launch {
            source.sendRawText(MessageBuilder().spacer(" ")
                .withPrefix().modernGreen("Server Connection of ${target.username}").newLine()
                .withPrefix().newLine()
                .withPrefix().modernGreen("Server: ").white(connection.serverInfo.name).newLine()
                .withPrefix().modernGreen("Ping: ").white("${target.ping}ms").newLine()
                .withPrefix().modernGreen("Address: ").white(target.remoteAddress.toString()).newLine()
                .withPrefix().modernGreen("Client: ").white(target.clientBrand ?: "N/A").newLine()
                .withPrefix().modernGreen("First Seen: ").white(formatTime(PlaytimeService.UuidMechanics.getFirstSeen(target.uniqueId) ?: System.currentTimeMillis())).newLine()
                .globalCommand("/server ${connection.serverInfo.name}")
                .globalHover(MessageBuilder().modernGreen("Click to connect to ${connection.serverInfo.name}"))
            )
        }
    }

    override fun hasPermission(invocation: SimpleCommand.Invocation): Boolean {
        return invocation.source().hasPermission("extendedvelocity.lookup.command")
    }

    override fun suggest(invocation: SimpleCommand.Invocation): List<String> {
        val args = invocation.arguments()

        if(args.size <= 1) {
            return plugin.proxy.allPlayers.map { it.username }
        }

        return emptyList()
    }

    private fun sendUsage(source: CommandSource) {
        source.sendRawText(MessageBuilder().spacer(" ")
            .withPrefix().modernGreen("Available Arguments/Sub Commands for /lookup").newLine()
            .withPrefix().newLine()
            .withPrefix().white("/lookup <player>").newLine()
            .darkSpacer(" - ").modernGreen("Lookup a player and their server connection.").newLine()
        )
    }
}