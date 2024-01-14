package dev.zanckor.advancedinventory.mixin.inventory;

import dev.zanckor.advancedinventory.core.config.ServerConfig;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slot.class)
public class ContainerMixin {

    @Inject(method = "getMaxStackSize*", at = @At("HEAD"), cancellable = true)
    public void getMaxStackSize(CallbackInfoReturnable<Integer> cir) {
        int limitStackSize = ServerConfig.spec.isLoaded() ? ServerConfig.LIMIT_STACK_SIZE.get() : ServerConfig.DEFAULT_MINECRAFT_SIZE;

        if (limitStackSize == ServerConfig.DEFAULT_MINECRAFT_SIZE) return; // In this case, we don't want to change the max stack size.
        cir.setReturnValue(limitStackSize);
    }
}