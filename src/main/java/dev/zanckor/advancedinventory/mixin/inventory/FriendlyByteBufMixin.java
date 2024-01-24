package dev.zanckor.advancedinventory.mixin.inventory;


import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FriendlyByteBuf.class)
public class FriendlyByteBufMixin {
    /**
     * This writes the item count as an int instead of a byte.
     */
    @Redirect(method = "writeItemStack",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/network/FriendlyByteBuf;writeByte(I)Lio/netty/buffer/ByteBuf;"))
    private ByteBuf writeBiggerStackCount(FriendlyByteBuf instance, int count) {
        return instance.writeInt(count);
    }

    @Redirect(method = "readItem",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/network/FriendlyByteBuf;readByte()B"))
    private byte doNothing(FriendlyByteBuf instance) {
        return 0; // do nothing, because we cannot change the return type of this method
    }
}