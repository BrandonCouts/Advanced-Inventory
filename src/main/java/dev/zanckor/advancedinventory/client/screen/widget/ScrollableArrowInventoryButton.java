package dev.zanckor.advancedinventory.client.screen.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

import static dev.zanckor.advancedinventory.AdvancedInventory.MODID;

public class ScrollableArrowInventoryButton extends Button {
    private static final Button.CreateNarration DEFAULT_NARRATION = Supplier::get;
    private static final ResourceLocation DOWNSIDE_ARROW = new ResourceLocation(MODID, "textures/gui/button/downside_arrow.png");
    private static final ResourceLocation UPSIDE_ARROW = new ResourceLocation(MODID, "textures/gui/button/upside_arrow.png");
    private static final int imageWidth = 6;
    private static final int imageHeight = 8;
    private boolean isUpArrow;

    public ScrollableArrowInventoryButton(int x, int y, int width, int height, OnPress onPress, boolean isUpArrow) {
        super(x, y, width, height, Component.nullToEmpty(""), onPress, DEFAULT_NARRATION);

        this.isUpArrow = isUpArrow;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        guiGraphics.blit(isUpArrow ? UPSIDE_ARROW : DOWNSIDE_ARROW, this.getX(), this.getY(), 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
    }
}
