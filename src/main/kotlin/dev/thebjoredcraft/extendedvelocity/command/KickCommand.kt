package dev.thebjoredcraft.extendedvelocity.command

import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.command.SimpleCommand
import dev.thebjoredcraft.extendedvelocity.message.Colors
import dev.thebjoredcraft.extendedvelocity.message.MessageBuilder
import dev.thebjoredcraft.extendedvelocity.plugin
import dev.thebjoredcraft.extendedvelocity.util.error
import dev.thebjoredcraft.extendedvelocity.util.sendRawText
import dev.thebjoredcraft.extendedvelocity.util.sendText
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import kotlin.jvm.optionals.getOrNull

class KickCommand : SimpleCommand {
    override fun execute(invocation: SimpleCommand.Invocation) {
        val source = invocation.source()
        val args = invocation.arguments()
        val reason: String

        if (args.isEmpty() || args.size > 2) {
            this.sendUsage(source)
            return
        }

        if (args.size == 1) {
            reason = "<red>No reason given.</red>"
        } else {
            reason = args[1]
        }

        val target = plugin.proxy.getPlayer(args[0]).getOrNull() ?: run {
            source.error("The requested player was not found.")
            return
        }

        val connection = target.currentServer.getOrNull()

        if (connection == null) {
            source.error("The requested player is not connected to a server.")
            return
        }

        val targetName = target.username

        target.disconnect(
            Component.text("You have been kicked from the network.").appendNewline().append(
                Component.text("Reason: ", Colors.WHITE)
                    .append(MiniMessage.miniMessage().deserialize(reason))
            )
        )
        source.sendText(MessageBuilder().modernGreen("Successfully kicked $targetName from the network."))
    }

    override fun hasPermission(invocation: SimpleCommand.Invocation): Boolean {
        return invocation.source().hasPermission("extendedvelocity.kick.command")
    }

    override fun suggest(invocation: SimpleCommand.Invocation): List<String> {
        val args = invocation.arguments()

        if (args.size <= 1) {
            return plugin.proxy.allPlayers.map { it.username }
        }

        return emptyList()
    }

    private fun sendUsage(source: CommandSource) {
        source.sendRawText(
            MessageBuilder().spacer(" ")
                .withPrefix().modernGreen("Available Arguments/Sub Commands for /kick").newLine()
                .withPrefix().newLine()
                .withPrefix().white("/kick <player> [<reason>]").newLine()
                .darkSpacer(" - ")
                .modernGreen("Kick a player from the network with a optional reason.")
                .newLine()
        )
    }
}