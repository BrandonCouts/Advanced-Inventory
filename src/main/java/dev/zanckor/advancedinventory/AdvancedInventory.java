package dev.zanckor.advancedinventory;

import com.mojang.logging.LogUtils;
import dev.zanckor.advancedinventory.core.config.ServerConfig;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

import static dev.zanckor.advancedinventory.AdvancedInventory.LOGGER;
import static dev.zanckor.advancedinventory.AdvancedInventory.MODID;
import static net.minecraftforge.fml.config.ModConfig.Type.SERVER;


@Mod(MODID)
public class AdvancedInventory {
    public static final String MODID = "advanced_inventory";
    public static final Logger LOGGER = LogUtils.getLogger();

    public AdvancedInventory() {
        LOGGER.info("Loading AdvancedInventory");

        ModLoadingContext.get().registerConfig(SERVER, ServerConfig.spec, "advancedinventory-server.toml");
    }
}