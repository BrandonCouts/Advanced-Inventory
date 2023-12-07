package dev.zanckor.advancedinventory;

import com.mojang.logging.LogUtils;
import dev.zanckor.advancedinventory.common.network.NetworkHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;


@Mod(AdvancedInventory.MODID)
public class AdvancedInventory {
    public static final String MODID = "advancedinventory";
    private static final Logger LOGGER = LogUtils.getLogger();

    public AdvancedInventory() {
        MinecraftForge.EVENT_BUS.register(this);

        LOGGER.info("Loading AdvancedInventory");

        LOGGER.info("Registering network handler for AdvancedInventory");
        NetworkHandler.register();
    }
}
