package com.marrineer.elyInv.utils

import com.marrineer.elyInv.ElyInv
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage

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

    enum class LogLevel(val color: String) {
        INFO("gray"),
        WARNING("yellow"),
        ERROR("red")
    }

}