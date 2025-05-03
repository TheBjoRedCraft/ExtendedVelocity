package dev.thebjoredcraft.extendedvelocity.maintenance

import dev.thebjoredcraft.extendedvelocity.plugin
import dev.thebjoredcraft.extendedvelocity.util.maintenanceConfig
import dev.thebjoredcraft.extendedvelocity.util.miniMessage
import dev.thebjoredcraft.extendedvelocity.util.motdConfig

object MaintenanceService {
    private var maintenanceMode: Boolean = false
    var kickMessage: String = "<red>The server is currently under maintenance. Please try again later."
    var kickOnEnable: Boolean = true

    var motdEnabled: Boolean = true
    var motd: String = ""

    var versionEnabled: Boolean = true
    var version: String = "1.0.0"
    var versionID = 1

    var playerCountEnabled: Boolean = true
    var playerCountOnline: String = "%online%"
    var playerCountMax: String = "%online_max%"

    var samplePlayersEnabled: Boolean = true
    var samplePlayers: List<String> = listOf("Line1", "Line2", "Line3")


    fun load() {
        maintenanceMode = maintenanceConfig.boolean("status")
        kickMessage = maintenanceConfig.string("kick.message")
        kickOnEnable = maintenanceConfig.boolean("kick.onEnable")

        motdEnabled = motdConfig.boolean("motd.enabled")
        motd = motdConfig.string("motd.message")

        versionEnabled = motdConfig.boolean("version.enabled")
        versionID = motdConfig.int("version.version")
        version = motdConfig.string("version.message")

        playerCountEnabled = motdConfig.boolean("playerCount.enabled")
        playerCountOnline = motdConfig.string("playerCount.onlinePlayers")
        playerCountMax = motdConfig.string("playerCount.maxPlayers")

        samplePlayersEnabled = motdConfig.boolean("samplePlayers.enabled")
        samplePlayers = motdConfig.list("samplePlayers.players")
    }

    fun save() {
        //maintenanceConfig.set("status", maintenanceMode)
        //maintenanceConfig.save()
    }

    fun reload() {
        this.save()
        this.load()
    }

    fun enable() {
        maintenanceMode = true

        if(kickOnEnable) {
            plugin.proxy.allPlayers
                .filter { !it.hasPermission("extendedvelocity.maintenance.bypass") }
                .forEach { it.disconnect(kickMessage.miniMessage()) }
        }
    }

    fun disable() {
        maintenanceMode = false
    }

    fun isEnabled(): Boolean {
        return maintenanceMode
    }
}