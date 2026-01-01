package com.marrineer.elyInv.listeners

import com.marrineer.elyInv.ElyInv
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

class PlayerDeathListener(
    private val plugin: ElyInv
) : Listener {
    @EventHandler(priority = EventPriority.HIGH)
    fun onPlayerDeath(event: PlayerDeathEvent) {
        val uuid = event.entity.uniqueId

        if (!plugin.playerManager.getToggle(uuid)) return
        if (plugin.playerManager.getCount(uuid) <= 0) return

        event.apply {
            keepInventory = true
            keepLevel = true
            droppedExp = 0
        }

        plugin.playerManager.subtractCount(uuid, 1)
    }
}