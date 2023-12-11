package dev.zanckor.advancedinventory.mixin.inventory;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;

@Mixin(GuiGraphics.class)
public abstract class TextItemRendererMixin {
    @Shadow @Final private PoseStack pose;

    @Shadow public abstract int drawString(Font p_282003_, @Nullable String p_281403_, int p_282714_, int p_282041_, int p_281908_);

    private static final DecimalFormat BILLION_FORMAT  = new DecimalFormat("#.#B");
    private static final DecimalFormat MILLION_FORMAT  = new DecimalFormat("#.#M");
    private static final DecimalFormat THOUSAND_FORMAT = new DecimalFormat("#.#K");
    private static final double ONE_BILLION = 1000000000;
    private static final double ONE_MILLION = 1000000;
    private static final double ONE_THOUSAND = 1000;

    @Inject(method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At("HEAD"), cancellable = true)
    public void renderItemDecorations(Font font, ItemStack itemStack, int p_282641_, int p_282146_, @Nullable String s, CallbackInfo ci) {
        var value = itemStack.getCount();

        String formattedInt = value > 1 ? String.valueOf(Mth.floor(value)) : "";

        if (value >= ONE_BILLION)
            formattedInt = BILLION_FORMAT.format(value / ONE_BILLION);
        else if (value >= ONE_MILLION)
            formattedInt = MILLION_FORMAT.format(value / ONE_MILLION);
        else if (value >= ONE_THOUSAND)
            formattedInt = THOUSAND_FORMAT.format(value / ONE_THOUSAND);

        this.pose.pushPose();
        this.pose.translate(0.0D, 0.0D, 200.0D);
        drawString(font, formattedInt, p_282641_ + 19 - 2 - font.width(formattedInt), p_282146_ + 6 + 3, 16777215);
        this.pose.popPose();

        ci.cancel();
    }
}
