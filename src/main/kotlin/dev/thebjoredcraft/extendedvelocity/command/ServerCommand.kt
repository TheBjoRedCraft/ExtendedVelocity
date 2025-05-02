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

class ServerCommand : SimpleCommand {
    override fun execute(invocation: SimpleCommand.Invocation) {
        val source = invocation.source()
        val args = invocation.arguments()

        if (args.isEmpty() || args.size > 1) {
            this.sendUsage(source)
            return
        }

        val server = plugin.proxy.getServer(args[0]).orElse(null) ?: run {
            source.error("The requested server was not found.")
            return
        }

        val permissionNode = "extendedvelocity.server.${server.serverInfo.name}"

        if (!source.hasPermission(permissionNode) && !source.hasPermission("extendedvelocity.server.*")) {
            source.error("You don't have permission to connect to this server.")
            return
        }

        val player = source as? Player ?: run {
            source.error("This command can only be used by players.")
            return
        }

        if (player.currentServer.getOrNull()?.serverInfo?.name == server.serverInfo.name) {
            source.error("You are already connected to this server.")
            return
        }

        player.createConnectionRequest(server).connect().thenAccept {
            if (it.isSuccessful) {
                source.sendText(MessageBuilder().modernGreen("Successfully connected to ${server.serverInfo.name}"))
            } else {
                source.error("Failed to connect to ${server.serverInfo.name}.")
            }
        }.exceptionally {
            source.error("An error occurred while trying to connect to ${server.serverInfo.name}: ${it.message}")
            null
        }
    }

    override fun hasPermission(invocation: SimpleCommand.Invocation): Boolean {
        return invocation.source().hasPermission("extendedvelocity.server.command")
    }

    private fun sendUsage(source: CommandSource) {
        source.sendRawText(
            MessageBuilder().spacer(" ")
                .withPrefix().modernGreen("Available Arguments/Sub Commands for /server").newLine()
                .withPrefix().newLine()
                .withPrefix().white("/server <server>").newLine()
                .darkSpacer(" - ").modernGreen("Switch to a backend server of your choice.").newLine()
        )
    }

    override fun suggest(invocation: SimpleCommand.Invocation): List<String> {
        val source = invocation.source()
        val args = invocation.arguments()

        if (args.size <= 1) {
            return plugin.proxy.allServers
                .filter { source.hasPermission("extendedvelocity.server.*") || source.hasPermission("extendedvelocity.server.${it.serverInfo.name}") }
                .map { it.serverInfo.name }
        }

        return emptyList()
    }
}

