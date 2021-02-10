package me.arasple.mc.trmenu.module.internal.hook.impl

import me.arasple.mc.trmenu.module.internal.hook.HookAbstract
import me.playernguyen.opteco.api.OptEcoAPI
import org.black_ixx.playerpoints.PlayerPoints
import org.black_ixx.playerpoints.PlayerPointsAPI
import org.bukkit.OfflinePlayer

/**
 * @author Arasple
 * @date 2021/1/26 22:18
 */
class HookOptEco : HookAbstract() {

    private fun getOptAPI(player: OfflinePlayer): OptEcoAPI {
        return OptEcoAPI(player.uniqueId);
    }

    fun getPoints(player: OfflinePlayer): Int {
        return getOptAPI(player).points.toInt()
    }

    fun setPoints(player: OfflinePlayer, amount: Int) {
        getOptAPI(player).points = amount.toDouble()
    }

    fun hasPoints(player: OfflinePlayer, amount: Int): Boolean {
        return getPoints(player) >= amount
    }

    fun addPoints(player: OfflinePlayer, amount: Int) {
        getOptAPI(player).addPoints(amount.toDouble())
    }

    fun takePoints(player: OfflinePlayer, amount: Int) {
        getOptAPI(player).takePoints(amount.toDouble());
    }

}