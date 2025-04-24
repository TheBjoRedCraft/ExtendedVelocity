package dev.thebjoredcraft.extendedvelocity

import com.google.gson.Gson
import com.google.inject.Inject
import com.velocitypowered.api.command.CommandMeta
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.PluginContainer
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import dev.thebjoredcraft.extendedvelocity.command.BroadcastCommand
import dev.thebjoredcraft.extendedvelocity.command.FindCommand
import org.slf4j.Logger
import java.nio.file.Path

@Plugin (
    id = "extendedvelocity",
    name = "ExtendedVelocity",
    version = "1.0.0-SNAPSHOT",
    description = "Do more with your proxy.",
    authors = ["TheBjoRedCraft"],
    url = "https://github.com/TheBjoRedCraft/ExtendedVelocity/",
    dependencies = []
)
class ExtendedVelocity {
    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var proxy: ProxyServer

    @Inject
    lateinit var pluginContainer: PluginContainer

    @Inject
    @DataDirectory
    lateinit var dataFolder: Path

    @Subscribe
    fun onInitialization(event: ProxyInitializeEvent) {
        INSTANCE = this

        val commandManager = proxy.commandManager

        commandManager.register(commandManager.metaBuilder("find").build(), FindCommand())
        commandManager.register(commandManager.metaBuilder("broadcast").aliases("alert").build(), BroadcastCommand())
    }

    @Subscribe
    fun onShutdown(event: ProxyShutdownEvent) {

    }

    companion object {
        lateinit var INSTANCE: ExtendedVelocity
            private set
    }
}

val gson = Gson()
val plugin get() = ExtendedVelocity.INSTANCE