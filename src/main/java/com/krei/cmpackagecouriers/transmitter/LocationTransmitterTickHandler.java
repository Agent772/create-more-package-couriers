package com.krei.cmpackagecouriers.transmitter;

import com.krei.cmpackagecouriers.PackageCouriers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

/**
 * Handles server tick events for location transmitter timer functionality.
 * Automatically disables transmitters when their timed enable duration expires.
 */
@EventBusSubscriber(modid = PackageCouriers.MODID)
public class LocationTransmitterTickHandler {

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        // Handle location transmitter timers for all players
        long worldTime = event.getServer().overworld().getGameTime();
        
        for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {
            ItemStack locationTransmitter = LocationTransmitterHelper.getLocationTransmitter(player);
            if (locationTransmitter != null && !locationTransmitter.isEmpty()) {
                LocationTransmitterItem.tickCheckTimer(locationTransmitter, player, worldTime);
            }
        }
    }
}