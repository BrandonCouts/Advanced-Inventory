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
    @Shadow @Final private List<NonNullList<ItemStack>> compartments;
    @Shadow @Final public NonNullList<ItemStack> items;
    @Shadow @Final public NonNullList<ItemStack> armor;
    @Shadow @Final public NonNullList<ItemStack> offhand;
    @Shadow @Final public Player player;
    public NonNullList<ItemStack> extraSlots;


    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(Player player, CallbackInfo ci) {
        extraSlots = NonNullList.withSize(900, ItemStack.EMPTY);
        compartments = ImmutableList.of(items, armor, offhand, extraSlots);
    }

    @Inject(method = "setItem", at = @At("TAIL"))
    public void setItem(int slot, ItemStack itemStack, CallbackInfo ci) {
        if(slot >= 41) {
            extraSlots.set(slot, itemStack);
        }
    }

    @Inject(method = "save", at = @At("HEAD"))
    private void save(ListTag listTag, CallbackInfoReturnable<ListTag> cir) {
        for (int slotIndex = 0, extraSlotSize = extraSlots.size(); slotIndex < extraSlotSize; slotIndex++) {
            ItemStack itemStack = extraSlots.get(slotIndex);

            if (!itemStack.isEmpty()) {
                CompoundTag compoundTag = new CompoundTag();
                compoundTag.putByte("Slot", (byte) (slotIndex + 200));

                extraSlots.get(slotIndex).save(compoundTag);
                listTag.add(compoundTag);
            }
        }
    }

    @Inject(method = "load", at = @At("HEAD"))
    private void load(ListTag listTag, CallbackInfo ci) {
        extraSlots.clear();

        for (int listTagIndex = 0; listTagIndex < listTag.size(); listTagIndex++) {
            CompoundTag compoundTag = listTag.getCompound(listTagIndex);
            int slot = compoundTag.getByte("Slot") & 255;
            ItemStack itemStack = ItemStack.of(compoundTag);

            if (!itemStack.isEmpty()) {
                if (slot >= 200 && slot < extraSlots.size() + 200) {
                    extraSlots.set(slot - 200, itemStack);
                }
            }
        }
    }
}
