package com.marrineer.elyInv.utils

import com.marrineer.elyInv.ElyInv
import net.kyori.adventure.platform.bukkit.BukkitAudiences
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

    fun getBannerList(): List<String> {
        return listOf(
            "      _         _            ",
            """  ___| |_   _  (_)_ ____   __""",
            """ / _ | | | | | | | '_ \ \ / /""",
            """|  __| | |_| | | | | | \ V / """,
            """ \___|_|\__, | |_|_| |_|\_/  """,
            "        |___/                "
        )
    }

    fun colorizeBanner(banner: List<String>): String {
        val colors = listOf("&d", "&5", "&d", "&5", "&d", "&5")
        return banner.mapIndexed { idx, line ->
            "${colors.getOrElse(idx) { "&d" }}$line"
        }.joinToString("\n")
    }

    enum class LogLevel(val color: String) {
        INFO("gray"),
        WARNING("yellow"),
        ERROR("red")
    }

}