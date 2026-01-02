package com.marrineer.elyInv.managers

import com.marrineer.elyInv.ElyInv
import com.marrineer.elyInv.models.ConfigData
import com.marrineer.elyInv.models.Default
import com.marrineer.elyInv.models.data.UsageData

@Suppress("unused")
class ConfigManager(
    private val plugin: ElyInv
) {

    @Volatile
    lateinit var config: ConfigData
        private set

    init {
        reload()
    }

    private fun load(): ConfigData {
        val cfg = plugin.config
        return ConfigData(
            prefix = cfg.getString(
                "global-prefix"
            )
                ?: Default.PREFIX,
            storage = cfg.getString(
                "storage.path"
            )
                ?: Default.STORAGE_PATH,
            usageData = UsageData(
                price = cfg.getDouble(
                    "usage.price",
                    Default.PRICE
                ),
                perPlayer = cfg.getInt(
                    "usage.max-per-player",
                    Default.MAX_PER_PLAYER
                )
            )
        )
    }

    fun reload() {
        config = load()
    }

    fun getPrefix(): String = config.prefix

    fun playerStoragePath(): String = config.storage

    fun getMaxUsage(): Int = config.usageData.perPlayer

    fun getPrice(): Double = config.usageData.price
}