package me.arasple.mc.trmenu.module.internal.service

import io.izzel.taboolib.Version
import io.izzel.taboolib.module.inject.TListener
import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.api.action.pack.Reactions
import me.arasple.mc.trmenu.module.display.MenuSession
import me.arasple.mc.trmenu.module.internal.data.Metadata
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.inventory.EquipmentSlot

/**
 * @author Arasple
 * @date 2021/2/4 15:37
 */
object Shortcuts {

    private val sneaking = mutableMapOf<String, Long>()

    private fun Player.isSneakingValid(): Boolean {
        return isSneaking && System.currentTimeMillis() - sneaking.computeIfAbsent(name) { System.currentTimeMillis() } <= 1500
    }

    private fun offhand(player: Player): Boolean {
        val reaction =
            if (player.isSneakingValid()) Type.REACTIONS[Type.SNEAKING_OFFHAND]
            else Type.REACTIONS[Type.OFFHAND]

        return reaction?.eval(player) ?: false
    }


    private fun rightClickPlayer(player: Player, clicked: Player): Boolean {
        val reaction =
            if (player.isSneakingValid()) Type.REACTIONS[Type.SNEAKING_RIGHT_CLICK_PLAYER]
            else Type.REACTIONS[Type.RIGHT_CLICK_PLAYER]

        MenuSession.getSession(player).arguments = arrayOf(clicked.name)

        return reaction?.eval(player) ?: false
    }

    private fun borderClick(player: Player, click: ClickType) {
        val type = when {
            click.isLeftClick -> Type.PLAYER_INVENTORY_BORDER_LEFT
            click.isRightClick -> Type.PLAYER_INVENTORY_BORDER_RIGHT
            else -> Type.PLAYER_INVENTORY_BORDER_MIDDLE
        }
        Type.REACTIONS[type]?.eval(player)
    }

    enum class Type(val key: String) {

        OFFHAND("Offhand"),

        SNEAKING_OFFHAND("Sneaking-Offhand"),

        RIGHT_CLICK_PLAYER("Right-Click-Player"),

        SNEAKING_RIGHT_CLICK_PLAYER("Sneaking-Right-Click-Player"),

        PLAYER_INVENTORY_BORDER_LEFT("PlayerInventory-Border-Left"),

        PLAYER_INVENTORY_BORDER_RIGHT("PlayerInventory-Border-Left"),

        PLAYER_INVENTORY_BORDER_MIDDLE("PlayerInventory-Border-Left");

        companion object {

            val REACTIONS = mutableMapOf<Type, Reactions>()

            fun load() {
                REACTIONS.clear()

                values().forEach {
                    Reactions.ofReaction(TrMenu.SETTINGS.get("Shortcuts.${it.key}")).let { react ->
                        if (!react.isEmpty()) {
                            REACTIONS[it] = react
                        }
                    }
                }
            }

        }


    }

    @TListener
    class Listener : org.bukkit.event.Listener {

        @EventHandler(ignoreCancelled = true)
        fun rightClick(e: PlayerInteractEntityEvent) {
            if (Version.isAfter(Version.v1_9) && e.hand == EquipmentSlot.OFF_HAND) return

            val clicked = e.rightClicked
            if (clicked is Player && Metadata.data.containsKey(clicked.name)) {
                e.isCancelled = rightClickPlayer(e.player, clicked)
            }
        }

        @EventHandler(ignoreCancelled = true)
        fun toggleSneaking(e: PlayerToggleSneakEvent) {
            sneaking.put(e.player.name, System.currentTimeMillis())
        }

        @EventHandler(ignoreCancelled = true)
        fun onClick(e: InventoryClickEvent) {
            val player = e.whoClicked as Player

            if (player.openInventory.topInventory.holder == player.inventory.holder && e.slot < 0) {
                borderClick(player, e.click)
            }
        }

    }

    @TListener(version = ">=10900")
    class High : org.bukkit.event.Listener {

        @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
        fun offHand(e: PlayerSwapHandItemsEvent) {
            e.isCancelled = offhand(e.player)
        }

    }

}