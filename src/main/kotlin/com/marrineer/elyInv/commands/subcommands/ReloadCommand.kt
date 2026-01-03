package com.marrineer.elyInv.commands.subcommands

import com.marrineer.elyInv.ElyInv
import com.marrineer.elyInv.commands.interfaces.SubCommand
import com.marrineer.elyInv.utils.SimpleLogger
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender

class ReloadCommand(
    private val plugin: ElyInv
) : SubCommand {
    override val name: String = "reload"
    override val permission: String = "elyinv.admin"
    override val playerOnly: Boolean = false

    override fun execute(sender: CommandSender, args: List<String>) {
        plugin.configManager.reload()
        plugin.messageManager.reload()
        plugin.playerManager.reloadAll()
        plugin.simpleLogger.log(
            SimpleLogger.LogLevel.INFO,
            "Config reloaded"
        )
        if (sender !is ConsoleCommandSender) {
            plugin.messageUtils.sendWithPrefixToSender(
                sender,
                plugin.messageUtils.get("commands.config-reloaded")
            )
        }
    }

    override fun tabComplete(
        sender: CommandSender,
        args: List<String>
    ): List<String> = emptyList()
}