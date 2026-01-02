package com.marrineer.elyInv.placeholder

import com.marrineer.elyInv.ElyInv
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.entity.Player

class ElyPlaceholderExpansion(
    private val plugin: ElyInv
) : PlaceholderExpansion() {
    override fun getIdentifier(): String = "elyinv"

    override fun getAuthor(): String = plugin.description.authors.joinToString(", ")

    override fun getVersion(): String = plugin.description.version

    override fun onPlaceholderRequest(player: Player?, params: String): String? {
        if (player == null) return ""
        val uuid = player.uniqueId

        return when (params.lowercase()) {
            "usage" -> plugin.playerManager.getCount(uuid).toString()
            "max" -> plugin.configManager.getMaxUsage().toString()
            "remaining" -> {
                val current = plugin.playerManager.getCount(uuid)
                val remaining = plugin.configManager.getMaxUsage() - current
                maxOf(remaining, 0).toString()
            }
            else -> null
        }
    }

}