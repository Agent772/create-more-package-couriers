package com.krei.cmpackagecouriers;

import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

public class ServerConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue PLANE_LOCATION_TARGETS = BUILDER
            .comment("enables targeting depots with cardboard planes")
            .define("enablePlaneLocationLogistics", true);

    private static final ModConfigSpec.BooleanValue PLANE_PLAYER_TARGETS = BUILDER
            .comment("enables targeting players with cardboard planes")
            .define("enablePlanePlayerLogistics", true);

    private static final ModConfigSpec.BooleanValue SHOP_ADDRESS_REPLACEMENT = BUILDER
        .comment("enables integration with Create's Shop system that rewrites @player addresses to the ordering player's nick)")
        .define("enableShopAddressReplacement", true);

    private static final ModConfigSpec.BooleanValue LOCATION_TRANSMITTER_NEEDED = BUILDER
            .comment("if true, cardboard planes will only be able to target players if they got a enabled location transmitter in their inventory or curios slots")
            .define("locationTransmitterNeededForPlayerTargeting", true);

    private static final ModConfigSpec.IntValue TIMED_ENABLE_TIME = BUILDER
            .comment("this sets the time (in seconds) a location transmitter remains enabled after being toggled on via another component (portable stock ticker)")
            .defineInRange("locationTransmitterTimedEnableTime", 60, 1, 600);


    static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean planeLocationTargets;
    public static boolean planePlayerTargets;
    public static boolean shopAddressReplacement;
    public static boolean locationTransmitterNeeded;
    public static int timedEnableTime;

    static void onLoad(final ModConfigEvent.Loading event) {
        planeLocationTargets = PLANE_LOCATION_TARGETS.get();
        planePlayerTargets = PLANE_PLAYER_TARGETS.get();
        shopAddressReplacement = SHOP_ADDRESS_REPLACEMENT.get();
        locationTransmitterNeeded = LOCATION_TRANSMITTER_NEEDED.get();
        timedEnableTime = TIMED_ENABLE_TIME.get();
    }

    static void onReload(final ModConfigEvent.Reloading event) {
        planeLocationTargets = PLANE_LOCATION_TARGETS.get();
        planePlayerTargets = PLANE_PLAYER_TARGETS.get();
        shopAddressReplacement = SHOP_ADDRESS_REPLACEMENT.get();
        locationTransmitterNeeded = LOCATION_TRANSMITTER_NEEDED.get();
        timedEnableTime = TIMED_ENABLE_TIME.get();
    }
}