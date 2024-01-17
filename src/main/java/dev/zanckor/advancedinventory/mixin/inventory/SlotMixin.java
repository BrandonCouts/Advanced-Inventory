package dev.zanckor.advancedinventory.mixin.inventory;

import dev.zanckor.advancedinventory.core.config.ServerConfig;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Slot.class)
public abstract class SlotMixin {

    /**
     * @author Zanckor
     * @reason Limit the stack size of items in the inventory
     */
    @Overwrite
    public int getMaxStackSize() {
        return ServerConfig.LIMIT_STACK_SIZE.get() == ServerConfig.DEFAULT_MINECRAFT_SIZE ? 64 : ServerConfig.LIMIT_STACK_SIZE.get();
    }

}