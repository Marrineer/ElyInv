package com.marrineer.elyInv

import com.marrineer.elyInv.commands.ElyBase
import com.marrineer.elyInv.listeners.PlayerDeathListener
import com.marrineer.elyInv.managers.ConfigManager
import com.marrineer.elyInv.managers.MessageManager
import com.marrineer.elyInv.managers.PlayerManager
import com.marrineer.elyInv.placeholder.ElyPlaceholderExpansion
import com.marrineer.elyInv.utils.MessageUtils
import com.marrineer.elyInv.utils.SimpleLogger
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import net.milkbowl.vault.economy.Economy
import org.bukkit.plugin.java.JavaPlugin

class ElyInv : JavaPlugin() {
    lateinit var econ: Economy

    lateinit var configManager: ConfigManager
    lateinit var messageUtils: MessageUtils
    lateinit var messageManager: MessageManager
    lateinit var playerManager: PlayerManager
    lateinit var simpleLogger: SimpleLogger
    lateinit var adventure: BukkitAudiences

    override fun onEnable() {
        this.adventure = BukkitAudiences.create(this)

        if (!setupEconomy()) {
            logger.severe(String.format("[%s] - Disabled due to no Vault dependency found!", this.name))
            server.pluginManager.disablePlugin(this)
            return
        }

        val hasPAPI = (server.pluginManager.getPlugin("PlaceholderAPI") != null)

        if(hasPAPI && server.pluginManager.isPluginEnabled("PlaceholderAPI")) {
            ElyPlaceholderExpansion(this).register()
        }

        this.configManager = ConfigManager(this).apply {
            reload()
        }

        this.simpleLogger = SimpleLogger(this, adventure)

        this.messageUtils = MessageUtils(
            this,
            hasPAPI,
            adventure,
            configManager.getPrefix()
        )

        this.messageManager = MessageManager.init(this)

        this.playerManager = PlayerManager(configManager.playerStoragePath(), this).init()

        val command = server.getPluginCommand("elyinv")
        if (command == null) {
            logger.severe("Command 'elyinv' not found in plugin.yml")
            server.pluginManager.disablePlugin(this)
            return
        }

        command.setExecutor(ElyBase(messageUtils, this))

        server.pluginManager.registerEvents(PlayerDeathListener(this), this)

        simpleLogger.log(SimpleLogger.LogLevel.INFO, "Plugin enabled")

        val infoLines = listOf(
            "&fVersion: &7${description.version}",
            "&fAuthor: &7${description.authors.joinToString(", ")}",
            "&fStatus: &a✓ Enabled"
        )

        val depLines = listOf(
            "&fVault: ${if(!setupEconomy()) "&c✗ NOT FOUND" else "&a✓ ENABLED"}",
            "&fPlaceholderAPI: ${if(!hasPAPI) "&c✗ NOT FOUND" else "&a✓ ENABLED"}"
        )

        val contactLines = listOf(
            "&fDiscord: &7@marrineer",
            "&fEmail: &7qhuylovegd@gmail.com"
        )

        val log = buildList {
            addAll(simpleLogger.box("&d&lElyInv &7v${description.version}", infoLines))
            add("") // Dòng trống
            addAll(simpleLogger.box("&6Dependencies", depLines))
            add("")
            addAll(simpleLogger.box("&bContact", contactLines))
        }
        log.forEach(logger::info)
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
