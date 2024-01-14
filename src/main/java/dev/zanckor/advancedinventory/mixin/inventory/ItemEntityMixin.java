package dev.zanckor.advancedinventory.mixin.inventory;

import dev.zanckor.advancedinventory.core.config.ServerConfig;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {

    @Inject(method = "merge(Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)V", at = @At("HEAD"), cancellable = true)
    private static void merge(ItemEntity p_32023_, ItemStack p_32024_, ItemStack p_32025_, CallbackInfo ci) {
        ItemStack itemstack = ItemEntity.merge(p_32024_, p_32025_, ServerConfig.LIMIT_STACK_SIZE.get() == ServerConfig.DEFAULT_MINECRAFT_SIZE ? p_32024_.getMaxStackSize() : ServerConfig.LIMIT_STACK_SIZE.get());
        p_32023_.setItem(itemstack);

        ci.cancel();
    }
}
