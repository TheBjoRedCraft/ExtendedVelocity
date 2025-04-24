package dev.thebjoredcraft.extendedvelocity.message

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor

object Colors {
    val PRIMARY: TextColor = TextColor.color(0x3b92d1)
    val SUCCESS: TextColor = TextColor.color(0x65ff64)
    val ERROR: TextColor = TextColor.color(0xee3d51)
    val SPACER: NamedTextColor = NamedTextColor.GRAY
    val DARK_SPACER: NamedTextColor = NamedTextColor.DARK_GRAY
    val PREFIX_COLOR: TextColor = PRIMARY

    val MODERN_GREEN: TextColor = TextColor.color(0x55FF55)

    val PREFIX: Component = Component.text("» ", DARK_SPACER)
    val WHITE: NamedTextColor = NamedTextColor.WHITE
}