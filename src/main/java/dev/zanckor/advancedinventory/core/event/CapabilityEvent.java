package dev.zanckor.advancedinventory.core.event;

import dev.zanckor.advancedinventory.core.data.capability.PlayerInventoryDataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static dev.zanckor.advancedinventory.AdvancedInventory.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CapabilityEvent {
    public static final String PLAYER_DATA_CAPABILITY = MODID + "player_capability";

    @SubscribeEvent
    public static void addCapabilityPlayer(AttachCapabilitiesEvent<Entity> e) {
        PlayerInventoryDataProvider capability = new PlayerInventoryDataProvider();
        e.addCapability(new ResourceLocation(PLAYER_DATA_CAPABILITY), capability);
    }


    @SubscribeEvent @SuppressWarnings("all")
    public static void onPlayerCloned(PlayerEvent.Clone e) {
        Player player = e.getEntity();

        if (e.isWasDeath()) {
            e.getOriginal().reviveCaps();
            e.getOriginal().getCapability(PlayerInventoryDataProvider.PLAYER_DATA_CAPABILITY).
                    ifPresent(oldStore -> player.getCapability(PlayerInventoryDataProvider.PLAYER_DATA_CAPABILITY).ifPresent(newStore -> newStore.copyForRespawn(oldStore)));

            e.getOriginal().invalidateCaps();

            GameRules gameRules = player.level().getGameRules();
            keepInventory(gameRules.getBoolean(GameRules.RULE_KEEPINVENTORY), player, e.getOriginal());
        }
    }

    public static void keepInventory(boolean keepInventory, Player player, Player oldPlayer) {
        if(!keepInventory) return;

        player.containerMenu.slots.forEach(slot -> {
            slot.set(oldPlayer.getInventory().getItem(slot.getSlotIndex()));
        });

        player.inventoryMenu.clearCraftingContent(); // Some slots move to crafting slots, so we need to clear them
    }
}

