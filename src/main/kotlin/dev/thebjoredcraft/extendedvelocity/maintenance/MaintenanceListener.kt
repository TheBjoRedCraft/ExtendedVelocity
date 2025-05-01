package dev.thebjoredcraft.extendedvelocity.maintenance

import com.google.common.eventbus.Subscribe
import com.velocitypowered.api.event.connection.PreLoginEvent

import com.velocitypowered.api.event.proxy.ProxyPingEvent
import com.velocitypowered.api.proxy.server.ServerPing

import dev.thebjoredcraft.extendedvelocity.util.miniMessage
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

import java.util.UUID

class MaintenanceListener {
    @Subscribe
    fun onProxyPing(event: ProxyPingEvent) {
        if(!MaintenanceService.isEnabled()) {
            return
        }

        event.ping = event.ping.asBuilder()
            .version(ServerPing.Version(
                1, MaintenanceService.pingVersion
            ))
            .clearSamplePlayers()
            .samplePlayers(MaintenanceService.pingHover.map {
                ServerPing.SamplePlayer(
                    LegacyComponentSerializer.legacySection().serialize(it.miniMessage()),
                    UUID.randomUUID()
                )
            })
            .description(MaintenanceService.pingMotd.miniMessage())
            .build()
    }

    @Subscribe
    fun onConnect(event: PreLoginEvent) {
        if(!MaintenanceService.isEnabled()) {
            return
        }

        event.result = PreLoginEvent.PreLoginComponentResult.denied(MaintenanceService.kickMessage.miniMessage())
    }
}