package dev.zanckor.advancedinventory.core.data.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.ItemStackHandler;

import static dev.zanckor.advancedinventory.core.data.InventoryData.INVENTORY_DATA_KEY;

public class PlayerInventoryData implements INBTSerializable<CompoundTag> {
    private int availableSlots = 9;

    public int getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(int availableSlots) {
        this.availableSlots = availableSlots;
    }

    public void increaseAvailableSlots(int amount) {
        availableSlots += amount;
    }


    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt(INVENTORY_DATA_KEY, availableSlots);

        return nbt;
    }

    public void deserializeNBT(CompoundTag nbt) {
        availableSlots = nbt.getInt(INVENTORY_DATA_KEY);
    }

    public void copyForRespawn(PlayerInventoryData oldStore) {
        availableSlots = oldStore.availableSlots;
    }
}