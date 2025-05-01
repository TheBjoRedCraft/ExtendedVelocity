package dev.thebjoredcraft.extendedvelocity.util

import dev.thebjoredcraft.extendedvelocity.config.Config
import dev.thebjoredcraft.extendedvelocity.plugin

val maintenanceConfig: Config by lazy {
    plugin.maintenanceConfig ?: throw IllegalStateException("Config '${maintenanceConfig.fileName}' not initialized.")
}

val pluginConfig: Config by lazy {
    plugin.pluginConfig ?: throw IllegalStateException("Config '${pluginConfig.fileName}' not initialized.")
}

val brandConfig: Config by lazy {
    plugin.brandConfig ?: throw IllegalStateException("Config '${brandConfig.fileName}' not initialized.")
}