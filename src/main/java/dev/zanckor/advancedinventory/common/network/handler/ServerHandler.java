package dev.zanckor.advancedinventory.common.network.handler;

import dev.zanckor.advancedinventory.core.data.InventoryData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

import static net.minecraft.world.inventory.InventoryMenu.INV_SLOT_END;
import static net.minecraft.world.inventory.InventoryMenu.INV_SLOT_START;

public class ServerHandler {

    public static void translateInventorySlots(int movementAmount, Player player) {
        int extraInvSlotStart = InventoryData.getExtraInvSlotStart();
        int extraInvSlotEnd = InventoryData.getExtraInvSlotEnd(player);

        ItemStack[] slotsBackup = copyInventory(player);
        clearSlots(player, INV_SLOT_START, INV_SLOT_END);
        clearSlots(player, extraInvSlotStart, extraInvSlotEnd);

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

    public static ItemStack[] copyInventory(Player player) {
        ItemStack[] slotsBackup = new ItemStack[player.containerMenu.slots.size()].clone();
        AbstractContainerMenu containerMenu = player.inventoryMenu;

        for (int realInventorySlotIndex = 0; realInventorySlotIndex < containerMenu.slots.size(); realInventorySlotIndex++) {
            slotsBackup[realInventorySlotIndex] = containerMenu.slots.get(realInventorySlotIndex).getItem().copy();
        }

        return slotsBackup;
    }

    public static void clearSlots(Player player, int startSlot, int endSlot) {
        for (int slotIndex = startSlot; slotIndex < endSlot; slotIndex++) {
            player.getInventory().setItem(slotIndex, ItemStack.EMPTY);
        }
    }
}
