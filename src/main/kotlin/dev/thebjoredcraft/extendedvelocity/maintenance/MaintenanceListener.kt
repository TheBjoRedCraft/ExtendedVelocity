package dev.thebjoredcraft.extendedvelocity.maintenance

import com.velocitypowered.api.event.ResultedEvent
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.LoginEvent
import com.velocitypowered.api.event.proxy.ProxyPingEvent
import com.velocitypowered.api.proxy.server.ServerPing
import dev.thebjoredcraft.extendedvelocity.plugin
import dev.thebjoredcraft.extendedvelocity.util.legacy
import dev.thebjoredcraft.extendedvelocity.util.miniMessage
import net.objecthunter.exp4j.ExpressionBuilder
import java.util.UUID

class MaintenanceListener {
    @Subscribe
    fun onProxyPing(event: ProxyPingEvent) {
        if (MaintenanceService.isEnabled()) {
            return
        }

        val builder = event.ping.asBuilder()

        if(MaintenanceService.motdEnabled) {
            builder.description(MaintenanceService.motd.miniMessage())
        }

        if(MaintenanceService.versionEnabled) {
            builder.version(ServerPing.Version(MaintenanceService.versionID, MaintenanceService.version))
        }

        if(MaintenanceService.playerCountEnabled) {
            builder.onlinePlayers(MaintenanceService.playerCountOnline.calculatePlayers())
            builder.maximumPlayers(MaintenanceService.playerCountMax.calculatePlayers())
        }

        if(MaintenanceService.samplePlayersEnabled) {
            MaintenanceService.samplePlayers.forEach { builder.samplePlayers(ServerPing.SamplePlayer(it.legacy(), UUID.randomUUID())) }
        }

        event.ping = builder.build()
    }

    @Subscribe
    fun onConnect(event: LoginEvent) {
        if(!MaintenanceService.isEnabled()) {
            return
        }

        if(event.player.hasPermission("extendedvelocity.maintenance.bypass")) {
            return
        }

        event.result = ResultedEvent.ComponentResult.denied(MaintenanceService.kickMessage.miniMessage())
    }

    fun String.calculatePlayers(): Int {
        val processed = this
            .replace("%online%", plugin.proxy.allPlayers.size.toString())
            .replace("%online_max%", plugin.proxy.configuration.showMaxPlayers.toString())

        val result = ExpressionBuilder(processed).build().evaluate()
        return result.toInt()
    }
}