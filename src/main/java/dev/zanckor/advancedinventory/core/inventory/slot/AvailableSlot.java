package dev.zanckor.advancedinventory.core.inventory.slot;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;

public class AvailableSlot extends Slot {
    private boolean isAvailable;
    private Player owner;

    public AvailableSlot(Container container, int slotIndex, int x, int y, boolean isAvailable, Player owner) {
        super(container, slotIndex, x, y);

        this.isAvailable = isAvailable;
        this.owner = owner;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public boolean isActive() {
        return !owner.isCreative() && !owner.isSpectator() && isAvailable();
    }
}