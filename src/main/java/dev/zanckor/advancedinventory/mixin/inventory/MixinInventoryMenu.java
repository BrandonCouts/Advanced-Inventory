package dev.zanckor.advancedinventory.mixin.inventory;

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
public class MixinInventoryMenu extends RecipeBookMenu<CraftingContainer> {

    @Shadow
    @Final
    private CraftingContainer craftSlots;
    @Shadow
    @Final
    private ResultContainer resultSlots;
    @Shadow
    @Final
    private Player owner;

    private int scrollAmount = 0;

    public MixinInventoryMenu(MenuType<?> menuType, int i) {
        super(menuType, i);
    }

    @SuppressWarnings("all")
    @Inject(method = "<init>", at = @At("TAIL"))
    public void constructor(Inventory inventory, boolean bl, Player player, CallbackInfo ci) {
    }

    @Override
    public boolean clickMenuButton(@NotNull Player player, int clickType) {
        final int UP_SCROLL = 1;
        final int DOWN_SCROLL = -1;
        int slotsTranslated = 0;

        switch (clickType) {
            case UP_SCROLL -> slotsTranslated = increaseScrollAmount(1) ? 9 : 0;
            case DOWN_SCROLL -> slotsTranslated = increaseScrollAmount(-1) ? -9  : 0;
        }

        moveSlots(slotsTranslated);

        return super.clickMenuButton(player, clickType);
    }

    public boolean increaseScrollAmount(int amount) {
        int finalScrollAmount = scrollAmount + amount;

        if(finalScrollAmount >= 1 && finalScrollAmount <= 3){
            scrollAmount = finalScrollAmount;
            return true;
        } else {
            return false;
        }
    }

    public void moveSlots(int amount) {
        InventoryMenu inventoryMenu = ((InventoryMenu) (Object) this);

        for(int row = 0; row < 3; ++row){
            int slot = row * 9 + 9;

            Slot previousSlot = inventoryMenu.getSlot(slot);
            Slot nextSlot = inventoryMenu.getSlot(slot + amount);

            ItemStack previousSlotItem = previousSlot.getItem();
            ItemStack nextSlotItem = nextSlot.getItem();

            nextSlot.set(previousSlotItem);
            previousSlot.set(nextSlotItem);
        }

        /*
        for (int row = 0; row < 3; ++row) {
            int index = row * 9 + 9;
            Slot slot = inventoryMenu.getSlot(index);
            inventoryMenu.getSlot((row + scrollAmount) * 9).set(slot.getItem());

            System.out.println("Index: " + index + " New Index: " + ((row + scrollAmount) * 9));
        }
         */
    }


    @Override
    public void clicked(int p_150400_, int p_150401_, ClickType p_150402_, Player p_150403_) {
        System.out.println(p_150400_);

        super.clicked(p_150400_, p_150401_, p_150402_, p_150403_);
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
