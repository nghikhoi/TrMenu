package me.arasple.mc.trmenu.api.receptacle.window

import org.bukkit.event.inventory.InventoryType

/**
 * @author Arasple
 * @date 2020/11/29 10:39
 *
 * reference https://wiki.vg/Inventory#Windows
 */
enum class ReceptacleType(
    val vanillaId: Int,
    val slotRange: IntRange
) {

    /**
     *
     * A 1-row inventory, not used by the notchian server.
     */
    GENERIC_9X1(
        0,
        0..8
    ),

    /**
     * A 2-row inventory, not used by the notchian server.
     */
    GENERIC_9X2(
        1,
        0..17
    ),

    /**
     * General-purpose 3-row inventory. Used by Chest, minecart with chest, ender chest, and barrel
     */
    GENERIC_9X3(
        2,
        0..26
    ),

    /**
     * A 4-row inventory, not used by the notchian server.
     */
    GENERIC_9X4(
        3,
        0..35
    ),

    /**
     * A 5-row inventory, not used by the notchian server.
     */
    GENERIC_9X5(
        4,
        0..44
    ),

    /**
     * General-purpose 6-row inventory, used by large chests.
     */
    GENERIC_9X6(
        5,
        0..53
    ),

    /**
     * General-purpose 3-by-3 square inventory, used by Dispenser and Dropper
     */
    GENERIC_3X3(
        6,
        0..8
    ),

    /**
     * Anvil
     */
    ANVIL(
        7,
        0..2
    ),

    /**
     * Beacon
     */
    BEACON(
        8,
        0..0
    ),

    BLAST_FURNACE(
        9,
        0..2
    ),

    BREWING_STAND(
        10,
        0..4
    ),

    CRAFTING(
        11,
        0..9
    ),

    ENCHANTMENT_TABLE(
        12,
        0..1
    ),

    FURNACE(
        13,
        0..2
    ),


    GRINDSTONE(
        14,
        0..2
    ),

    HOPPER(
        15,
        0..4
    ),


//    LECTERN("minecraft:lectern"),

    LOOM(
        17,
        0..3
    ),

    MERCHANT(
        18,
        0..2
    ),

    SHULKER_BOX(
        19,
        0..26
    ),

    SMOKER(
        20,
        0..2
    ),

    CARTOGRAPHY(
        21,
        0..2
    ),

    STONECUTTER(
        22,
        0..1
    );

    val vanillaType by lazy { "minecraft:${toBukkitType().name.toLowerCase()}" }

    /**
     * Main inventory slot range
     */
    val mainInvSlotRange = slotRange.last + 1..slotRange.last + 27

    val mainInvSlots = mainInvSlotRange.toList()

    /**
     * Hotbar slot range
     */
    val hotBarSlotRange = mainInvSlotRange.last + 1..mainInvSlotRange.last + 9

    val hotBarSlots = hotBarSlotRange.toList()

    /**
     * Container size
     */
    val containerSize = slotRange.last - (slotRange.first - 1)

    /**
     * Total size (with hotbar & mainInv)
     * Not slot index
     */
    val totalSize = containerSize + 37

    private fun toBukkitType(): InventoryType {
        return when (this) {
            GENERIC_9X1, GENERIC_9X2, GENERIC_9X4, GENERIC_9X5, GENERIC_9X6, GENERIC_3X3 -> InventoryType.CHEST
            GENERIC_9X3 -> InventoryType.HOPPER
            ANVIL -> InventoryType.ANVIL
            BEACON -> InventoryType.BEACON
            BLAST_FURNACE -> InventoryType.BLAST_FURNACE
            BREWING_STAND -> InventoryType.BREWING
            CRAFTING -> InventoryType.CRAFTING
            ENCHANTMENT_TABLE -> InventoryType.ENCHANTING
            FURNACE -> InventoryType.FURNACE
            GRINDSTONE -> InventoryType.GRINDSTONE
            HOPPER -> InventoryType.HOPPER
            LOOM -> InventoryType.LOOM
            MERCHANT -> InventoryType.MERCHANT
            SHULKER_BOX -> InventoryType.SHULKER_BOX
            SMOKER -> InventoryType.SMOKER
            CARTOGRAPHY -> InventoryType.CARTOGRAPHY
            STONECUTTER -> InventoryType.STONECUTTER
        }
    }

    companion object {

        fun ofRows(rows: Int): ReceptacleType {
            return when (rows) {
                1 -> GENERIC_9X1
                2 -> GENERIC_9X2
                3 -> GENERIC_9X3
                4 -> GENERIC_9X4
                5 -> GENERIC_9X5
                6 -> GENERIC_9X6
                else -> throw IllegalArgumentException("Rows for chest must be an integer between [1, 6]")
            }
        }

    }

}