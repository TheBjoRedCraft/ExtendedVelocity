package dev.thebjoredcraft.extendedvelocity.config

import dev.thebjoredcraft.extendedvelocity.message.Colors
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage

class Configuration() {
    val prefix: Component = MiniMessage.miniMessage().deserialize(configValue("messages.prefix") ?: MiniMessage.miniMessage().serialize(Colors.PREFIX))

    val maintenanceLine1: Component = MiniMessage.miniMessage().deserialize(configValue("messages.maintenance.line1") ?: "Maintenance")
    val maintenanceLine2: Component = MiniMessage.miniMessage().deserialize(configValue("messages.maintenance.line2") ?: "Server is in maintenance mode")
    val maintenanceKick: Component = MiniMessage.miniMessage().deserialize(configValue("messages.maintenance.kick") ?: "Please try again later")
}