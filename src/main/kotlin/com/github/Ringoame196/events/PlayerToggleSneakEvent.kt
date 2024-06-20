package com.github.Ringoame196.events

import com.github.Ringoame196.managers.ChairManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.plugin.Plugin

class PlayerToggleSneakEvent(private val plugin: Plugin) : Listener {
    @EventHandler
    fun onPlayerToggleSneak(e: PlayerToggleSneakEvent) {
        val chairManager = ChairManager(plugin)
        val player = e.player
        val sneaking = e.isSneaking

        if (!sneaking) {
            return
        }

        chairManager.getOff(player)
    }
}
