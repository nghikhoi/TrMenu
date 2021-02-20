package me.arasple.mc.trmenu.api

import io.izzel.taboolib.kotlin.kether.KetherShell
import io.izzel.taboolib.kotlin.kether.common.util.LocalizedException
import me.arasple.mc.trmenu.module.display.Menu
import me.arasple.mc.trmenu.module.internal.script.EvalResult
import me.arasple.mc.trmenu.module.internal.service.Performance
import org.bukkit.entity.Player
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


/**
 * @author Arasple
 * @date 2021/1/25 9:54
 */
object TrMenuAPI {

    /**
     * Get the menu through its ID
     *
     * @return Menu
     * @see Menu
     */
    fun getMenuById(id: String): Menu? {
        return Menu.menus.find { it.id == id }
    }

    @JvmStatic
    fun eval(player: Player, script: String): CompletableFuture<Any?> {
        Performance.MIRROR.check("Handler:Script:Evaluation") {
            return try {
                KetherShell.eval(script, namespace = listOf("trhologram", "trmenu")) {
                    sender = player
                }
            } catch (e: LocalizedException) {
                println("§c[TrMenu] §8Unexpected exception while parsing kether shell:")
                e.localizedMessage.split("\n").forEach {
                    println("         §8$it")
                }
                CompletableFuture.completedFuture(false)
            }
        }
        throw Exception()
    }

    @JvmStatic
    fun instantKether(player: Player, script: String, timout: Long = 500): EvalResult {
        return try {
            EvalResult(eval(player, script).get(timout, TimeUnit.MILLISECONDS))
        } catch (e: TimeoutException) {
            println("§c[TrMenu] §8Timeout while parsing kether shell:")
            e.localizedMessage.split("\n").forEach { println("         §8$it") }
            EvalResult.FALSE
        }
    }

}