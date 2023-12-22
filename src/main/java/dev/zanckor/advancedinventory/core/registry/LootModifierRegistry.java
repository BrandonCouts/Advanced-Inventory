package dev.zanckor.advancedinventory.core.registry;

import com.mojang.serialization.Codec;
import dev.zanckor.advancedinventory.core.data.AddLootModifier;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static dev.zanckor.advancedinventory.AdvancedInventory.MODID;

public class LootModifierRegistry {


    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MODID);


    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADD_ITEM =
            LOOT_MODIFIERS.register("add_item", () -> AddLootModifier.CODEC.get());

    public static void register(IEventBus bus) {
        LOOT_MODIFIERS.register(bus);
    }
}
