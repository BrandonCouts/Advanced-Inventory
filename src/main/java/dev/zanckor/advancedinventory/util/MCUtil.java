package dev.zanckor.advancedinventory.util;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class MCUtil {

    public static void sendPlayerMessage(Player player, String message) {
        player.sendSystemMessage(Component.literal(message));
    }

}
