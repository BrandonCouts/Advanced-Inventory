package dev.zanckor.advancedinventory.core.data.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.ItemStackHandler;

import static dev.zanckor.advancedinventory.core.data.InventoryData.INVENTORY_DATA_KEY;

public class PlayerInventoryData implements INBTSerializable<CompoundTag> {
    private int availableSlots = 9;
    private ItemStackHandler inventory;

    public int getAvailableSlots() {
        return availableSlots;
    }

    public void increaseAvailableSlots(int amount) {
        availableSlots += amount;
    }

    public void saveInventory(ItemStackHandler inventory) {
        this.inventory = inventory;
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt(INVENTORY_DATA_KEY, availableSlots);

        if (inventory != null)
            nbt.put("inventory", inventory.serializeNBT());

        return nbt;
    }

    public void deserializeNBT(CompoundTag nbt) {
        availableSlots = nbt.getInt(INVENTORY_DATA_KEY);

        if (inventory != null)
            inventory.deserializeNBT(nbt.getCompound("inventory"));
    }

    public void copyForRespawn(PlayerInventoryData oldStore) {
        availableSlots = oldStore.availableSlots;

        if (inventory != null)
            inventory.deserializeNBT(oldStore.inventory.serializeNBT());
    }
}