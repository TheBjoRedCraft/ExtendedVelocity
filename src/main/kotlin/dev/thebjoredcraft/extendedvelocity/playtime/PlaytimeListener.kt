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
            println("Player ${player.username} connected a server")

            if (PlaytimeService.UuidMechanics.isFirstSeen(uuid)) {
                println("Player ${player.username} is first seen.")
                PlaytimeService.UuidMechanics.updateFirstSeen(uuid)
            }

            PlaytimeService.UuidMechanics.updateLastSeen(uuid)

            println("Player ${player.username} last seen updated.")
        }
    }
}