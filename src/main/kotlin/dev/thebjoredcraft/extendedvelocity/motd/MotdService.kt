package dev.thebjoredcraft.extendedvelocity.motd

import dev.thebjoredcraft.extendedvelocity.util.motdConfig

object MotdService {
    var motdEnabled: Boolean = true
    var motd: String = ""

    var versionEnabled: Boolean = true
    var version: String = "1.0.0"
    var versionID = 1

    var playerCountEnabled: Boolean = true
    var playerCountOnline: String = "%online%"
    var playerCountMax: String = "%online_max%"

    var samplePlayersEnabled: Boolean = true
    var samplePlayers: List<String> = listOf("Line1", "Line2", "Line3")


    fun load() {
        motdEnabled = motdConfig.boolean("motd.enabled")
        motd = motdConfig.string("motd.message")

        versionEnabled = motdConfig.boolean("version.enabled")
        versionID = motdConfig.int("version.version")
        version = motdConfig.string("version.message")

        playerCountEnabled = motdConfig.boolean("playerCount.enabled")
        playerCountOnline = motdConfig.string("playerCount.onlinePlayers")
        playerCountMax = motdConfig.string("playerCount.maxPlayers")

        samplePlayersEnabled = motdConfig.boolean("samplePlayers.enabled")
        samplePlayers = motdConfig.list("samplePlayers.players")
    }

    fun reload() {
        this.load()
    }
}