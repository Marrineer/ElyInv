package com.marrineer.elyInv.commands

import com.marrineer.elyInv.ElyInv
import com.marrineer.elyInv.commands.interfaces.SubCommand
import com.marrineer.elyInv.commands.subcommands.BuyCommand
import com.marrineer.elyInv.commands.subcommands.HelpCommand
import com.marrineer.elyInv.commands.subcommands.ReloadCommand
import com.marrineer.elyInv.commands.subcommands.SetCommand
import com.marrineer.elyInv.commands.subcommands.ToggleCommand
import com.marrineer.elyInv.utils.MessageUtils
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class ElyBase(
    var messageUtils: MessageUtils,
    var plugin: ElyInv
) : CommandExecutor, TabCompleter {
    private val subCommand = mutableMapOf<String, SubCommand>()

    init {
        register(ReloadCommand(plugin))
        register(ToggleCommand(plugin))
        register(BuyCommand(plugin))
        register(SetCommand(plugin))
        register(HelpCommand(plugin))
    }

    override fun onCommand(
        sender: CommandSender,
        cmd: Command,
        s: String,
        args: Array<out String>
    ): Boolean {
        if (args.isEmpty()) {
            messageUtils.sendWithPrefixToSender(
                sender,
                messageUtils.get(
                    "commands.no-subcommand"
                )
            )
            return true
        }
        val sub = subCommand[args[0].lowercase()]
            ?: run {
                messageUtils.sendWithPrefixToSender(
                    sender,
                    messageUtils.get(
                        "commands.invalid-command"
                    )
                )
                return true
            }
        if (!sender.hasPermission(sub.permission)) {
            messageUtils.sendWithPrefixToSender(
                sender,
                messageUtils.get(
                    "commands.no-permission"
                )
            )
            return true
        }
        if (sub.playerOnly && sender !is Player) {
            messageUtils.sendWithPrefixToSender(
                sender,
                messageUtils.get(
                    "commands.player-only"
                )
            )
            return true
        }
        sub.execute(sender, args.drop(1))
        return true
    }

    private fun register(cmd: SubCommand) {
        subCommand[cmd.name.lowercase()] = cmd
    }

    override fun onTabComplete(
        sender: CommandSender,
        cmd: Command,
        s: String,
        args: Array<out String>
    ): List<String> {

        if (args.size == 1) {
            return subCommand.values
                .filter { sub ->
                    (!sub.playerOnly || sender is Player) &&
                            sender.hasPermission(sub.permission)
                }
                .map { it.name }
                .filter { it.startsWith(args[0], ignoreCase = true) }
        }

        val sub = subCommand[args[0].lowercase()] ?: return emptyList()

        if (sub.playerOnly && sender !is Player) return emptyList()
        if (!sender.hasPermission(sub.permission)) return emptyList()

        return sub.tabComplete(sender, args.drop(1))
    }

}