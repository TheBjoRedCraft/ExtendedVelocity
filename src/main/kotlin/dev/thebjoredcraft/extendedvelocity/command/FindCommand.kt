package dev.thebjoredcraft.extendedvelocity.command

import com.velocitypowered.api.command.SimpleCommand

import dev.thebjoredcraft.extendedvelocity.message.MessageBuilder
import dev.thebjoredcraft.extendedvelocity.plugin
import dev.thebjoredcraft.extendedvelocity.util.error
import dev.thebjoredcraft.extendedvelocity.util.sendRawText

import kotlin.jvm.optionals.getOrNull

class FindCommand: SimpleCommand {
    override fun execute(invocation: SimpleCommand.Invocation) {
        val source = invocation.source()
        val args = invocation.arguments()

        if(args.isEmpty()) {
            source.sendRawText(MessageBuilder()
                .withPrefix()
                .modernGreen("Find a player and their server connection.")
                .newLine()
                .withPrefix()
                .newLine()
                .withPrefix()
                .modernGreen("/find <player>")
            )

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

        source.sendRawText(MessageBuilder()
            .withPrefix()
            .modernGreen("Server Connection Information for ${target.username}").newLine()
            .withPrefix().newLine()
            .withPrefix().newLine()
            .spacer("Server: ").modernGreen(connection.serverInfo.name).newLine()
            .spacer("Ping: ").modernGreen("${target.ping}ms").newLine()
        )
    }

    override fun hasPermission(invocation: SimpleCommand.Invocation): Boolean {
        return invocation.source().hasPermission("extendedvelocity.command.find")
    }

    override fun suggest(invocation: SimpleCommand.Invocation): List<String> {
        return plugin.proxy.allPlayers.map { it.username }
    }
}