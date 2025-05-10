package dev.thebjoredcraft.extendedvelocity.command

import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.Player

import dev.thebjoredcraft.extendedvelocity.message.MessageBuilder
import dev.thebjoredcraft.extendedvelocity.plugin
import dev.thebjoredcraft.extendedvelocity.util.error
import dev.thebjoredcraft.extendedvelocity.util.sendRawText
import dev.thebjoredcraft.extendedvelocity.util.sendText
import kotlin.jvm.optionals.getOrNull

class SendCommand : SimpleCommand {
    override fun execute(invocation: SimpleCommand.Invocation) {
        val source = invocation.source()
        val args = invocation.arguments()

        if (args.size < 2) {
            sendUsage(source)
            return
        }

        val selector = args[0]
        val targetServer = args[1]

        if (!source.hasPermission("extendedvelocity.send.server.$targetServer") && !source.hasPermission("extendedvelocity.send.server.*")) {
            sendUsage(source)
            return
        }

        val players = when (selector) {
            "*" -> plugin.proxy.allPlayers
            "current" -> {
                val senderPlayer = source as? Player
                if (senderPlayer == null) {
                    source.error("Only players can use 'current'.")
                    return
                }

                plugin.proxy.allPlayers.filter {
                    it.currentServer.getOrNull()?.serverInfo?.name == senderPlayer.currentServer.getOrNull()?.serverInfo?.name
                }
            }
            "other" -> {
                val senderPlayer = source as? Player
                if (senderPlayer == null) {
                    source.error("Only players can use 'other'.")
                    return
                }
                plugin.proxy.allPlayers.filter {
                    it.currentServer.getOrNull()?.serverInfo?.name != senderPlayer.currentServer.getOrNull()?.serverInfo?.name
                }
            }
            else -> {
                val target = plugin.proxy.getPlayer(selector).getOrNull()
                if (target == null) {
                    source.error("The requested player was not found.")
                    return
                }
                listOf(target)
            }
        }

        val allowedPlayers = players.filter { player ->
            when (selector) {
                "*" -> source.hasPermission("extendedvelocity.send.target.*")
                "current" -> source.hasPermission("extendedvelocity.send.target.current") || source.hasPermission("extendedvelocity.send.target.*")
                "other" -> source.hasPermission("extendedvelocity.send.target.other") || source.hasPermission("extendedvelocity.send.target.*")
                else -> source.hasPermission("extendedvelocity.send.target.${player.username}") || source.hasPermission("extendedvelocity.send.target.*")
            }
        }

        if (allowedPlayers.isEmpty()) {
            source.sendText("&cDu hast keine Berechtigung, einen dieser Spieler zu senden.")
            return
        }

        val server = plugin.proxy.getServer(targetServer).getOrNull()

        if (server == null) {
            source.sendText("&cZielserver '$targetServer' nicht gefunden.")
            return
        }

        var failed = 0

        allowedPlayers.forEach { player ->
            player.createConnectionRequest(server).connect().thenAccept {
                if (it.isSuccessful) {
                    source.sendText(MessageBuilder().modernGreen("Successfully connected to ${server.serverInfo.name}"))
                } else {
                    source.error("Failed to connect to ${server.serverInfo.name}.")
                    failed++
                }
            }.exceptionally {
                failed++
                source.error("An error occurred while trying to connect to ${server.serverInfo.name}: ${it.message}")
                null
            }
        }

        source.sendText(MessageBuilder().modernGreen("Successfully sent ${allowedPlayers.size - failed} player(s) to $targetServer, failed to send $failed player(s)."))
    }


    override fun hasPermission(invocation: SimpleCommand.Invocation): Boolean {
        return invocation.source().hasPermission("extendedvelocity.send.command")
    }

    private fun sendUsage(source: CommandSource) {
        source.sendRawText(
            MessageBuilder().spacer(" ")
                .withPrefix().modernGreen("Command Help for /send").newLine()
                .withPrefix().newLine()
                .withPrefix().white("/send <player(s)> <server>").newLine()
                .darkSpacer(" - ").modernGreen("Switch to a backend server of your choice.").newLine()
        )
    }

    override fun suggest(invocation: SimpleCommand.Invocation): List<String> {
        val source = invocation.source()
        val args = invocation.arguments()

        return when (args.size) {
            0, 1 -> {
                val suggestions = mutableListOf<String>()

                if (source.hasPermission("extendedvelocity.send.target.*") || source.hasPermission("extendedvelocity.send.target.current")) {
                    suggestions.add("current")
                }
                if (source.hasPermission("extendedvelocity.send.target.*") || source.hasPermission("extendedvelocity.send.target.other")) {
                    suggestions.add("other")
                }
                if (source.hasPermission("extendedvelocity.send.target.*")) {
                    suggestions.add("*")
                }

                suggestions.addAll(
                    plugin.proxy.allPlayers
                        .filter { source.hasPermission("extendedvelocity.send.target.${it.username}") || source.hasPermission("extendedvelocity.send.target.*") }
                        .map { it.username }
                )

                suggestions.sorted()
            }

            2 -> {
                plugin.proxy.allServers
                    .filter { source.hasPermission("extendedvelocity.send.server.${it.serverInfo.name}") || source.hasPermission("extendedvelocity.send.server.*") }
                    .map { it.serverInfo.name }
                    .sorted()
            }
            else -> emptyList()
        }
    }

}

