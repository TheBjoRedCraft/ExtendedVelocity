package dev.thebjoredcraft.extendedvelocity.brand

import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier
import com.velocitypowered.api.scheduler.ScheduledTask
import com.velocitypowered.api.scheduler.TaskStatus
import dev.thebjoredcraft.extendedvelocity.plugin
import dev.thebjoredcraft.extendedvelocity.util.brandConfig
import dev.thebjoredcraft.extendedvelocity.util.miniMessage
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
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
        if(!this.customBrandEnabled) {
            return
        }

        task = plugin.proxy.scheduler
            .buildTask(plugin, Runnable {
                plugin.proxy.allPlayers.forEach { it.sendPluginMessage(MinecraftChannelIdentifier.from("brand"), legacyBrand.toByteArray(Charsets.UTF_8)) }
            })
            .repeat(0L, TimeUnit.MINUTES)
            .schedule()
    }

    fun isRunning(): Boolean {
        if(task == null) {
            return false
        }

        return task!!.status() != TaskStatus.CANCELLED
    }

    fun restart() {
        if(this.isRunning()) {
            this.stop()
        }

        this.start()
    }

    fun stop() {
        task?.cancel()
    }
}