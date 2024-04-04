package dev.zanckor.advancedinventory.core.item;

import dev.zanckor.advancedinventory.core.data.InventoryData;
import dev.zanckor.advancedinventory.core.inventory.slot.AvailableSlot;
import dev.zanckor.advancedinventory.util.MCUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class SlotIncreaser extends Item {
    public SlotIncreaser(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Player player = context.getPlayer();

        if (player != null) {
            InventoryData.increaseExtraInvSlotStart( 9, player);


            if(player.containerMenu.slots.size() >= InventoryData.getExtraInvSlotEnd(player) + 9) {
                // Change the next 9 * quantity slots to be extra inventory slots
                for (int slotIndex = InventoryData.getExtraInvSlotEnd(player); slotIndex < InventoryData.getExtraInvSlotEnd(player) + 9; slotIndex++) {

                    if(player.containerMenu.getSlot(slotIndex) instanceof AvailableSlot slot) {
                        slot.setAvailable(false);
                    }
                }

                stack.shrink(1);
                MCUtil.sendPlayerMessage(player, "You have increased your extra inventory slots by 9 to " + InventoryData.getExtraInvSlotEnd(player) + " slots.");
            } else {
                MCUtil.sendPlayerMessage(player, "You have reached the maximum amount of extra inventory slots.");
            }
        }


        return super.onItemUseFirst(stack, context);
    }
}

