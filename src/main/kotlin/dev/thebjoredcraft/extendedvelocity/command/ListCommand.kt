package dev.thebjoredcraft.extendedvelocity.command

import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.command.SimpleCommand

import dev.thebjoredcraft.extendedvelocity.message.MessageBuilder
import dev.thebjoredcraft.extendedvelocity.plugin
import dev.thebjoredcraft.extendedvelocity.util.error
import dev.thebjoredcraft.extendedvelocity.util.sendRawText
import dev.thebjoredcraft.extendedvelocity.util.sendText
import net.kyori.adventure.text.Component
import kotlin.jvm.optionals.getOrNull

class ListCommand: SimpleCommand {
    override fun execute(invocation: SimpleCommand.Invocation) {
        val source = invocation.source()
        val args = invocation.arguments()

        if(args.size > 1) {
            this.sendUsage(source)
            return
        }


        if(args.isEmpty()) {
            val players = plugin.proxy.allPlayers.sortedByDescending { it.currentServer.getOrNull()?.serverInfo?.name }

            if (players.isEmpty()) {
                source.error("There are no players online.")
                return
            }

            val playerList = players.joinToString(", ") { it.username }

            source.sendText(MessageBuilder()
                .modernGreen("There are ${players.size} players online: ")
                .white(playerList)
            )

        } else {
            val serverName = args[0]

            val server = plugin.proxy.getServer(serverName).getOrNull() ?: run {
                source.sendText(MessageBuilder().error("Server $serverName not found."))
                return
            }

            val players = server.playersConnected.sortedByDescending { it.username }

            if (players.isEmpty()) {
                source.error("There are no players online on $serverName.")
                return
            }

            val playerList = players.joinToString(", ") { it.username }

            source.sendText(MessageBuilder()
                .modernGreen("There are ${players.size} players online on $serverName: ")
                .white(playerList)
            )
        }
    }

    override fun hasPermission(invocation: SimpleCommand.Invocation): Boolean {
        return invocation.source().hasPermission("extendedvelocity.list.command")
    }

    override fun suggest(invocation: SimpleCommand.Invocation): List<String> {
        val args = invocation.arguments()

        if (args.size <= 1) {
            return plugin.proxy.allServers.map { it.serverInfo.name }
        }

        return emptyList()
    }

    private fun sendUsage(source: CommandSource) {
        source.sendRawText(MessageBuilder().spacer(" ")
            .withPrefix().modernGreen("Available Arguments/Sub Commands for /list").newLine()
            .withPrefix().newLine()
            .withPrefix().white("/list").newLine()
            .darkSpacer(" - ").modernGreen("Shows a list of all onlineplayers.").newLine()
            .withPrefix().newLine()
            .withPrefix().white("/list <server>").newLine()
            .darkSpacer(" - ").modernGreen("Shows a list of onlineplayers of a specific server.").newLine()
        )
    }
}