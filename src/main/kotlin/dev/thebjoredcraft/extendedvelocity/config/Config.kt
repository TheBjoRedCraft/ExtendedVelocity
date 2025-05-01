package dev.thebjoredcraft.extendedvelocity.config

import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.CommentedConfigurationNode
import org.spongepowered.configurate.loader.ConfigurationLoader
import org.spongepowered.configurate.yaml.YamlConfigurationLoader

import java.io.File
import java.nio.file.Files
import java.nio.file.Path

class Config(private val folder: Path, val fileName: String) {
    private val configFile: File = folder.resolve("$fileName.yml").toFile()
    private val loader: ConfigurationLoader<CommentedConfigurationNode> = YamlConfigurationLoader.builder()
        .path(configFile.toPath())
        .build()
    private lateinit var rootNode: CommentedConfigurationNode

    fun load() {
        if (!configFile.exists()) {
            generateDefaultConfig()
        }
        rootNode = loader.load()
    }

    fun save() {
        loader.save(rootNode)
    }

    fun set(path: String, value: Any) {
        val node = getNodeFromPath(path)
        node.set(value)
    }

    fun get(path: String): Any? {
        return getNodeFromPath(path).raw()
    }

    fun int(path: String): Int {
        return this.getNodeFromPath(path).int
    }

    fun list(path: String): List<String> {
        return this.getNodeFromPath(path).getList(String::class.java) ?: emptyList()
    }

    fun boolean(path: String): Boolean {
        return this.getNodeFromPath(path).boolean
    }

    fun string(path: String): String {
        return this.getNodeFromPath(path).string ?: ""
    }

    fun generateDefaultConfig() {
        val resource = javaClass.getResourceAsStream("/$fileName.yml") ?: throw IllegalStateException("Default $fileName.yml not found in resources!")

        Files.createDirectories(folder)
        resource.use { input ->
            configFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
    }

    private fun getNodeFromPath(path: String): ConfigurationNode {
        if (!::rootNode.isInitialized) throw IllegalStateException("Config '$fileName' not loaded. Please call Config#load() before using this")
        return path.split('.').fold(rootNode) { node, key -> node.node(key) }
    }
}
