package com.marrineer.elyInv

import com.marrineer.elyInv.commands.ElyBase
import com.marrineer.elyInv.managers.ConfigManager
import com.marrineer.elyInv.managers.MessageManager
import com.marrineer.elyInv.managers.PlayerManager
import com.marrineer.elyInv.utils.MessageUtils
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import net.milkbowl.vault.economy.Economy
import org.bukkit.plugin.java.JavaPlugin

class ElyInv : JavaPlugin() {
    lateinit var econ: Economy
    var hasPAPI: Boolean = false

    lateinit var adventure: BukkitAudiences
    lateinit var messageUtils: MessageUtils
    lateinit var configManager: ConfigManager
    lateinit var messageManager: MessageManager
    lateinit var playerManager: PlayerManager

    override fun onEnable() {
        if (!setupEconomy()) {
            logger.severe(String.format("[%s] - Disabled due to no Vault dependency found!", this.name));
            server.pluginManager.disablePlugin(this)
        }
        hasPAPI = (server.pluginManager.getPlugin("PlaceholderAPI") != null)

        this.adventure = BukkitAudiences.create(this)
        this.messageUtils = MessageUtils(
            this,
            hasPAPI,
            adventure,
            configManager.getPrefix()
        )
        this.configManager = ConfigManager(this)
        this.messageManager = MessageManager.apply {
            init(this@ElyInv)
        }
        playerManager =
            PlayerManager(configManager.playerStoragePath(), this).init()

        val command = server.getPluginCommand("elyinv")
        if (command == null) {
            logger.severe("Command 'elyinv' not found in plugin.yml")
            server.pluginManager.disablePlugin(this)
            return
        }
        command.setExecutor(ElyBase(messageUtils, this))
    }

    override fun onDisable() {
        if (::adventure.isInitialized) {
            adventure.close()
        }
        if (::playerManager.isInitialized) {
            playerManager.shutdown()
        }
    }

    private fun setupEconomy(): Boolean {
        server.pluginManager.getPlugin("Vault") ?: return false
        val rsp = server.servicesManager.getRegistration(Economy::class.java) ?: return false
        rsp.provider.also { provider ->
            econ = provider
        }
        return econ != null
    }
}
