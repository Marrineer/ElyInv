package com.marrineer.elyInv.commands.subcommands

import com.marrineer.elyInv.ElyInv
import com.marrineer.elyInv.commands.interfaces.SubCommand
import com.marrineer.elyInv.managers.ConfigManager
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ToggleCommand(
    var plugin: ElyInv
) : SubCommand {
    override val name: String = "toggle"
    override val permission: String = "elyinv.use"
    override val playerOnly: Boolean = true

    fun Player.toggleKeep(): Boolean {
        val uuid = uniqueId
        val newState = !plugin.playerManager.getToggle(uuid)
        plugin.playerManager.setToggle(
            uuid,
            newState
        )
        return newState
    }

    override fun execute(sender: CommandSender, args: List<String>) {
        val player = sender as? Player ?: return
        val status = if (player.toggleKeep()) "enabled" else "disabled"

        plugin.messageUtils.sendWithPrefixToSender(
            sender,
            plugin.messageUtils.get(
                ConfigManager.FileType.MESSAGE,
                "commands.toggle-keep.$status"
            )
        )
    }

    override fun tabComplete(
        sender: CommandSender,
        args: List<String>
    ): List<String> = emptyList()
}