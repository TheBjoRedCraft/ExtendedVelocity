package dev.thebjoredcraft.extendedvelocity.maintenance

import com.velocitypowered.api.event.ResultedEvent
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.LoginEvent
import com.velocitypowered.api.event.proxy.ProxyPingEvent
import com.velocitypowered.api.proxy.server.ServerPing
import dev.thebjoredcraft.extendedvelocity.util.miniMessage

class MaintenanceListener {
    @Subscribe
    fun onProxyPing(event: ProxyPingEvent) {
        if (!MaintenanceService.isEnabled()) {
            return
        }

        val builder = event.ping.asBuilder()

        builder.version(ServerPing.Version(1, MaintenanceService.pingVersion))
        builder.description(MaintenanceService.pingMotd.miniMessage())

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
}