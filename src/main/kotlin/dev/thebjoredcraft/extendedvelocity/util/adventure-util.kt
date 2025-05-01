package dev.thebjoredcraft.extendedvelocity.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage

fun String.miniMessage(): Component {
    return MiniMessage.miniMessage().deserialize(this)
}