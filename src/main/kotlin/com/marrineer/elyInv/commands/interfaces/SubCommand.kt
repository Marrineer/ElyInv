package com.marrineer.elyInv.commands.interfaces

import org.bukkit.command.CommandSender

interface SubCommand {
    val name: String
    val permission: String
    val playerOnly: Boolean

    fun execute(
        sender: CommandSender,
        args: List<String>
    )

    fun tabComplete(
        sender: CommandSender,
        args: List<String>
    ): List<String>
}