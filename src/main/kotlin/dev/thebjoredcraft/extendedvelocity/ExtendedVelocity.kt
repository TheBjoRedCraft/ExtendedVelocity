package dev.thebjoredcraft.extendedvelocity

import com.google.gson.Gson
import com.google.inject.Inject

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.PluginContainer
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import dev.thebjoredcraft.extendedvelocity.brand.CustomBrandService

import dev.thebjoredcraft.extendedvelocity.command.*
import dev.thebjoredcraft.extendedvelocity.command.internal.ExtendedVelocityCommand
import dev.thebjoredcraft.extendedvelocity.command.maintenance.MaintenanceCommand
import dev.thebjoredcraft.extendedvelocity.config.Config
import dev.thebjoredcraft.extendedvelocity.maintenance.MaintenanceListener
import dev.thebjoredcraft.extendedvelocity.maintenance.MaintenanceService
import dev.thebjoredcraft.extendedvelocity.message.Colors
import dev.thebjoredcraft.extendedvelocity.motd.MotdListener
import dev.thebjoredcraft.extendedvelocity.motd.MotdService

import org.bstats.velocity.Metrics
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
    lateinit var metricsFactory: Metrics.Factory

    @Inject
    @DataDirectory
    lateinit var dataFolder: Path

    var pluginConfig: Config? = null
    var maintenanceConfig: Config? = null
    var brandConfig: Config? = null
    var motdConfig: Config? = null

    @Subscribe
    fun onInitialization(event: ProxyInitializeEvent) {
        INSTANCE = this
        this.loadConfigurations()
        metricsFactory.make(this, 25615)

        val commandManager = proxy.commandManager
        val eventManager = proxy.eventManager

        commandManager.register(commandManager.metaBuilder("lookup").build(), LookupCommand())
        commandManager.register(commandManager.metaBuilder("broadcast").aliases("alert").build(), BroadcastCommand())
        commandManager.register(commandManager.metaBuilder("vversion").aliases("vver").build(), VersionCommand())
        commandManager.register(commandManager.metaBuilder("vplugins").aliases("vpl").build(), PluginsCommand())
        commandManager.register(commandManager.metaBuilder("server").build(), ServerCommand())
        commandManager.register(commandManager.metaBuilder("shutdown").build(), ShutdownCommand())
        commandManager.register(commandManager.metaBuilder("whereami").build(), WhereAmICommand())
        commandManager.register(commandManager.metaBuilder("extendedvelocity").aliases("ev").build(), ExtendedVelocityCommand())
        commandManager.register(commandManager.metaBuilder("list").aliases("vlist").build(), ListCommand())
        commandManager.register(commandManager.metaBuilder("maintenance").build(), MaintenanceCommand())

        eventManager.register(this, MaintenanceListener())
        eventManager.register(this, MotdListener())

        MaintenanceService.load()
        CustomBrandService.load()
        CustomBrandService.start()
        MotdService.load()
        Colors.loadPrefix()
    }

    fun loadConfigurations() {
        pluginConfig = Config(folder = dataFolder, fileName = "config")
        pluginConfig?.load()

        maintenanceConfig = Config(folder = dataFolder, fileName = "maintenance")
        maintenanceConfig?.load()

        brandConfig = Config(folder = dataFolder, fileName = "server-brand")
        brandConfig?.load()

        motdConfig = Config(folder = dataFolder, fileName = "motd")
        motdConfig?.load()
    }

    @Subscribe
    fun onShutdown(event: ProxyShutdownEvent) {
        MaintenanceService.save()
        CustomBrandService.stop()
    }

    companion object {
        lateinit var INSTANCE: ExtendedVelocity
            private set
    }
}

val gson = Gson()
val plugin get() = ExtendedVelocity.INSTANCE