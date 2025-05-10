package dev.thebjoredcraft.extendedvelocity.command

import com.github.shynixn.mccoroutine.velocity.launch
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.command.SimpleCommand
import dev.thebjoredcraft.extendedvelocity.container

import dev.thebjoredcraft.extendedvelocity.message.MessageBuilder
import dev.thebjoredcraft.extendedvelocity.playtime.PlaytimeService
import dev.thebjoredcraft.extendedvelocity.plugin
import dev.thebjoredcraft.extendedvelocity.util.error
import dev.thebjoredcraft.extendedvelocity.util.formatTime
import dev.thebjoredcraft.extendedvelocity.util.sendRawText
import dev.thebjoredcraft.extendedvelocity.util.sendText
import kotlin.jvm.optionals.getOrNull

class LastseenCommand: SimpleCommand {
    override fun execute(invocation: SimpleCommand.Invocation) {
        val source = invocation.source()
        val args = invocation.arguments()

        if(args.isEmpty() || args.size > 1) {
            this.sendUsage(source)
            return
        }

        val player = plugin.proxy.getPlayer(args[0]).getOrNull()

        if (player != null) {
            source.sendText(MessageBuilder().modernGreen("${args[0]} is currently online."))
            return
        }

        container.launch {
            val lastSeen = PlaytimeService.NameMechanics.getLastSeen(args[0])

            if (lastSeen == null) {
                source.sendText(MessageBuilder().error("Player ${args[0]} has never been seen before."))
                return@launch
            }

            source.sendText(MessageBuilder().modernGreen("${args[0]} was last seen on ${formatTime(lastSeen)}."))
        }
    }

    override fun suggest(invocation: SimpleCommand.Invocation): List<String> {
        val args = invocation.arguments()

        if (args.size <= 1) {
            return plugin.proxy.allPlayers.map { it.username }
        }

        return emptyList()
    }

    override fun hasPermission(invocation: SimpleCommand.Invocation): Boolean {
        return invocation.source().hasPermission("extendedvelocity.lastseen.command")
    }

    private fun sendUsage(source: CommandSource) {
        source.sendRawText(MessageBuilder().spacer(" ")
            .withPrefix().modernGreen("Available Arguments/Sub Commands for /lastseen").newLine()
            .withPrefix().newLine()
            .withPrefix().white("/lastseen <player>").newLine()
            .darkSpacer(" - ").modernGreen("Check the last online time of a player.").newLine()
        )
    }
}