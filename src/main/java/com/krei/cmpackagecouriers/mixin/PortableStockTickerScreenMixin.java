package com.krei.cmpackagecouriers.mixin;

import com.krei.cmpackagecouriers.stock_ticker.PortableStockTickerScreen;
import com.krei.cmpackagecouriers.network.EnableLocationTransmitterPacket;
import com.krei.cmpackagecouriers.ServerConfig;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.simibubi.create.content.logistics.AddressEditBox;

import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.network.PacketDistributor;

@Mixin(value = PortableStockTickerScreen.class, remap = false)
public class PortableStockTickerScreenMixin {

    @Shadow
    public AddressEditBox addressBox;

    @Inject(method = "sendIt", at = @At("TAIL"))
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
        
        // Get client player name
        String playerName = Minecraft.getInstance().player.getName().getString();
        
        if (playerName.equals(address)) {
            // Send packet to server to handle the transmitter logic
            EnableLocationTransmitterPacket packet = new EnableLocationTransmitterPacket(address);
            PacketDistributor.sendToServer(packet);
        }
    }
}