package dev.zanckor.advancedinventory;

import dev.zanckor.advancedinventory.core.registry.ItemRegistry;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static dev.zanckor.advancedinventory.AdvancedInventory.LOGGER;
import static dev.zanckor.advancedinventory.AdvancedInventory.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {

    @SubscribeEvent
    public static void setup(final FMLClientSetupEvent event) {
        LOGGER.info("Setup AdvancedInventory");
        event.enqueueWork(() -> {
            ItemProperties.register(ItemRegistry.CAPTURE_MOB.get(), new ResourceLocation(MODID, "mob_captured"),
                    (stack, world, entity, id) -> stack.getOrCreateTagElement("entity").contains("EntityTag") ? 1.0F : 0.0F);
        });
    }
}
