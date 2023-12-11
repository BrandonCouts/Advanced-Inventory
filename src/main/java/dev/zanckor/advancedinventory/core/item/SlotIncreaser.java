package dev.zanckor.advancedinventory.core.item;

import dev.zanckor.advancedinventory.core.data.InventoryData;
import dev.zanckor.advancedinventory.core.inventory.slot.AvailableSlot;
import dev.zanckor.advancedinventory.util.MCUtil;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

public class SlotIncreaser extends Item {
    public SlotIncreaser(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Player player = context.getPlayer();

        if (player != null) {
            // Change the next 9 slots to be extra inventory slots
            for (int slotIndex = InventoryData.getExtraInvSlotEnd(player); slotIndex < InventoryData.getExtraInvSlotEnd(player) + 9; slotIndex++) {
                AvailableSlot slot = (AvailableSlot) player.containerMenu.slots.get(slotIndex);
                slot.setAvailable(true);
            }

            InventoryData.increaseExtraInvSlotStart(9, player);
            MCUtil.sendPlayerMessage(player, "You now have " + InventoryData.getExtraInvSlotEnd(player) + " extra inventory slots.");
        }


        return super.onItemUseFirst(stack, context);
    }
}
