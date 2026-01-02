package com.marrineer.elyInv.utils

import com.marrineer.elyInv.ElyInv
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import net.kyori.adventure.text.minimessage.MiniMessage
import net.md_5.bungee.api.ChatColor

class SimpleLogger(
    plugin: ElyInv,
    private val audience: BukkitAudiences
) {
    private val prefix = plugin.name
    private val miniMessage = MiniMessage.miniMessage()

    fun log(level: LogLevel, message: String) {
        audience.console().sendMessage(
            miniMessage.deserialize(
                "<${level.color}>[$prefix]</${level.color}> <reset>$message"
            )
        )
    }

    fun box(title: String, lines: List<String>): List<String> {
        fun stripColor(text: String): String {
            return ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', text)) ?: text
        }

        val strippedLines = lines.map { stripColor(it) }
        val strippedTitle = stripColor(title)
        val width = maxOf(strippedTitle.length, strippedLines.maxOf { it.length }) + 4

        fun line(s: String): String {
            val stripped = stripColor(s)
            val padding = " ".repeat(width - stripped.length - 4)
            return "│ $s$padding │"
        }

        return buildList {
            add("┌" + "─".repeat(width - 2) + "┐")
            add(line(title))
            add("├" + "─".repeat(width - 2) + "┤")
            lines.forEach { add(line(it)) }
            add("└" + "─".repeat(width - 2) + "┘")
        }
    }

    enum class LogLevel(val color: String) {
        INFO("gray"),
        WARNING("yellow"),
        ERROR("red")
    }

}