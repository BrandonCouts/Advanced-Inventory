package dev.zanckor.advancedinventory.common.network;

import dev.zanckor.advancedinventory.common.network.packet.MoveSlot;
import dev.zanckor.advancedinventory.common.network.packet.SearchItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import static dev.zanckor.advancedinventory.AdvancedInventory.MODID;

public class NetworkHandler {

    private static final String PROTOCOL_VERSION = "1.0";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MODID, "advancedinventory"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        int index = -1;

        CHANNEL.messageBuilder(MoveSlot.class, ++index, NetworkDirection.PLAY_TO_SERVER)
                .encoder(MoveSlot::encodeBuffer).decoder(MoveSlot::new)
                .consumerNetworkThread(MoveSlot::handler).add();

        CHANNEL.messageBuilder(SearchItem.class, ++index, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SearchItem::encodeBuffer).decoder(SearchItem::new)
                .consumerNetworkThread(SearchItem::handler).add();
    }
}
