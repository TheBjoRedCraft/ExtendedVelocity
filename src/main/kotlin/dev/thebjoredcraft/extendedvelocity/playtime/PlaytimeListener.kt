package dev.thebjoredcraft.extendedvelocity.playtime

import com.github.shynixn.mccoroutine.velocity.launch

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent

import dev.thebjoredcraft.extendedvelocity.container

class PlaytimeListener {
    @Subscribe
    fun onServerConnect(event: PlayerChooseInitialServerEvent) {
        val player = event.player
        val uuid = player.uniqueId

        container.launch {
            if (PlaytimeService.UuidMechanics.isFirstSeen(uuid)) {
                PlaytimeService.UuidMechanics.updateFirstSeen(uuid, player.username)
            }

            PlaytimeService.UuidMechanics.updateLastSeen(uuid)
        }
    }
}