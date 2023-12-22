package dev.zanckor.advancedinventory.core.registry;

import dev.zanckor.advancedinventory.core.item.CaptureMobItem;
import dev.zanckor.advancedinventory.core.item.SlotIncreaser;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static dev.zanckor.advancedinventory.AdvancedInventory.MODID;

@SuppressWarnings("unused")
public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final RegistryObject<Item> SLOT_INCREASER = ITEMS.register("slot_increaser",
            () -> new SlotIncreaser(new Item.Properties().stacksTo(64)));

    public static final RegistryObject<Item> CAPTURE_MOB = ITEMS.register("capture_mob",
            () -> new CaptureMobItem(new Item.Properties().stacksTo(64)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
