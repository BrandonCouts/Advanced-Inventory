package dev.zanckor.advancedinventory.mixin.inventory;

import dev.zanckor.advancedinventory.core.config.ServerConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Inject(method = "getMaxStackSize", at = @At("RETURN"), cancellable = true)
    public void getMaxStackSize(CallbackInfoReturnable<Integer> cir) {
        int limitStackSize = ServerConfig.spec.isLoaded() ? ServerConfig.LIMIT_STACK_SIZE.get() : ServerConfig.DEFAULT_MINECRAFT_SIZE;

        if (limitStackSize == ServerConfig.DEFAULT_MINECRAFT_SIZE)
            return; // In this case, we don't want to change the max stack size.
        cir.setReturnValue(limitStackSize);
    }
}
