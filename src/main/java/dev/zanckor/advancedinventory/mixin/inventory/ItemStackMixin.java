package dev.zanckor.advancedinventory.mixin.inventory;

import dev.zanckor.advancedinventory.core.config.ServerConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
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



    /**
     * Saves the stack size as an int instead of a byte.
     * Attempts to maintain some vanilla compatibility
     */
    @Redirect(method = "save",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompoundTag;putByte(Ljava/lang/String;B)V"))
    private void saveBigStack(CompoundTag tag, String key, byte p_128346_) {
        int count = ((ItemStack) (Object) this).getCount();

        tag.putByte("Count", (byte) Math.min(count, Byte.MAX_VALUE));

        if (count > Byte.MAX_VALUE)
            tag.putInt("BigCount", count);
    }

    /**
     * Reads the stack size as an int instead of a byte
     * Attempts to maintain some vanilla compatibility
     */
    @SuppressWarnings("DataFlowIssue")
    @Redirect(method = "<init>(Lnet/minecraft/nbt/CompoundTag;)V", at = @At(value = "FIELD", target = "Lnet/minecraft/world/item/ItemStack;count:I", opcode = Opcodes.PUTFIELD))
    private void readBigStack(ItemStack instance, int value, CompoundTag tag) {

        if (tag.contains("BigCount"))
            instance.setCount(tag.getInt("BigCount"));
        else if (tag.getTagType("Count") == Tag.TAG_INT)
            instance.setCount(tag.getInt("Count"));
        else
            instance.setCount(tag.getByte("Count"));
    }
}
