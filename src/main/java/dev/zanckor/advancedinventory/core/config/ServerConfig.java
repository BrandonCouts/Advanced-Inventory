package dev.zanckor.advancedinventory.core.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig {
    private static final String CATEGORY_GENERAL = "general";
    private static final String LIMIT_STACK_SIZE_KEY = "limitStackSize";

    public static final int DEFAULT_MINECRAFT_SIZE = -1;

    private static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec.IntValue LIMIT_STACK_SIZE;


    public static ForgeConfigSpec spec;

    static {
        builder.push("AdvancedInventory");
        builder.comment("Advanced Inventory configuration").push(CATEGORY_GENERAL);

        LIMIT_STACK_SIZE = builder
                .comment("Limit the stack size of items in the extra inventory slots. (-1 to vanilla default size)")
                .defineInRange(LIMIT_STACK_SIZE_KEY, 50000, -1, Integer.MAX_VALUE);


        builder.pop();
        spec = builder.build();
    }
}
