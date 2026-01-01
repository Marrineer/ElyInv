package com.marrineer.elyInv.managers

import com.marrineer.elyInv.ElyInv
import org.bukkit.configuration.file.FileConfiguration

@Suppress("unused")
class ConfigManager(
    private val plugin: ElyInv
) {

    private val config: FileConfiguration
        get() = plugin.config

    init {
        loadConfig()
    }

    fun loadConfig() {
        plugin.saveDefaultConfig()
        plugin.reloadConfig()
    }

    fun reloadConfig() {
        plugin.reloadConfig()
    }

    fun get(path: String): Any? =
        config.get(path, "Missing config: $path")

    fun getPrefix(): String =
        config.getString("global-prefix", "") ?: ""

    fun playerStoragePath(): String =
        config.getString("storage.path", "player.yml") ?: "player.yml"

    fun getMaxUsage(): Int = config.getInt("usage.max-per-player", 50)

    enum class FileType {
        CONFIG, MESSAGE
    }
}