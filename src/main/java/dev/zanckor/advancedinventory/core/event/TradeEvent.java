package dev.zanckor.advancedinventory.core.event;

import dev.zanckor.advancedinventory.core.registry.ItemRegistry;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

import static dev.zanckor.advancedinventory.AdvancedInventory.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TradeEvent {

    @SubscribeEvent
    public static void addCustomTrade(VillagerTradesEvent e) {
        if (e.getType().equals(VillagerProfession.LEATHERWORKER)) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = e.getTrades();

            // Get == Level
            trades.get(3).add((entity, random) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 32),
                    new ItemStack(ItemRegistry.SLOT_INCREASER.get(), 1),
                    3, 8, 0.05F // Uses, XP, priceMultiplier
            ));
        }
    }
}

