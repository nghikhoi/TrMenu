package me.arasple.mc.trmenu.module.internal.data

import io.izzel.taboolib.module.config.TConfig
import io.izzel.taboolib.module.db.local.LocalPlayer
import io.izzel.taboolib.module.inject.TFunction
import io.izzel.taboolib.module.inject.TSchedule
import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.module.display.MenuSession
import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue

/**
 * @author Arasple
 * @date 2021/1/27 11:46
 *
 * differences:
 *
 * <arguments> -> for each menu session
 * <meta> -> only lost when the server is shut down
 * <data> -> storable, (support MySQL)
 */
object Metadata {

    internal val meta = mutableMapOf<String, DataMap>()
    internal val data = mutableMapOf<String, DataMap>()
    internal val globalData = TConfig.create(TrMenu.plugin, "data/globalData.yml")

    @TSchedule(delay = 100, period = 20 * 30, async = true)
    @TFunction.Cancel
    fun save() {
        data.forEach { (player, dataMap) ->
            getLocalePlayer(player)?.let {
                dataMap.data.forEach { (key, value) -> it.set("TrMenu.Data.$key", value) }
            }
        }
    }

    @Suppress("DEPRECATION")
    fun getLocalePlayer(playerName: String): FileConfiguration? {
        return LocalPlayer.get(Bukkit.getOfflinePlayer(playerName))
    }

    fun loadData(player: Player) {
        data[player.name] = DataMap(
            getLocalePlayer(player.name)!!.getConfigurationSection("TrMenu.Data")?.getValues(true) ?: mutableMapOf()
        )
    }

    fun <T> getData(target: T): DataMap {
        return data.computeIfAbsent(getPlayerName(target)) { DataMap() }
    }

    fun <T> getMeta(target: T): DataMap {
        return meta.computeIfAbsent(getPlayerName(target)) { DataMap() }
    }

    fun setBukkitMeta(player: Player, key: String, value: String = "") {
        player.setMetadata(key, FixedMetadataValue(TrMenu.plugin, value))
    }

    fun byBukkit(player: Player, key: String): Boolean {
        return player.hasMetadata(key).also {
            if (it) player.removeMetadata(key, TrMenu.plugin)
        }
    }

    private fun <T> getPlayerName(target: T): String {
        return when (target) {
            is Player -> target.name
            is MenuSession -> target.placeholderPlayer.name
            else -> throw Exception("?????")
        }
    }

}