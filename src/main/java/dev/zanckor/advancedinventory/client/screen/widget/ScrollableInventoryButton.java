package dev.zanckor.advancedinventory.client.screen.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import org.jetbrains.annotations.NotNull;

import static dev.zanckor.advancedinventory.AdvancedInventory.MODID;

public class ScrollableInventoryButton extends AbstractButton {
    private static final ResourceLocation BUTTON = new ResourceLocation(MODID, "textures/gui/button/scroll_button.png");
    private static final int imageWidth = 4;
    private static final int imageHeight = 11;
    private final InventoryMenu MENU;


    public ScrollableInventoryButton(int x, int y, int width, int height, InventoryMenu menu) {
        super(x, y, width, height, Component.nullToEmpty(""));

        this.MENU = menu;
    }

    @Override @SuppressWarnings("all")
    public boolean mouseScrolled(double xPos, double yPos, double scrollType) {
        final int UP_SCROLL = 1;
        final int DOWN_SCROLL = -1;

        if(MENU != null){
            switch ((int) scrollType) {
                case UP_SCROLL -> MENU.clickMenuButton(null, 1);
                case DOWN_SCROLL -> MENU.clickMenuButton(null, -1);
            }
        }

        return super.mouseScrolled(xPos, yPos, scrollType);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        guiGraphics.blit(BUTTON, this.getX(), this.getY(), 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
    }

    @Override
    public void onPress() {
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {

    }
}
