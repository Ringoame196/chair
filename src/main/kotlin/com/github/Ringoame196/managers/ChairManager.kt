package com.github.Ringoame196.managers

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.io.File
import java.util.UUID

class ChairManager(plugin: Plugin) {
    private val dataFile = File(plugin.dataFolder, "data.yml")
    private val dataFileManager = YmlFileManager(dataFile)
    fun isCanSit(chairBlock: Block): Boolean {
        val location = chairBlock.location.clone()
        val upperBlock = location.add(0.0, 1.0, 0.0).block
        return upperBlock.type == Material.AIR
    }
    private fun summonArmorStand(location: Location): ArmorStand? {
        val summonLocation = location.add(0.5, -0.5, 0.5)
        val world = location.world
        // アーマースタンドを召喚
        val armorStand: ArmorStand? = world?.spawn(summonLocation, ArmorStand::class.java)
        armorStand?.let {
            // アーマースタンドの設定
            it.isVisible = false // 可視化するかどうか
            it.isSmall = true // サイズを小さくするかどうか
            it.isInvulnerable = true
            it.setGravity(false)
        }
        return armorStand
    }
    fun sit(chairBlock: Block, player: Player) {
        val location = chairBlock.location.clone()
        val chair = summonArmorStand(location) ?: return
        val playerUUID = player.uniqueId.toString()
        val chairUUID = chair.uniqueId.toString()
        chair.addPassenger(player)
        dataFileManager.setValue(playerUUID, chairUUID)
    }
    fun getOff(player: Player) {
        val chairUUID = dataFileManager.acquisitionStringValue(player.uniqueId.toString())
        val playerUUID = player.uniqueId.toString()
        if (chairUUID != null) {
            player.teleport(player.location.add(0.0, 0.5, 0.0)) // 埋まらないように
            val chair = Bukkit.getEntity(UUID.fromString(chairUUID)) ?: return
            chair.remove()
            dataFileManager.setValue(playerUUID, null)
        }
    }
}
