package dev.thebjoredcraft.extendedvelocity

import com.google.inject.Inject
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.proxy.ProxyServer

@Plugin(
    id = "extendedvelocity",
    name = "ExtendedVelocity",
    version = "1.0.0-SNAPSHOT",
    description = "Do more with your proxy.",
    authors = ["TheBjoRedCraft"]
)
class ExtendedVelocity {
    @Inject
    lateinit var proxy: ProxyServer
}