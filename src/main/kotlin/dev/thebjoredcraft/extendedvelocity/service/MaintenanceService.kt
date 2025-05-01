package dev.thebjoredcraft.extendedvelocity.service

import dev.thebjoredcraft.extendedvelocity.util.maintenanceConfig

object MaintenanceService {
    private var maintenanceMode: Boolean = false
    private var maintenanceKickMessage: String = "<red>The server is currently under maintenance. Please try again later."

    fun load() {
        maintenanceMode = maintenanceConfig.boolean("status")
        maintenanceKickMessage = maintenanceConfig.string("kick-message")
    }

    fun save() {
        maintenanceConfig.set("status", maintenanceMode)
        maintenanceConfig.save()
    }
}