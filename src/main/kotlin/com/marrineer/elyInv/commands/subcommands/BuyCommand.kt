package com.marrineer.elyInv.commands.subcommands

import com.marrineer.elyInv.ElyInv
import com.marrineer.elyInv.commands.interfaces.SubCommand
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class BuyCommand(
    private val plugin: ElyInv
) : SubCommand {
    override val name: String = "buy"
    override val permission: String = "elyinv.use"
    override val playerOnly: Boolean = true
    private val maxusage = plugin.configManager.getMaxUsage()
    private val price = plugin.configManager.getPrice()

    override fun execute(sender: CommandSender, args: List<String>) {
        val player = sender as? Player ?: return
        val uuid = player.uniqueId

        val currentUsage = plugin.playerManager.getCount(uuid)

        if (currentUsage >= maxusage) {
            plugin.messageUtils.sendWithPrefixToSender(
                sender,
                plugin.messageUtils.get("commands.elyinv.buy.max-usage")
            )
            return
        }

        val requested: Int = if (args.isEmpty()) {
            1
        } else {
            val raw = args[0]

            if (!raw.all { it.isDigit() }) {
                plugin.messageUtils.sendWithPrefixToSender(
                    sender,
                    plugin.messageUtils.get("commands.elyinv.buy.invalid-amount")
                )
                return
            }

            val value = raw.toInt()
            if (value <= 0) {
                plugin.messageUtils.sendWithPrefixToSender(
                    sender,
                    plugin.messageUtils.get("commands.elyinv.buy.amount-must-positive")
                )
                return
            }
            value
        }

        val remaining = maxusage - currentUsage
        val actualBuy = minOf(requested, remaining)

        val balance = plugin.econ.getBalance(player)
        val totalPrice = actualBuy * price
        if (balance < totalPrice) {
            plugin.messageUtils.sendWithPrefixToSender(
                sender,
                plugin.messageUtils.get("commands.elyinv.buy.not-enough-money")
            )
            return
        }
        plugin.econ.withdrawPlayer(player, totalPrice)
        plugin.playerManager.addCount(uuid, actualBuy)

        plugin.messageUtils.sendWithPrefixToSender(
            sender,
            plugin.messageUtils.get("commands.elyinv.buy.success-with-price")
        )
    }

    override fun tabComplete(
        sender: CommandSender,
        args: List<String>
    ): List<String> {

        if (sender !is Player) return emptyList()

        if (args.size != 1) return emptyList()

        val uuid = sender.uniqueId
        val currentUsage = plugin.playerManager.getCount(uuid)
        val remaining = maxusage - currentUsage

        if (remaining <= 0) return emptyList()

        val suggestions = listOf(1, 5, 10, 25, 30, 40, 50)
            .filter { it <= remaining }
            .map { it.toString() }

        val input = args[0]

        return suggestions.filter {
            it.startsWith(input)
        }
    }
}