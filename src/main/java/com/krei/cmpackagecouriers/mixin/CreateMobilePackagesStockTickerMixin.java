package com.krei.cmpackagecouriers.mixin;
// NOT IMPLEMENTED - IS UNUSED FOR NOW
import com.krei.cmpackagecouriers.PackageCouriers;
import com.krei.cmpackagecouriers.ServerConfig;
import com.krei.cmpackagecouriers.transmitter.LocationTransmitterHelper;
import com.krei.cmpackagecouriers.transmitter.LocationTransmitterItem;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.simibubi.create.content.logistics.AddressEditBox;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;

@Mixin(targets = "de.theidler.create_mobile_packages.items.portable_stock_ticker.PortableStockTickerScreen", remap = false)
public class CreateMobilePackagesStockTickerMixin {

    @Shadow
    public AddressEditBox addressBox;

    @Shadow
    public Object menu; // Use Object instead of PortableStockTickerMenu to avoid class loading issues


    @Inject(method = "sendIt", at = @At("HEAD"))
    private void onSendIt(CallbackInfo ci) {
        String address = addressBox.getValue();
        
        // Check if the address is a player address (starts with @)
        int atIndex = address.indexOf('@');
        if (atIndex != -1) {
            address = address.substring(atIndex + 1);
        } else {
            return; // Not a player address, exit early
        }
        if (ServerConfig.locationTransmitterNeeded == false) {
            return; // Config disabled transmitter logic
        }
        
        // Use reflection to access the player field from the menu object
        try {
            Player player = (Player) menu.getClass().getField("player").get(menu);
            String playerName = player.getName().getString();
            
            if (playerName.equals(address)) {
                ServerPlayer serverPlayer = (ServerPlayer) player;
                if (!LocationTransmitterHelper.hasEnabledLocationTransmitter(serverPlayer)) {
                    ItemStack transmitterStack = LocationTransmitterHelper.getLocationTransmitter(serverPlayer);
                    if (transmitterStack != null) {
                        long worldTime = serverPlayer.level().getGameTime();
                        // Timed enable as he is targeting himself and transmitter is off
                        LocationTransmitterItem.timedEnable(transmitterStack, serverPlayer, worldTime);
                        serverPlayer.displayClientMessage(Component.translatable(PackageCouriers.MODID + ".message.transmitter_enabled", ServerConfig.timedEnableTime), true);
                    }
                }
            }
        } catch (Exception e) {
            // Silently fail if reflection doesn't work - the mod structure may have changed
            PackageCouriers.LOGGER.warn("Failed to access player from Create: Mobile Packages menu", e);
        }
    }
}