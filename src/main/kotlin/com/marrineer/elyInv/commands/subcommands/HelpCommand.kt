package com.marrineer.elyInv.commands.subcommands

import com.marrineer.elyInv.ElyInv
import com.marrineer.elyInv.commands.interfaces.SubCommand
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class HelpCommand(
    private val plugin: ElyInv
) : SubCommand {
    override val name: String = "help"
    override val permission: String = "elyinv.use"
    override val playerOnly: Boolean = false

    override fun execute(sender: CommandSender, args: List<String>) {
        val header = listOf(
            "<gradient:#b388ff:#5fd3ff><bold> ᴇʟʏɪɴᴠ ᴠ${plugin.description.version}</bold></gradient>",
            "<dark_gray>────────── <gradient:#b388ff:#5fd3ff>ᴇʟʏɪɴᴠ</gradient> ──────────</dark_gray>"
        ).joinToString("\n")

        val footer = listOf(
            "<dark_gray>─────────────────────────────</dark_gray>"
        ).joinToString("\n")

        val buy = listOf(
            "<aqua>▶</aqua> <yellow><bold>/elyinv buy <amount></bold></yellow>",
            "<dark_gray>  └─</dark_gray> <gray>Buy uses with your balance</gray>"
        ).joinToString("\n")

        val toggle = listOf(
            "<aqua>▶</aqua> <yellow><bold>/elyinv toggle</bold></yellow>",
            "<dark_gray>  └─</dark_gray> <gray>Toggle keepInventory</gray>"
        ).joinToString("\n")

        val help = listOf(
            "<aqua>▶</aqua> <yellow><bold>/elyinv help</bold></yellow>",
            "<dark_gray>  └─</dark_gray> <gray>Show this help menu</gray>"
        ).joinToString("\n")

        val set = listOf(
            "<aqua>▶</aqua> <yellow><bold>/elyinv set</bold></yellow>",
            "<dark_gray>  └─</dark_gray> <gray>Set usage of a player</gray>"
        ).joinToString("\n")

        val reload = listOf(
            "<aqua>▶</aqua> <yellow><bold>/elyinv reload</bold></yellow>",
            "<dark_gray>  └─</dark_gray> <gray>Reload configuration and data</gray>"
        ).joinToString("\n")
        val list: List<String> = listOf(
            header,
            buy,
            toggle,
            help,
            footer
        )
        val shouldAddSet = when (sender) {
            is Player -> sender.isOp
            else -> true
        }
        val result = if (shouldAddSet) {
            buildList {
                list.forEach { item ->
                    if(item == footer) {
                        add(set)
                        add(reload)
                    }
                    add(item)
                }
            }
        } else {
            list
        }

        result.forEach { plugin.messageUtils.sendToSender(sender, it) }
    }

    override fun tabComplete(
        sender: CommandSender,
        args: List<String>
    ): List<String> = emptyList()
}