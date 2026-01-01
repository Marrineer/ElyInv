package com.marrineer.elyInv.managers

import com.marrineer.elyInv.ElyInv
import com.marrineer.elyInv.model.PlayerProfile
import com.marrineer.elyInv.utils.SimpleLogger
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Suppress("unused")
class PlayerManager(
    val filePath: String,
    val plugin: ElyInv
) {
    lateinit var file: File
    lateinit var playerFile: FileConfiguration
    val playerProfile: MutableMap<UUID, PlayerProfile> = ConcurrentHashMap()

    fun init(): PlayerManager {
        this.file = File(plugin.dataFolder, filePath)
        if (!file.exists()) {
            plugin.saveResource(filePath, false)
        }
        reload()
        return this@PlayerManager
    }

    fun reload() {
        val startTime = System.currentTimeMillis()
        playerFile = YamlConfiguration.loadConfiguration(file)
        clearAllProfiles()

        playerFile.getKeys(false).forEach { key ->
            val uuid = runCatching { UUID.fromString(key) }.getOrNull() ?: return@forEach

            val section = playerFile.getConfigurationSection(key) ?: return@forEach
            val counts = section.getInt("counts", 0)
            val toggle = section.getBoolean("toggle", false)

            playerProfile[uuid] = PlayerProfile(counts, toggle)
        }

        val duration = System.currentTimeMillis() - startTime
        plugin.simpleLogger.log(
            SimpleLogger.LogLevel.INFO,
            "Loaded ${playerProfile.size} profiles in ${duration}ms"
        )
    }

    fun reloadAll() {
        saveAll()
        reload()
    }

    fun saveAll() {
        playerProfile.forEach { (uuid, profile) ->
            val section = playerFile.getConfigurationSection(uuid.toString())
                ?: playerFile.createSection(uuid.toString())

            section.set("counts", profile.count)
            section.set("toggle", profile.toggle)
        }

        try {
            playerFile.save(file)
        } catch (ex: Exception) {
            plugin.simpleLogger.log(
                SimpleLogger.LogLevel.ERROR,
                "Failed to save player data: ${ex.message}"
            )
        }
    }

    fun shutdown() {
        saveAll()
        playerProfile.clear()
    }


    fun getOrCreateProfile(player: UUID): PlayerProfile {
        return playerProfile.getOrPut(player) { PlayerProfile(0) }
    }

    fun getProfile(player: UUID): PlayerProfile? = playerProfile[player]

    fun getCount(player: UUID): Int = getProfile(player)?.count ?: 0

    fun setCount(player: UUID, count: Int) {
        playerProfile[player] = PlayerProfile(count)
    }

    fun addCount(player: UUID, amount: Int) {
        val currentCount = getCount(player)
        setCount(player, currentCount + amount)
    }

    fun subtractCount(player: UUID, amount: Int): Boolean {
        val current = getProfile(player)
        return if ((current?.count ?: 0) >= amount) {
            val newCount = (current?.count ?: 0) - amount
            playerProfile[player] = current?.copy(count = newCount) ?: PlayerProfile(newCount)
            true
        } else {
            false
        }
    }

    fun removeProfile(player: UUID) = playerProfile.remove(player)

    fun clearAllProfiles() = playerProfile.clear()

    fun onUse(player: UUID): Boolean = subtractCount(player, 1)

    fun hasCount(player: UUID, minCount: Int = 1): Boolean {
        return getCount(player) >= minCount
    }

    fun resetCount(player: UUID) {
        setCount(player, 0)
    }

    fun getToggle(player: UUID): Boolean = getProfile(player)?.toggle ?: false

    fun setToggle(player: UUID, toggle: Boolean) {
        val current = getProfile(player)
        playerProfile[player] = current?.copy(toggle = toggle) ?: PlayerProfile(0, toggle)
    }
}