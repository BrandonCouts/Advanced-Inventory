package dev.zanckor.advancedinventory.common.network.packet;

import dev.zanckor.advancedinventory.common.network.handler.ServerHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class MoveSlot {
    int movementAmount;

    public MoveSlot(int movementAmount) {
        this.movementAmount = movementAmount;
    }

    public void encodeBuffer(FriendlyByteBuf buffer) {
        buffer.writeInt(movementAmount);
    }

    public MoveSlot(FriendlyByteBuf buffer) {
        this.movementAmount = buffer.readInt();
    }


    public static void handler(MoveSlot msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerHandler.translateInventorySlots(msg.movementAmount, Objects.requireNonNull(ctx.get().getSender()));
        });

        ctx.get().setPacketHandled(true);
    }
}
