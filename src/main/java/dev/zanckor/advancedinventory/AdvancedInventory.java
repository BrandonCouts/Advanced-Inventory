package dev.zanckor.advancedinventory;

import com.mojang.logging.LogUtils;
import dev.zanckor.advancedinventory.common.network.NetworkHandler;
import dev.zanckor.advancedinventory.core.config.ServerConfig;
import dev.zanckor.advancedinventory.core.registry.ItemRegistry;
import dev.zanckor.advancedinventory.core.registry.LootModifierRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import static net.minecraftforge.fml.config.ModConfig.Type.SERVER;


@Mod(AdvancedInventory.MODID)
public class AdvancedInventory {
    public static final String MODID = "advancedinventory";
    private static final Logger LOGGER = LogUtils.getLogger();

    public AdvancedInventory() {
        MinecraftForge.EVENT_BUS.register(this);
        IEventBus iEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        LOGGER.info("Loading AdvancedInventory");

        LOGGER.info("Registering network handler for AdvancedInventory");
        NetworkHandler.register();

        LOGGER.info("Registering items for AdvancedInventory");
        ItemRegistry.register(iEventBus);

        LOGGER.info("Registering loot modifiers for AdvancedInventory");
        LootModifierRegistry.register(iEventBus);

        LOGGER.info("Registering config for AdvancedInventory");
        ModLoadingContext.get().registerConfig(SERVER, ServerConfig.spec, "advancedinventory-server.toml");
    }
}
