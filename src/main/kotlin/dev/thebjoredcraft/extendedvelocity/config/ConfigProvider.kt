package dev.thebjoredcraft.extendedvelocity.config

import dev.thebjoredcraft.extendedvelocity.plugin
import org.spongepowered.configurate.CommentedConfigurationNode
import org.spongepowered.configurate.yaml.YamlConfigurationLoader
import java.nio.file.Files
import java.nio.file.Path

object ConfigProvider {
    var configPath: Path? = null
    var config: CommentedConfigurationNode? = null
    var configuration: Configuration? = null

    fun load() {
        if(Files.notExists(plugin.dataFolder)) {
            Files.createDirectories(plugin.dataFolder)
        }

        configPath = plugin.dataFolder.resolve("config.yml")

        if(Files.notExists(configPath)) {
            this.javaClass.getClassLoader().getResourceAsStream("config.yml").use {
                Files.copy(it, configPath)
            }
        }

        val loader = YamlConfigurationLoader.builder().path(configPath).build()
        config = loader.load()

        configuration = Configuration()
    }

    fun reload() {
        this.load()
    }
}

val config = ConfigProvider.configuration ?: throw IllegalStateException("Config not loaded. Please call ConfigProvider.load() before using this.")

fun configValue(path: String): String? {
    return ConfigProvider.config?.node(path)?.string
}