package dev.zanckor.advancedinventory.core.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {
    private static final String CATEGORY_GENERAL = "general";
    private static final String ENABLE_RENDER_COUNT_KEY = "enableItemRenderCount";

    private static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec.BooleanValue ENABLE_RENDER_COUNT;

    public static ForgeConfigSpec spec;

    static {
        builder.push("AdvancedInventory");
        builder.comment("Client configuration").push(CATEGORY_GENERAL);

        ENABLE_RENDER_COUNT = builder
                .comment("Enable abbreviated item count rendering on items")
                .define(ENABLE_RENDER_COUNT_KEY, true);

        builder.pop();
        spec = builder.build();
    }
}
