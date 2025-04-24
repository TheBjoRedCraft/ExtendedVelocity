package dev.thebjoredcraft.extendedvelocity.util

import com.velocitypowered.api.command.CommandSource
import dev.thebjoredcraft.extendedvelocity.message.Colors
import dev.thebjoredcraft.extendedvelocity.message.MessageBuilder

fun CommandSource.sendText(message: MessageBuilder) {
    this.sendMessage(Colors.PREFIX.append { message.build() })
}

fun CommandSource.error(message: String) {
    this.sendMessage(Colors.PREFIX.append { MessageBuilder().error(message).build() })
}

fun CommandSource.sendRawText(message: MessageBuilder) {
    this.sendMessage(message.build())
}

fun CommandSource.sendText(message: String) {
    this.sendMessage(Colors.PREFIX.append { MessageBuilder().primary(message).build() })
}