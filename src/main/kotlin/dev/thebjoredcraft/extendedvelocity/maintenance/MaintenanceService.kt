package dev.thebjoredcraft.extendedvelocity.maintenance

import dev.thebjoredcraft.extendedvelocity.plugin
import dev.thebjoredcraft.extendedvelocity.util.maintenanceConfig
import dev.thebjoredcraft.extendedvelocity.util.miniMessage

object MaintenanceService {
    private var maintenanceMode: Boolean = false
    var kickMessage: String = "<red>The server is currently under maintenance. Please try again later."

    var pingVersion: String = "Some version"
    var pingMotd: String = "<red>The server is currently under maintenance. \nPlease try again later."
    var pingHover: List<String> = listOf("<red>The server is currently under maintenance.", "<red>Please try again later.")

    fun load() {
        maintenanceMode = maintenanceConfig.boolean("status")
        kickMessage = maintenanceConfig.string("kick-message")

        pingVersion = maintenanceConfig.string("ping.version")
        pingMotd = maintenanceConfig.string("ping.motd")
        pingHover = maintenanceConfig.list("ping.hover")
    }

    fun reload() {
        save()
        load()
    }

    fun enable() {
        maintenanceMode = true

        plugin.proxy.allPlayers
            .filter { !it.hasPermission("extendedvelocity.maintenance.bypass") }
            .forEach { it.disconnect(kickMessage.miniMessage()) }
    }

    fun disable() {
        maintenanceMode = false
    }

    fun save() {
        maintenanceConfig.set("status", maintenanceMode)
        maintenanceConfig.save()
    }

    fun isEnabled(): Boolean {
        return maintenanceMode
    }
}