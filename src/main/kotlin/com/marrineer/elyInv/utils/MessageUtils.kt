package com.marrineer.elyInv.utils

import com.marrineer.elyInv.ElyInv
import com.marrineer.elyInv.managers.ConfigManager
import me.clip.placeholderapi.PlaceholderAPI
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@Suppress("unused")
class MessageUtils(
    val plugin: ElyInv,
    val hasPAPI: Boolean,
    val adventure: BukkitAudiences,
    val prefix: String
) {
    private val miniMessage: MiniMessage = MiniMessage.miniMessage()

    private fun audience(player: Player): Audience {
        return adventure.player(player)
    }

    private fun audience(sender: CommandSender): Audience {
        return adventure.sender(sender)
    }

    //===================== GET FROM FILE =====================//
    fun get(type: ConfigManager.FileType, placeholder: String) {
        when (type) {
            ConfigManager.FileType.CONFIG -> {
                plugin.configManager.get(placeholder)
            }

            ConfigManager.FileType.MESSAGE -> {
                plugin.messageManager.get(placeholder)
            }
        }
    }

    //===================== SEND WITH PREFIX =====================//
    fun sendWithPrefixToSender(sender: CommandSender, text: Unit) {
        sendToSender(
            sender,
            String.format("%s <reset>%s", prefix, text)
        )
    }

    fun sendWithPrefixToPlayer(player: Player, text: String) {
        sendToPlayer(
            player,
            String.format("%s <reset>%s", prefix, text)
        )
    }

    //===================== SEND ONLY =====================//
    fun sendToSender(sender: CommandSender, text: String) {
        audience(sender).sendMessage {
            miniMessage.deserialize(text)
        }
    }

    fun sendToPlayer(player: Player, text: String) {
        audience(player).sendMessage {
            miniMessage.deserialize(text)
        }
    }

    fun parse(parser: Player, text: String): String {
        return if (hasPAPI) PlaceholderAPI.setPlaceholders(parser, text) else text
    }
}