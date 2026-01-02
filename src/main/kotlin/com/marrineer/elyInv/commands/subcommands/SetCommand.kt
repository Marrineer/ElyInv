package com.marrineer.elyInv.commands.subcommands

import com.marrineer.elyInv.ElyInv
import com.marrineer.elyInv.commands.interfaces.SubCommand
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

class SetCommand(
    private val plugin: ElyInv
) : SubCommand {
    override val name: String = "set"
    override val permission: String = "elyinv.admin"
    override val playerOnly: Boolean = false

    override fun execute(sender: CommandSender, args: List<String>) {
        if (args.size < 2) {
            plugin.messageUtils.sendWithPrefixToSender(
                sender,
                plugin.messageUtils.get("commands.elyinv.set.usage")
            )
            return
        }

        val targetName = args[0]
        val rawAmount = args[1]

        if (!rawAmount.all { it.isDigit() }) {
            plugin.messageUtils.sendWithPrefixToSender(
                sender,
                plugin.messageUtils.get("commands.elyinv.set.invalid-amount")
            )
            return
        }

        val amount = rawAmount.toInt()
        if (amount <= 0) {
            plugin.messageUtils.sendWithPrefixToSender(
                sender,
                plugin.messageUtils.get("commands.elyinv.set.amount-must-positive")
            )
            return
        }
        val targetUuid = Bukkit.getOfflinePlayer(targetName).uniqueId

        plugin.playerManager.setCount(targetUuid, amount)
        plugin.messageUtils.sendWithPrefixToSender(
            sender,
            plugin.messageUtils.get("commands.elyinv.set.success")
        )
    }

    override fun tabComplete(
        sender: CommandSender,
        args: List<String>
    ): List<String> {

        return when (args.size) {

            1 -> Bukkit.getOnlinePlayers()
                .map { it.name }
                .filter { it.startsWith(args[0], true) }

            2 -> listOf("1", "5", "10", "25", "50", "100")
                .filter { it.startsWith(args[1]) }

            else -> emptyList()
        }
    }
}