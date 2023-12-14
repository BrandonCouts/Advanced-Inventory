package dev.zanckor.advancedinventory.common.network.packet;

import dev.zanckor.advancedinventory.common.network.handler.ServerHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class SearchItem {
    String text;

    public SearchItem(String text) {
        this.text = text;
    }

    public void encodeBuffer(FriendlyByteBuf buffer) {
        buffer.writeUtf(text);
    }

    public SearchItem(FriendlyByteBuf buffer) {
        this.text = buffer.readUtf();
    }


    public static void handler(SearchItem msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerHandler.searchItemText(msg.text, Objects.requireNonNull(ctx.get().getSender()));
        });

        ctx.get().setPacketHandled(true);
    }
}
