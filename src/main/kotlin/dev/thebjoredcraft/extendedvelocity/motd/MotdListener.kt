package dev.thebjoredcraft.extendedvelocity.motd

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyPingEvent
import com.velocitypowered.api.proxy.server.ServerPing
import dev.thebjoredcraft.extendedvelocity.maintenance.MaintenanceService
import dev.thebjoredcraft.extendedvelocity.plugin
import dev.thebjoredcraft.extendedvelocity.util.miniMessage
import net.objecthunter.exp4j.ExpressionBuilder

class MotdListener {
    @Subscribe
    fun onProxyPing(event: ProxyPingEvent) {
        if (MaintenanceService.isEnabled()) {
            return
        }

        val builder = event.ping.asBuilder()

        if(MotdService.motdEnabled) {
            builder.description(MotdService.motd.miniMessage())
        }

        if(MotdService.versionEnabled) {
            builder.version(ServerPing.Version(MotdService.versionID, MotdService.version))
        }

        if(MotdService.playerCountEnabled) {
            builder.onlinePlayers(MotdService.playerCountOnline.calculatePlayers())
            builder.maximumPlayers(MotdService.playerCountMax.calculatePlayers())
        }

        event.ping = builder.build()
    }

    fun String.calculatePlayers(): Int {
        val processed = this
            .replace("%online%", plugin.proxy.allPlayers.size.toString())
            .replace("%online_max%", plugin.proxy.configuration.showMaxPlayers.toString())

        val result = ExpressionBuilder(processed).build().evaluate()
        return result.toInt()
    }
}