package dev.zanckor.advancedinventory.mixin.inventory;

import dev.zanckor.advancedinventory.common.network.SendPacket;
import dev.zanckor.advancedinventory.common.network.packet.MoveSlot;
import dev.zanckor.advancedinventory.core.config.ServerConfig;
import dev.zanckor.advancedinventory.core.data.InventoryData;
import dev.zanckor.advancedinventory.core.inventory.slot.AvailableSlot;
import dev.zanckor.advancedinventory.core.inventory.slot.SearchSlot;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryMenu.class)
public abstract class MixinInventoryMenu extends RecipeBookMenu<CraftingContainer> {
    @Shadow
    @Final
    private CraftingContainer craftSlots;
    @Shadow
    @Final
    private ResultContainer resultSlots;
    @Shadow
    @Final
    private Player owner;


    @Shadow public abstract void slotsChanged(Container p_39710_);

    public MixinInventoryMenu(MenuType<?> menuType, int i) {
        super(menuType, i);
    }

    @SuppressWarnings("all")
    @Inject(method = "<init>", at = @At("TAIL"))
    public void constructor(Inventory inventory, boolean bl, Player player, CallbackInfo ci) {
        int startIndexHotbar = InventoryData.getExtraInvSlotStart() - 9;
        int extraSlotSize = ServerConfig.DEFAULT_ROW_SIZE.get() * 9;

        for (int extraHotbarSlot = 0; extraHotbarSlot < 9; ++extraHotbarSlot) {
            int xPos = 8 + extraHotbarSlot * 18;
            int yPos = 160;

            addSlot(new AvailableSlot(inventory, startIndexHotbar + extraHotbarSlot, xPos, yPos, true, player));
        }

        for (int slot = 0; slot < extraSlotSize; ++slot) {
            boolean isAvailable = slot < ServerConfig.DEFAULT_ROW_SIZE.get();
            int slotIndex = InventoryData.getExtraInvSlotStart() + slot;
            AvailableSlot availableSlot = new AvailableSlot(inventory, slotIndex, -1000000, -1000000, isAvailable, player);

            addSlot(availableSlot);
        }

        for (int rowSearch = 0; rowSearch < 3; ++rowSearch) {
            for (int slotSearch = 0; slotSearch < 5; ++slotSearch) {
                int xPos = 184 + slotSearch * 18;
                int yPos = rowSearch * 18 + 21;
                int slotIndex = InventoryData.getExtraInvSlotStart() + extraSlotSize + slotSearch + rowSearch * 5;

                addSlot(new SearchSlot(inventory, slotIndex, xPos, yPos));
            }
        }
    }

    @Override
    public boolean clickMenuButton(@NotNull Player player, int clickType) {
        final int UP_SCROLL = 1;
        final int DOWN_SCROLL = -1;
        int slotsTranslated = 0;

        switch (clickType) {
            case UP_SCROLL -> slotsTranslated = -9;
            case DOWN_SCROLL -> slotsTranslated = 9;
        }

        moveSlots(slotsTranslated);

        return super.clickMenuButton(player, clickType);
    }

    public void moveSlots(int amount) {
        SendPacket.TO_SERVER(new MoveSlot(amount));
    }


    @Override
    public void fillCraftSlotsStackedContents(@NotNull StackedContents stackedContents) {
        craftSlots.fillStackedContents(stackedContents);
    }

    @Override
    public void clearCraftingContent() {
        resultSlots.clearContent();
        craftSlots.clearContent();
    }

    @Override
    public boolean recipeMatches(Recipe<? super CraftingContainer> recipe) {
        return recipe.matches(this.craftSlots, owner.level());
    }

    @Override
    public int getResultSlotIndex() {
        return 0;
    }

    @Override
    public int getGridWidth() {
        return this.craftSlots.getWidth();
    }

    @Override
    public int getGridHeight() {
        return this.craftSlots.getHeight();
    }

    @Override
    public int getSize() {
        return 5;
    }

    @Override
    public @NotNull RecipeBookType getRecipeBookType() {
        return RecipeBookType.CRAFTING;
    }

    @Override
    public boolean shouldMoveToInventory(int i) {
        return i != this.getResultSlotIndex();
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int i) {

        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(i);
        if (slot.hasItem()) {
            ItemStack itemStack2 = slot.getItem();
            itemStack = itemStack2.copy();
            EquipmentSlot equipmentSlot = Mob.getEquipmentSlotForItem(itemStack);
            if (i == 0) {
                if (!this.moveItemStackTo(itemStack2, 9, 45, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemStack2, itemStack);
            } else if (i >= 1 && i < 5) {
                if (!this.moveItemStackTo(itemStack2, 9, 45, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (i >= 5 && i < 9) {
                if (!this.moveItemStackTo(itemStack2, 9, 45, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (equipmentSlot.getType() == EquipmentSlot.Type.ARMOR && !this.slots.get(8 - equipmentSlot.getIndex()).hasItem()) {
                int j = 8 - equipmentSlot.getIndex();
                if (!this.moveItemStackTo(itemStack2, j, j + 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (equipmentSlot == EquipmentSlot.OFFHAND && !this.slots.get(45).hasItem()) {
                if (!this.moveItemStackTo(itemStack2, 45, 46, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (i >= 9 && i < 36) {
                if (!this.moveItemStackTo(itemStack2, 36, 45, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (i >= 36 && i < 45) {
                if (!this.moveItemStackTo(itemStack2, 9, 36, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemStack2, 9, 45, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemStack2);
            if (i == 0) {
                player.drop(itemStack2, false);
            }
        }

        return itemStack;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }
}
