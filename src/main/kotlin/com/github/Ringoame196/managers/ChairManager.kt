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
        chair.addPassenger(player) // 召喚した防具立ての上に座らせる
        dataFileManager.setValue(playerUUID, chairUUID) // プレイヤーが座っている防具立てを保存する
    }
    fun getOff(player: Player) {
        val playerUUID = player.uniqueId.toString()
        val chairUUID = dataFileManager.acquisitionStringValue(playerUUID) // プレイヤーが座っている防具立てを取得する
        if (chairUUID != null) {
            val teleportLocation = player.location.add(0.0, 0.8, 0.0)
            player.teleport(teleportLocation) // 埋まらないようにテレポート
            val chair = Bukkit.getEntity(UUID.fromString(chairUUID)) ?: return
            chair.remove() // 防具立てを切るする
            dataFileManager.setValue(playerUUID, null) // データを削除する
        }
    }
}
