package dev.thebjoredcraft.extendedvelocity.config

import dev.thebjoredcraft.extendedvelocity.plugin
import org.spongepowered.configurate.CommentedConfigurationNode
import org.spongepowered.configurate.yaml.YamlConfigurationLoader
import java.nio.file.Files
import java.nio.file.Path

object ConfigProvider {
    lateinit var configPath: Path
    lateinit var config: CommentedConfigurationNode

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
    }
}

val config = ConfigProvider.config

fun CommentedConfigurationNode.configValue(path: String): String {
    return ConfigProvider.config.node(path).string ?: ""
}