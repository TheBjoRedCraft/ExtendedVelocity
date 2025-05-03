package dev.thebjoredcraft.extendedvelocity.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

fun String.miniMessage(): Component {
    return MiniMessage.miniMessage().deserialize(this)
}

fun String.legacy(): String {
    return LegacyComponentSerializer.legacySection().serialize(MiniMessage.miniMessage().deserialize(this))
}