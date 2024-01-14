package dev.zanckor.advancedinventory.core.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig {
    private static final String CATEGORY_GENERAL = "general";
    private static final String LIMIT_ROWS_KEY = "limitRows";
    private static final String LIMIT_STACK_SIZE_KEY = "limitStackSize";
    private static final String DEFAULT_ROW_SIZE_KEY = "defaultRowSize";

    public static final int DEFAULT_MINECRAFT_SIZE = -1;

    private static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec.IntValue LIMIT_ROWS;
    public static final ForgeConfigSpec.IntValue LIMIT_STACK_SIZE;
    public static final ForgeConfigSpec.IntValue DEFAULT_ROW_SIZE;

    public static ForgeConfigSpec spec;

    static {
        builder.push("AdvancedInventory");
        builder.comment("Advanced Inventory configuration").push(CATEGORY_GENERAL);

        LIMIT_ROWS = builder
                .comment("Limit the amount of rows a player can have in their inventory.")
                .defineInRange(LIMIT_ROWS_KEY, 900, 4, Integer.MAX_VALUE);

        LIMIT_STACK_SIZE = builder
                .comment("Limit the stack size of items in the extra inventory slots.")
                .defineInRange(LIMIT_STACK_SIZE_KEY, DEFAULT_MINECRAFT_SIZE, -1, Integer.MAX_VALUE);

        DEFAULT_ROW_SIZE = builder
                .comment("The default amount of slots in a row.")
                .defineInRange(DEFAULT_ROW_SIZE_KEY, 6, 1, Integer.MAX_VALUE);


        builder.pop();
        spec = builder.build();
    }
}
