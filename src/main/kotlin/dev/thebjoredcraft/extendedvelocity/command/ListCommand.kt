package dev.thebjoredcraft.extendedvelocity.command

import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.command.SimpleCommand

import dev.thebjoredcraft.extendedvelocity.message.MessageBuilder
import dev.thebjoredcraft.extendedvelocity.plugin
import dev.thebjoredcraft.extendedvelocity.util.sendRawText
import dev.thebjoredcraft.extendedvelocity.util.sendText

class ListCommand: SimpleCommand {
    override fun execute(invocation: SimpleCommand.Invocation) {
        val source = invocation.source()
        val args = invocation.arguments()

        if(args.size > 1) {
            this.sendUsage(source)
            return
        }

        source.sendText(MessageBuilder().modernGreen("The proxy server is running ${plugin.proxy.version.name} ${plugin.proxy.version.version} by ${plugin.proxy.version.vendor} on ${System.getProperty("os.name")} ${System.getProperty("os.version")}"))
    }

    override fun hasPermission(invocation: SimpleCommand.Invocation): Boolean {
        return invocation.source().hasPermission("extendedvelocity.command.list")
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