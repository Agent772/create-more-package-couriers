package com.krei.cmpackagecouriers.transmitter;

import com.krei.cmpackagecouriers.compat.Mods;
import com.krei.cmpackagecouriers.compat.curios.CuriosCompat;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class LocationTransmitterHelper {
    
        /**
     * Checks if the player has an enabled location transmitter in their inventory or Curios slots.
     * @param player The player to check
     * @return true if the player has an enabled location transmitter, false otherwise
     */
    public static boolean hasEnabledLocationTransmitter(ServerPlayer player) {
        // Check regular inventory
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() instanceof LocationTransmitterItem && LocationTransmitterItem.isEnabled(stack)) {
                return true;
            }
        }
        
        // Check Curios slots if Curios is loaded
        if (Mods.CURIOS.isLoaded() && CuriosCompat.isCuriosLoaded()) {
            return CuriosCompat.hasEnabledLocationTransmitterInCurios(player);
        }
        
        return false;
    }

    /**
     * Finds the first location transmitter in the player's inventory or Curios slots.
     * @param player The player to search
     * @return The ItemStack containing the location transmitter, or null if none found
     */
    public static ItemStack getLocationTransmitter(ServerPlayer player) {        
        // Check Curios slots if Curios is loaded and prioritize curios item over other inventory
        if (Mods.CURIOS.isLoaded() && CuriosCompat.isCuriosLoaded()) {
            ItemStack curiosStack = CuriosCompat.getLocationTransmitterFromCurios(player);
            if (curiosStack != null) {
                return curiosStack;
            }
        }
        
        // Check regular inventory
        for (int i = 0; i < player.getInventory().items.size(); i++) {
            ItemStack stack = player.getInventory().items.get(i);
            if (stack.getItem() instanceof LocationTransmitterItem) {
                return stack;
            }
        }
        
        return null;
    }

    
}