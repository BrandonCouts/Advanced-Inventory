package dev.zanckor.advancedinventory.mixin.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = InventoryScreen.class, priority = 2000)
public abstract class InventoryScreenTextureFixMixin extends EffectRenderingInventoryScreen<InventoryMenu> {

    @Shadow @Final
    private static ResourceLocation INVENTORY_LOCATION; // from AbstractContainerScreen

    @Shadow protected float xMouse;
    @Shadow protected float yMouse;

    // The vanilla method is static, but keeping this shadow avoids compile issues across versions
    @Shadow(remap = false)
    protected abstract void renderEntityInInventoryFollowsMouse(GuiGraphics guiGraphics, int x, int y, int size, float mouseX, float mouseY, net.minecraft.world.entity.LivingEntity entity);

    protected InventoryScreenTextureFixMixin(InventoryMenu menu, Inventory playerInv, net.minecraft.network.chat.Component title) {
        super(menu, playerInv, title);
    }

    /**
     * Use vanilla inventory background texture to avoid missing resource issues.
     */
    @Overwrite
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(INVENTORY_LOCATION, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        InventoryScreen.renderEntityInInventoryFollowsMouse(graphics, this.leftPos + 51, this.topPos + 75, 30,
                (float)(this.leftPos + 51) - this.xMouse, (float)(this.topPos + 75 - 50) - this.yMouse,
                Minecraft.getInstance().player);
    }
}
