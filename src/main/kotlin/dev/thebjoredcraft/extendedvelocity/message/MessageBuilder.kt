package dev.thebjoredcraft.extendedvelocity.message

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.minimessage.MiniMessage

class MessageBuilder {
    private var message: Component = Component.empty()

    fun withPrefix(): MessageBuilder {
        message = message.append(Colors.PREFIX)
        return this
    }

    fun hover(message: MessageBuilder): MessageBuilder {
        this.message = this.message.hoverEvent(HoverEvent.showText(message.build()))
        return this
    }

    fun success(text: String): MessageBuilder {
        message = message.append(Component.text(text, Colors.SUCCESS))
        return this
    }

    fun error(text: String): MessageBuilder {
        message = message.append(Component.text(text, Colors.ERROR))
        return this
    }

    fun modernGreen(text: String): MessageBuilder {
        message = message.append(Component.text(text, Colors.MODERN_GREEN))
        return this
    }

    fun prefixColor(text: String): MessageBuilder {
        message = message.append(Component.text(text, Colors.PREFIX_COLOR))
        return this
    }

    fun darkSpacer(text: String): MessageBuilder {
        message = message.append(Component.text(text, Colors.DARK_SPACER))
        return this
    }

    fun spacer(text: String): MessageBuilder {
        message = message.append(Component.text(text, Colors.SPACER))
        return this
    }

    fun miniMessage(text: String): MessageBuilder {
        message = message.append(MiniMessage.miniMessage().deserialize(text))
        return this
    }

    fun component(component: Component): MessageBuilder {
        message = message.append(component)
        return this
    }

    fun command(text: MessageBuilder, hover: MessageBuilder, command: String): MessageBuilder {
        message = message.append(text.build().clickEvent(ClickEvent.runCommand(command)).hoverEvent(HoverEvent.showText(hover.build())))
        return this
    }

    fun suggest(text: MessageBuilder, hover: MessageBuilder, command: String): MessageBuilder {
        message = message.append(text.build().clickEvent(ClickEvent.suggestCommand(command)).hoverEvent(HoverEvent.showText(hover.build())))
        return this
    }

    fun white(text: String): MessageBuilder {
        message = message.append(Component.text(text, Colors.WHITE))
        return this
    }

    fun newLine(): MessageBuilder {
        message = message.append(Component.newline())
        return this
    }

    fun build(): Component {
        return message
    }
}