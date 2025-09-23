package com.krei.cmpackagecouriers.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class PackageCouriersConfig {
    
    public static final ModConfigSpec COMMON_SPEC;
    public static final Common COMMON;
    
    static {
        ModConfigSpec.Builder commonBuilder = new ModConfigSpec.Builder();
        COMMON = new Common(commonBuilder);
        COMMON_SPEC = commonBuilder.build();
    }
    
    public static class Common {
        public final ModConfigSpec.BooleanValue enableDepotSignTargeting;
        
        public Common(ModConfigSpec.Builder builder) {
            builder.comment("Package Courier Settings")
                   .push("general");
                   
            enableDepotSignTargeting = builder
                .comment("Allow packages to be sent to depots with signs. If disabled, packages can only be sent to players.")
                .define("enableDepotSignTargeting", true);
                
            builder.pop();
        }
    }
}