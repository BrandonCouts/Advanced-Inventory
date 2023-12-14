package dev.zanckor.advancedinventory.core.inventory.slot;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SearchSlot extends Slot {

    public SearchSlot(Container container, int slotIndex, int x, int y) {
        super(container, slotIndex, x, y);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack p_40231_) {
        return false;
    }
}