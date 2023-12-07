package dev.zanckor.advancedinventory.common.network.handler;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

import static net.minecraft.world.inventory.InventoryMenu.*;

public class ServerHandler {
    public static final int EXTRA_INV_SLOT_START = SHIELD_SLOT + 1; // TODO: Need to change this to other class (Prolly capabilities)
    public static final int EXTRA_INV_SLOT_END = EXTRA_INV_SLOT_START + 9;



    public static void moveSlots(int movementAmount, Player player) {
        AbstractContainerMenu containerMenu = player.containerMenu;
        ItemStack[] slotsBackup = new ItemStack[containerMenu.slots.size()].clone();

        for (int i = 0; i < containerMenu.slots.size(); i++) {
            slotsBackup[i] = containerMenu.slots.get(i).getItem().copy();
        }


        for (int slotIndex = INV_SLOT_START; slotIndex < INV_SLOT_END; ++slotIndex) {
            int slotCopyIndex = slotIndex + movementAmount;

            if (slotCopyIndex < INV_SLOT_START) {
                int slotsBeyondStart = INV_SLOT_START - slotCopyIndex;
                slotCopyIndex = INV_SLOT_END - slotsBeyondStart;

            } else if (slotCopyIndex > INV_SLOT_END - 1) {
                int slotsBeyondEnd = slotCopyIndex - INV_SLOT_END;
                slotCopyIndex = INV_SLOT_START + slotsBeyondEnd;

            }

            ItemStack itemStackForCopy = slotsBackup[slotCopyIndex]; // This is the ItemStack that will be moved to the current slot index
            containerMenu.getSlot(slotIndex).set(itemStackForCopy); // Set the current slot to the item in the slot that will be moved to the current slot index
        }
    }
}
