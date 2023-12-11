package dev.zanckor.advancedinventory.mixin.inventory;

import dev.zanckor.advancedinventory.core.data.InventoryData;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slot.class)
public class ContainerMixin {

    @Inject(method = "getMaxStackSize*", at = @At("TAIL"), cancellable = true)
    public void getMaxStackSize(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(InventoryData.MAX_STACK_SIZE);
    }
}
