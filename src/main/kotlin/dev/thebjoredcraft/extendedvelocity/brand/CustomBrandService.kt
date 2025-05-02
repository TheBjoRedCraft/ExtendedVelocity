package dev.thebjoredcraft.extendedvelocity.brand

import com.velocitypowered.api.network.ProtocolState
import com.velocitypowered.api.network.ProtocolVersion
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier
import com.velocitypowered.api.scheduler.ScheduledTask
import com.velocitypowered.api.scheduler.TaskStatus
import com.velocitypowered.proxy.protocol.ProtocolUtils
import dev.thebjoredcraft.extendedvelocity.plugin
import dev.thebjoredcraft.extendedvelocity.util.brandConfig
import dev.thebjoredcraft.extendedvelocity.util.miniMessage
import io.netty.buffer.Unpooled
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit

object CustomBrandService {
    private var task: ScheduledTask? = null

    var customBrandEnabled: Boolean = false
    var customBrand: String = ""
    var legacyBrand: String = ""

    fun load() {
        customBrandEnabled = brandConfig.boolean("enabled")
        customBrand = brandConfig.string("server-brand")

        legacyBrand = LegacyComponentSerializer.legacySection().serialize(customBrand.miniMessage())
    }

    fun start() {
        if (!this.customBrandEnabled) {
            return
        }

        task = plugin.proxy.scheduler
            .buildTask(plugin, Runnable {
                plugin.proxy.allPlayers
                    .parallelStream()
                    .forEach {
                        if (it.protocolState != ProtocolState.PLAY) {
                            return@forEach;
                        }

                        val buf = Unpooled.buffer();

                        if (it.protocolVersion >= ProtocolVersion.MINECRAFT_1_8) {
                            ProtocolUtils.writeString(buf, legacyBrand)

                            it.sendPluginMessage(MinecraftChannelIdentifier.forDefaultNamespace("brand"), buf.array());
                        }
                    }
                }).repeat(1L, TimeUnit.SECONDS).schedule()
    }

    fun isRunning(): Boolean {
        val t = task ?: return false
        return t.status() != TaskStatus.CANCELLED
    }

    fun restart() {
        if (this.isRunning()) {
            this.stop()
        }
        this.start()
    }

    fun reload() {
        this.stop()
        this.load()
        this.start()
    }

    fun stop() {
        task?.cancel()
    }
}
