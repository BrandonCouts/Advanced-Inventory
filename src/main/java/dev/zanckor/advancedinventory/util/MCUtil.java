package dev.zanckor.advancedinventory.util;

import dev.zanckor.advancedinventory.core.data.InventoryData;
import dev.zanckor.advancedinventory.core.inventory.slot.SearchSlot;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

public class MCUtil {

    public static void sendPlayerMessage(Player player, String message) {
        player.sendSystemMessage(Component.literal(message));
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

    /**
     * @param player                Player whose inventory will be searched
     * @param text                  Item name to search for
     * @param searchSlotIndex       Slot index start that will copy the shorted items into
     * @param indexSearchRangeStart Slot index start to search for items
     * @param indexSearchRangeEnd   Slot index end to search for items
     * @param searchLimit           Max number of items to copy
     */
    public static void shortSlotsByItem(Player player, String text, int searchSlotIndex, int indexSearchRangeStart, int indexSearchRangeEnd, int searchLimit) {
        int[] slotIndexes = getShortedSlotsByItemName(player, text, indexSearchRangeStart, indexSearchRangeEnd, searchLimit);

        for(int slotIndex = 0; slotIndex < slotIndexes.length; slotIndex++) {
            int shortedSlot = slotIndex + searchSlotIndex;
            Slot copySlot = slotIndexes[slotIndex] != Integer.MAX_VALUE ?
                    player.containerMenu.getSlot(slotIndexes[slotIndex]) :
                    new SearchSlot(player.getInventory(), Integer.MAX_VALUE, -1000000, -1000000);

            player.containerMenu.slots.set(shortedSlot, copySlot);
        }
    }

    public static int[] getShortedSlotsByItemName(Player player, String text, int slotRangeStart, int slotRangeEnd, int searchLimit) {
        int[] slotIndexes = new int[searchLimit];
        Arrays.fill(slotIndexes, Integer.MAX_VALUE); // Fill the array with MAX_VALUE so that we can check if the slot index is valid
        int foundItems = 0;
        boolean hasFoundEnoughItems = text.isEmpty();

        for (int slotIndex = slotRangeStart; slotIndex < slotRangeEnd && !hasFoundEnoughItems; slotIndex++) {
            Slot slot = player.containerMenu.getSlot(slotIndex);
            String itemName = slot.getItem().getHoverName().getString().toLowerCase();
            String searchText = text.toLowerCase();

            if (itemName.contains(searchText) && !slot.getItem().isEmpty()) {
                slotIndexes[foundItems++] = slot.index;

                if (foundItems >= searchLimit) {
                    hasFoundEnoughItems = true;
                }
            }
        }

        return slotIndexes;
    }

    public static Entity getEntityLookinAt(Entity rayTraceEntity, double distance) {
        float playerRotX = rayTraceEntity.getXRot();
        float playerRotY = rayTraceEntity.getYRot();
        Vec3 startPos = rayTraceEntity.getEyePosition();
        float f2 = Mth.cos(-playerRotY * ((float) Math.PI / 180F) - (float) Math.PI);
        float f3 = Mth.sin(-playerRotY * ((float) Math.PI / 180F) - (float) Math.PI);
        float f4 = -Mth.cos(-playerRotX * ((float) Math.PI / 180F));
        float additionY = Mth.sin(-playerRotX * ((float) Math.PI / 180F));
        float additionX = f3 * f4;
        float additionZ = f2 * f4;
        double d0 = distance;
        Vec3 endVec = startPos.add(
                ((double) additionX * d0),
                ((double) additionY * d0),
                ((double) additionZ * d0));

        AABB startEndBox = new AABB(startPos, endVec);
        Entity entity = null;
        for (Entity entity1 : rayTraceEntity.level().getEntities(rayTraceEntity, startEndBox, (val) -> true)) {
            AABB aabb = entity1.getBoundingBox().inflate(entity1.getPickRadius());
            Optional<Vec3> optional = aabb.clip(startPos, endVec);
            if (aabb.contains(startPos)) {
                if (d0 >= 0.0D) {
                    entity = entity1;
                    startPos = optional.orElse(startPos);
                    d0 = 0.0D;
                }
            } else if (optional.isPresent()) {
                Vec3 vec31 = optional.get();
                double d1 = startPos.distanceToSqr(vec31);
                if (d1 < d0 || d0 == 0.0D) {
                    if (entity1.getRootVehicle() == rayTraceEntity.getRootVehicle() && !entity1.canRiderInteract()) {
                        if (d0 == 0.0D) {
                            entity = entity1;
                            startPos = vec31;
                        }
                    } else {
                        entity = entity1;
                        startPos = vec31;
                        d0 = d1;
                    }
                }
            }
        }

        return entity;
    }
}
