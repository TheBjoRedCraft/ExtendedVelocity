package dev.thebjoredcraft.extendedvelocity.command

import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.command.SimpleCommand

import dev.thebjoredcraft.extendedvelocity.message.MessageBuilder
import dev.thebjoredcraft.extendedvelocity.plugin
import dev.thebjoredcraft.extendedvelocity.util.sendRawText
import dev.thebjoredcraft.extendedvelocity.util.sendText
import net.kyori.adventure.text.Component
import kotlin.sequences.ifEmpty

class PluginsCommand: SimpleCommand {
    override fun execute(invocation: SimpleCommand.Invocation) {
        val source = invocation.source()
        val args = invocation.arguments()

        if (args.isNotEmpty()) {
            this.sendUsage(source)
            return
        }

        val plugins = plugin.proxy.pluginManager.plugins
        val pluginList = plugins.map {
            val name = it.description.name.orElse("Unknown")
            val version = it.description.version.orElse("Unknown")
            val authors = it.description.authors.joinToString(", ").ifEmpty { "Unknown" }
            val description = it.description.description.orElse("No description provided.")
            val isInbuild = it.description.id == "velocity"

            MessageBuilder()
                .white(name)
                .hover(
                    MessageBuilder()
                        .modernGreen("Name: ").white(name).apply {
                            if (isInbuild) this.white(" (inbuild)")
                        }.newLine()
                        .modernGreen("Version: ").white(version).newLine()
                        .modernGreen("Authors: ").white(authors).newLine()
                        .modernGreen("Description: ").white(description)
                )
                .build()
        }

        source.sendText (
            MessageBuilder()
                .modernGreen("There are ${plugins.size} plugins loaded: ")
                .component(pluginList.reduce { acc, component -> acc.append(Component.text(", ")).append(component) })
        )
    }

    override fun hasPermission(invocation: SimpleCommand.Invocation): Boolean {
        return invocation.source().hasPermission("extendedvelocity.plugins.command")
    }

    private fun sendUsage(source: CommandSource) {
        source.sendRawText(MessageBuilder().spacer(" ")
            .withPrefix().modernGreen("Available Arguments/Sub Commands for /vplugins").newLine()
            .withPrefix().newLine()
            .withPrefix().white("/vplugins").newLine()
            .darkSpacer(" - ").modernGreen("Show a list of plugins of the velocity proxy.").newLine()
        )
    }
}