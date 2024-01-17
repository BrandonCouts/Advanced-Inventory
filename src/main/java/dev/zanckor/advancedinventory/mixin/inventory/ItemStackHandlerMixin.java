package dev.zanckor.advancedinventory.mixin.inventory;

import dev.zanckor.advancedinventory.core.config.ServerConfig;
import net.minecraftforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(ItemStackHandler.class)
public class ItemStackHandlerMixin {
    @Inject(method = "getSlotLimit", at = @At("RETURN"), cancellable = true, remap = false)
    private void increaseStackLimit(int slot, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(ServerConfig.LIMIT_STACK_SIZE.get() == ServerConfig.DEFAULT_MINECRAFT_SIZE ? cir.getReturnValue() : ServerConfig.LIMIT_STACK_SIZE.get());
    }

    @Inject(method = "getStackLimit", at = @At("RETURN"), cancellable = true, remap = false)
    private void increaseStackLimit(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(ServerConfig.LIMIT_STACK_SIZE.get() == ServerConfig.DEFAULT_MINECRAFT_SIZE ? cir.getReturnValue() : ServerConfig.LIMIT_STACK_SIZE.get());
    }
}