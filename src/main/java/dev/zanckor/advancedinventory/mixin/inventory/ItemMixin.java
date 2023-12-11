package dev.zanckor.advancedinventory.mixin.inventory;

import dev.zanckor.advancedinventory.core.data.InventoryData;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Item.class)
public class ItemMixin {

    @Mutable
    @Shadow
    @Final
    private int maxStackSize;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(Item.Properties properties, CallbackInfo ci) {
        this.maxStackSize = InventoryData.MAX_STACK_SIZE;
    }
}
