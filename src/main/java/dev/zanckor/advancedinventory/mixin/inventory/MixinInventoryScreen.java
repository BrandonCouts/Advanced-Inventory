package dev.zanckor.advancedinventory.mixin.inventory;


import dev.zanckor.advancedinventory.client.screen.widget.ScrollableInventoryButton;
import dev.zanckor.advancedinventory.common.network.SendPacket;
import dev.zanckor.advancedinventory.common.network.packet.SearchItem;
import dev.zanckor.advancedinventory.core.config.ServerConfig;
import dev.zanckor.advancedinventory.core.data.InventoryData;
import dev.zanckor.advancedinventory.core.inventory.slot.AvailableSlot;
import dev.zanckor.advancedinventory.core.inventory.slot.SearchSlot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static dev.zanckor.advancedinventory.AdvancedInventory.MODID;
import static net.minecraft.client.gui.screens.inventory.InventoryScreen.renderEntityInInventoryFollowsMouse;

@Mixin(InventoryScreen.class)
public abstract class MixinInventoryScreen extends EffectRenderingInventoryScreen<InventoryMenu> implements RecipeUpdateListener {
    @Shadow
    private float xMouse;
    @Shadow
    private float yMouse;
    @Shadow @Final private RecipeBookComponent recipeBookComponent;
    private static final int IMAGE_WIDTH = 275;
    private static final int IMAGE_HEIGHT = 184;
    private EditBox editBox;

    private boolean showSearchScreen = false;
    private boolean isSearching = false;
    private static final int DELETE_KEY = 259;


    public MixinInventoryScreen(InventoryMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
    }

    @Inject(method = "init()V", at = @At("TAIL"))
    public void init(CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance();
        editBox = new EditBox(minecraft.font, leftPos + 185, topPos + 4,
                69, 11,
                Component.literal("Item name"));

        addRenderableWidget(new ScrollableInventoryButton(leftPos + 172, topPos + 83, 4, 11, getMenu()));
        addRenderableWidget(editBox);
        editBox.setVisible(showSearchScreen);

        addWidget(Button.builder(Component.empty(), (button) -> {
            int extraSlotSize = ServerConfig.DEFAULT_ROW_SIZE.get() * 9;
            int startIndex = InventoryData.getExtraInvSlotStart() + extraSlotSize;
            int endIndex = startIndex + 15;
            showSearchScreen = !showSearchScreen;

            changeSlotVisibility(startIndex, endIndex);
            editBox.setVisible(showSearchScreen);
        }).bounds(leftPos + 180, topPos + 76, 18, 18).build());
    }

    @Override
    public boolean mouseClicked(double x, double y, int clickType) {
        super.mouseClicked(x, y, clickType);
        isSearching = editBox.isMouseOver(x, y);

        if(!editBox.mouseClicked(x, y, clickType)) {
            clearWidgets();
            init();
        }

        return true;
    }

    @Override
    public boolean keyPressed(int keyInt, int p_97766_, int p_97767_) {
        if(isSearching){
            if(keyInt == DELETE_KEY && editBox.getValue().length() > 0){
                editBox.setValue(editBox.getValue().substring(0, editBox.getValue().length() - 1));
            }

            SendPacket.TO_SERVER(new SearchItem(editBox.getValue()));
            return true;
        }

        return super.keyPressed(keyInt, p_97766_, p_97767_);
    }


    @Inject(method = "renderBg", at = @At("TAIL"))
    protected void renderBg(GuiGraphics graphics, float f, int g, int h, CallbackInfo ci) {
        graphics.blit(getInventoryScreenTexture(), leftPos, topPos, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, IMAGE_WIDTH, IMAGE_HEIGHT);

        if (minecraft != null && minecraft.player != null) {
            renderEntityInInventoryFollowsMouse(graphics, leftPos + 51, topPos + 75,
                    30, (float) (leftPos + 51) - xMouse, (float) (topPos + 75 - 50) - yMouse, this.minecraft.player);
        }
    }

    private ResourceLocation getInventoryScreenTexture() {
        return showSearchScreen ? new ResourceLocation(MODID, "textures/gui/container/inventory.png") : new ResourceLocation(MODID, "textures/gui/container/closed_inventory.png");
    }

    private void changeSlotVisibility(int slotStart, int slotEnd){
        // In case that the button is hidden, change visibility of extraArmorSlots to notActive
        if (minecraft != null && minecraft.player != null) {
            NonNullList<Slot> slots = minecraft.player.inventoryMenu.slots;

            for(int slotIndex = slotStart; slotIndex < slotEnd; slotIndex++){
                Slot slot = slots.get(slotIndex);

                if(slot instanceof SearchSlot searchSlot){
                    searchSlot.setAvailable(showSearchScreen);
                }
            }
        }
    }
}
