package me.arasple.mc.trmenu.api.action.impl

import io.izzel.taboolib.util.Features
import me.arasple.mc.trmenu.api.action.base.AbstractAction
import me.arasple.mc.trmenu.api.action.base.ActionOption
import me.arasple.mc.trmenu.util.Tasks
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/1/31 11:38
 */
class ActionCommand(content: String, option: ActionOption) : AbstractAction(content, option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        Tasks.task(false) {
            parseContentSplited(placeholderPlayer, ";").forEach {
                Features.dispatchCommand(player, it)
            }
        }
    }

    companion object {

        private val name = "command|cmd|player|execute".toRegex()

        private val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionCommand(value.toString(), option)
        }

        val registery = name to parser

    }

}