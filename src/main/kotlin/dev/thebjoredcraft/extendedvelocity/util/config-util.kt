package dev.thebjoredcraft.extendedvelocity.util

import dev.thebjoredcraft.extendedvelocity.config.Config
import dev.thebjoredcraft.extendedvelocity.plugin

val maintenanceConfig: Config
    get() = plugin.maintenanceConfig ?: throw IllegalStateException("Maintenance config not initialized")

val pluginConfig: Config
    get() = plugin.pluginConfig ?: throw IllegalStateException("Plugin config not initialized")

val brandConfig: Config
    get() = plugin.brandConfig ?: throw IllegalStateException("Brand config not initialized")

val motdConfig: Config
    get() = plugin.motdConfig ?: throw IllegalStateException("Motd config not initialized")
