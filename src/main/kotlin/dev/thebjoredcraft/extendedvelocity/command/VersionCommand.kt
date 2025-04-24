package dev.thebjoredcraft.extendedvelocity.command

import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.command.SimpleCommand

import dev.thebjoredcraft.extendedvelocity.message.MessageBuilder
import dev.thebjoredcraft.extendedvelocity.plugin
import dev.thebjoredcraft.extendedvelocity.util.sendRawText
import dev.thebjoredcraft.extendedvelocity.util.sendText

class VersionCommand: SimpleCommand {
    override fun execute(invocation: SimpleCommand.Invocation) {
        val source = invocation.source()
        val args = invocation.arguments()

        if(args.isNotEmpty()) {
            this.sendUsage(source)
            return
        }

        source.sendText(MessageBuilder().modernGreen("The proxy server is running ${plugin.proxy.version.name} ${plugin.proxy.version.version} by ${plugin.proxy.version.vendor} on ${System.getProperty("os.name")} ${System.getProperty("os.version")}"))
    }

    override fun hasPermission(invocation: SimpleCommand.Invocation): Boolean {
        return invocation.source().hasPermission("extendedvelocity.command.version")
    }

    private fun sendUsage(source: CommandSource) {
        source.sendRawText(MessageBuilder().spacer(" ")
            .withPrefix().modernGreen("Available Arguments/Sub Commands for /vversion").newLine()
            .withPrefix().newLine()
            .withPrefix().white("/vversion").newLine()
            .darkSpacer(" - ").modernGreen("Show the velocity proxy server version and the host.").newLine()
        )
    }
}