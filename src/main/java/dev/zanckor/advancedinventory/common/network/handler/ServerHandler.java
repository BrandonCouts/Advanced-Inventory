package dev.zanckor.advancedinventory.common.network.handler;

import dev.zanckor.advancedinventory.core.data.InventoryData;
import dev.zanckor.advancedinventory.util.MCUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import static net.minecraft.world.inventory.InventoryMenu.INV_SLOT_END;
import static net.minecraft.world.inventory.InventoryMenu.INV_SLOT_START;

public class ServerHandler {

    public static void translateInventorySlots(int movementAmount, Player player) {
        int extraInvSlotStart = InventoryData.getExtraInvSlotStart();
        int extraInvSlotEnd = InventoryData.getExtraInvSlotEnd(player);

        ItemStack[] slotsBackup = MCUtil.copyInventory(player);
        MCUtil.clearSlots(player, INV_SLOT_START, INV_SLOT_END);
        MCUtil.clearSlots(player, extraInvSlotStart, extraInvSlotEnd);

        for (int slotIndex = INV_SLOT_START; slotIndex < extraInvSlotEnd; ++slotIndex) {
            if (slotIndex == INV_SLOT_END) {
                slotIndex = extraInvSlotStart;
            }

            int slotCopyIndex = slotIndex + movementAmount;

            if (slotCopyIndex < INV_SLOT_START) { // If the slot index is less than the start of the inventory, move it to the end of the extra inventory
                int slotsBeyondStart = INV_SLOT_START - slotCopyIndex;
                slotCopyIndex = extraInvSlotEnd - slotsBeyondStart;

            } else if ((slotCopyIndex > (INV_SLOT_END - 1)) && (slotCopyIndex < extraInvSlotStart) && movementAmount > 0) { // If the slot index is greater than the end of the inventory, move it to the start of the extra inventory
                int slotsBeyondEnd = slotCopyIndex - INV_SLOT_END;
                slotCopyIndex = extraInvSlotStart + slotsBeyondEnd;

            } else if ((slotCopyIndex < extraInvSlotStart) && (slotCopyIndex > (INV_SLOT_END - 1)) && movementAmount < 0) { // If the slot index is less than the end of the inventory, move it to the start of the extra inventory
                int slotsBeyondStart = extraInvSlotStart - slotCopyIndex;
                slotCopyIndex = INV_SLOT_END - slotsBeyondStart;

            } else if (slotCopyIndex > (extraInvSlotEnd - 1)) { // If the slot index is greater than the end of the extra inventory, move it to the start of the inventory
                int slotsBeyondEnd = slotCopyIndex - extraInvSlotEnd;
                slotCopyIndex = INV_SLOT_START + slotsBeyondEnd;

            }

            player.getInventory().setItem(slotCopyIndex, slotsBackup[slotIndex]); // Set the slot that will be moved to the current slot index to the item in the current slot
        }
    }

    public static void searchItemText(String text, ServerPlayer player) {
        int slotIndexStart = InventoryData.getExtraInvSlotStart() + 900; // TODO: Change this to config

        MCUtil.shortSlotsByItem(player, text, slotIndexStart, 9, InventoryData.getExtraInvSlotEnd(player), 15);
    }
}
