package dev.zanckor.advancedinventory.mixin.inventory;


import com.google.common.collect.ImmutableList;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Inventory.class)
public class MixinInventoryContainer {

    @Mutable
    @Shadow
    @Final
    private List<NonNullList<ItemStack>> compartments;
    @Shadow
    @Final
    public NonNullList<ItemStack> items;
    @Shadow
    @Final
    public NonNullList<ItemStack> armor;
    @Shadow
    @Final
    public NonNullList<ItemStack> offhand;
    public NonNullList<ItemStack> extraSlot;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(Player player, CallbackInfo ci) {
        extraSlot = NonNullList.withSize(929, ItemStack.EMPTY); // TODO: Change 900 with config
        compartments = ImmutableList.of(items, armor, offhand, extraSlot);
    }

    @Inject(method = "save", at = @At("TAIL"))
    private void save(ListTag listTag, CallbackInfoReturnable<ListTag> cir) {
        for (int slotIndex = 0, extraSlot = this.extraSlot.size(); slotIndex < extraSlot; slotIndex++) {
            ItemStack itemStack = this.extraSlot.get(slotIndex);

            if (!itemStack.isEmpty()) {
                CompoundTag compoundTag = new CompoundTag();
                compoundTag.putByte("Slot", (byte) (slotIndex + 200));

                this.extraSlot.get(slotIndex).save(compoundTag);
                listTag.add(compoundTag);
            }
        }
    }

    @Inject(method = "load", at = @At("TAIL"))
    private void load(ListTag listTag, CallbackInfo ci) {
        extraSlot.clear();

        for (int listTagIndex = 0; listTagIndex < listTag.size(); listTagIndex++) {
            CompoundTag compoundTag = listTag.getCompound(listTagIndex);
            int slot = compoundTag.getByte("Slot") & 255;
            ItemStack itemStack = ItemStack.of(compoundTag);

            if (!itemStack.isEmpty()) {
                if (slot >= 200 && slot < extraSlot.size() + 200) {
                    extraSlot.set(slot - 200, itemStack);
                }
            }
        }
    }
}