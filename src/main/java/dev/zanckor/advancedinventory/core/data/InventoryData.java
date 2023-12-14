package dev.zanckor.advancedinventory.core.data;

import dev.zanckor.advancedinventory.core.data.capability.PlayerInventoryData;
import dev.zanckor.advancedinventory.core.data.capability.PlayerInventoryDataProvider;
import net.minecraft.world.entity.player.Player;

import static dev.zanckor.advancedinventory.AdvancedInventory.MODID;

public class InventoryData {
    public static final int MAX_STACK_SIZE = 1000; // TODO: Change with config
    private static final int EXTRA_INVENTORY_SLOT = 55;

    public static int getExtraInvSlotStart() {
        return EXTRA_INVENTORY_SLOT;
    }

    public static int getExtraInvSlotEnd(Player player) {
        PlayerInventoryData playerData = PlayerInventoryDataProvider.getPlayer(player);
        return EXTRA_INVENTORY_SLOT + playerData.getAvailableSlots();
    }

    public static void increaseExtraInvSlotStart(int amount, Player player) {
        PlayerInventoryData playerData = PlayerInventoryDataProvider.getPlayer(player);
        playerData.increaseAvailableSlots(amount);
    }

    public static String INVENTORY_DATA_KEY = MODID + ":inventory_data";
}
