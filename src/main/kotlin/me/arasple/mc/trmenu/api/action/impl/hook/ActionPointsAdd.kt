package me.arasple.mc.trmenu.api.action.impl.hook

import me.arasple.mc.trmenu.api.action.base.AbstractAction
import me.arasple.mc.trmenu.api.action.base.ActionOption
import me.arasple.mc.trmenu.module.internal.hook.HookPlugin
import me.arasple.mc.trmenu.util.Tasks
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/1/31 16:21
 */
class ActionPointsAdd(content: String, option: ActionOption) : AbstractAction(content, option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        Tasks.task(false) {
            val amount = parseContent(placeholderPlayer).toIntOrNull() ?: -1
            if (amount > 0) {
                HookPlugin.getPlayerPoints().addPoints(player, amount)
            }
        }
    }

    companion object {

        private val name = "(give|add|deposit)-?points?".toRegex()

        private val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionPointsAdd(value.toString(), option)
        }

        val registery = name to parser

    }

}