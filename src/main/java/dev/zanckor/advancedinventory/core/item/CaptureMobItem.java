package dev.zanckor.advancedinventory.core.item;

import dev.zanckor.advancedinventory.core.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CaptureMobItem extends Item {
    public CaptureMobItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        BlockPos pos = context.getClickedPos();
        if(!stack.getOrCreateTagElement("entity").contains("EntityTag")) {
            return InteractionResult.FAIL;
        } else {
            EntityType<?> entityType = EntityType.byString(stack.getOrCreateTagElement("entity").getString("Type")).orElseGet(() -> EntityType.PIG);
            Entity entity = entityType.create(context.getLevel());
            entity.load(stack.getOrCreateTagElement("entity").getCompound("EntityTag"));
            entity.moveTo(pos.getX(), pos.getY() + 1, pos.getZ());
            context.getLevel().addFreshEntity(entity);

            stack.getOrCreateTagElement("entity").remove("EntityTag");
        }

        return super.onItemUseFirst(stack, context);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if(stack.getOrCreateTagElement("entity").contains("EntityTag")) {
            return false;
        }



        entity.remove(Entity.RemovalReason.DISCARDED);
        player.getMainHandItem().setCount(player.getMainHandItem().getCount() - 1);

        //Create a CaptureMobItem with the entity's type
        ItemStack captureMobItem = ItemRegistry.CAPTURE_MOB.get().getDefaultInstance();

        captureMobItem.getOrCreateTagElement("entity").put("EntityTag", entity.saveWithoutId(new CompoundTag()));
        captureMobItem.getOrCreateTagElement("entity").putString("Name", entity.getName().getString());
        captureMobItem.getOrCreateTagElement("entity").putString("Type", EntityType.getKey(entity.getType()).toString());

        player.addItem(captureMobItem);
        return super.onLeftClickEntity(stack, player, entity);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        if(itemStack.getOrCreateTagElement("entity").contains("EntityTag")) {
            components.add(Component.literal("Right click to spawn " + itemStack.getOrCreateTagElement("entity").getString("Name")));
        } else {
            components.add(Component.literal("Left click to capture entity"));
        }

        super.appendHoverText(itemStack, level, components, tooltipFlag);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return stack.getOrCreateTagElement("entity").contains("EntityTag") ? 1 : super.getMaxStackSize(stack);
    }
}
