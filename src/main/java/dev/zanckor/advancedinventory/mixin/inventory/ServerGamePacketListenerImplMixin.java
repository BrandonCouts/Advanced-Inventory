package dev.zanckor.advancedinventory.mixin.inventory;

import dev.zanckor.advancedinventory.core.config.ServerConfig;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import java.util.Objects;

/*
 * This mixin has been modified from the original version.
 * The original version can be found here: https://codeberg.org/PORTB/BiggerStacks/src/branch/1.18/src/main/java/portb/biggerstacks/mixin/vanilla/stacksize/ServerGamePacketListenerImplMixin.java
 */

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {

    /**
     * Removes the hard coded limit to disallow giving more than 64 items in creative mode.
     */
    @ModifyConstant(method = "handleSetCreativeModeSlot", constant = @Constant(intValue = 64))
    private int increaseStackLimit(int value) {
        return ServerConfig.LIMIT_STACK_SIZE.get() == ServerConfig.DEFAULT_MINECRAFT_SIZE ? 64 : ServerConfig.LIMIT_STACK_SIZE.get();
    }
}