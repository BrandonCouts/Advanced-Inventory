package dev.zanckor.advancedinventory.core.data.capability;


import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@AutoRegisterCapability
public class PlayerInventoryDataProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static final Capability<PlayerInventoryData> PLAYER_DATA_CAPABILITY = CapabilityManager.get(new CapabilityToken<PlayerInventoryData>() {
    });
    private PlayerInventoryData PlayerInventoryData = null;
    private final LazyOptional<PlayerInventoryData> optional = LazyOptional.of(this::createData);

    private PlayerInventoryData createData() {
        if (PlayerInventoryData == null) {
            PlayerInventoryData = new PlayerInventoryData();
        }

        return PlayerInventoryData;
    }

    public static PlayerInventoryData getPlayer(Player player) {
        LazyOptional<PlayerInventoryData> playerData = player.getCapability(PLAYER_DATA_CAPABILITY, null);
        return playerData.orElse(null);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == PLAYER_DATA_CAPABILITY) {
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return createData().serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag compoundTag) {
        createData().deserializeNBT(compoundTag);
    }
}
