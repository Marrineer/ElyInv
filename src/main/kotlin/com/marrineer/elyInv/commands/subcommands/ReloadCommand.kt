package com.marrineer.elyInv.commands.subcommands

import com.marrineer.elyInv.ElyInv
import com.marrineer.elyInv.commands.interfaces.SubCommand
import com.marrineer.elyInv.managers.ConfigManager
import org.bukkit.command.CommandSender

class ReloadCommand(
    private val plugin: ElyInv
) : SubCommand {
    override val name: String = "reload"
    override val permission: String = "elyinv.admin"
    override val playerOnly: Boolean = false

    override fun execute(sender: CommandSender, args: List<String>) {
        plugin.configManager.reloadConfig()
        plugin.messageManager.reload()
        plugin.playerManager.reloadAll()
        plugin.logger.severe(String.format("[%s] Config reloaded", plugin.name))
        plugin.messageUtils.sendWithPrefixToSender(
            sender,
            plugin.messageUtils.get(
                ConfigManager.FileType.MESSAGE,
                "commands.config-reloaded"
            )
        )
    }

    override fun tabComplete(
        sender: CommandSender,
        args: List<String>
    ): List<String> = emptyList()
}