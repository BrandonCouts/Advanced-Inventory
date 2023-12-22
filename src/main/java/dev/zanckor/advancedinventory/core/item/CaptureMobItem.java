package dev.zanckor.advancedinventory.core.item;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

public class CaptureMobItem extends Item {
    private CompoundTag entityData = null;

    public CaptureMobItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        System.out.println("A");
        BlockPos pos = context.getClickedPos();

        EntityType.loadEntityRecursive(entityData, context.getLevel(), (entity) -> {
            context.getLevel().addFreshEntity(entity);
            entity.moveTo(pos.getX(), pos.getY(), pos.getZ());

            return entity;
        });

        entityData = null;

        return super.onItemUseFirst(stack, context);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        //createNewStack(entity, player);
        addEntity(entity);


        return super.onLeftClickEntity(stack, player, entity);
    }

    public void createNewStack(Entity entity, Player player) {
        ItemStack stack = new ItemStack(this.getDefaultInstance().getItem());
        stack.setHoverName(Component.literal("Captured Entity: " + entity.getDisplayName().getString()));
        ((CaptureMobItem) stack.getItem()).addEntity(entity);

        player.addItem(stack);
    }

    public void addEntity(Entity entity) {
        entity.save(entityData = new CompoundTag());
        entity.remove(Entity.RemovalReason.DISCARDED);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return entityData == null ? 64 : 1;
    }
}
