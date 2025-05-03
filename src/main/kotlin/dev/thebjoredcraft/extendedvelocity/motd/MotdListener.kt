package dev.thebjoredcraft.extendedvelocity.motd

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyPingEvent
import com.velocitypowered.api.proxy.server.ServerPing
import dev.thebjoredcraft.extendedvelocity.maintenance.MaintenanceService
import dev.thebjoredcraft.extendedvelocity.util.miniMessage

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

        event.ping = builder.build()
    }
}