package com.krei.cmpackagecouriers.network;

import com.krei.cmpackagecouriers.PackageCouriers;
import com.krei.cmpackagecouriers.ServerConfig;
import com.krei.cmpackagecouriers.transmitter.LocationTransmitterHelper;
import com.krei.cmpackagecouriers.transmitter.LocationTransmitterItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Packet sent from client to server when a player targets themselves with the stock ticker
 * and needs their location transmitter enabled.
 */
public record EnableLocationTransmitterPacket(String targetPlayerName) implements CustomPacketPayload {
    
    public static final Type<EnableLocationTransmitterPacket> TYPE = new Type<>(
        ResourceLocation.fromNamespaceAndPath(PackageCouriers.MODID, "enable_location_transmitter")
    );

    public static final StreamCodec<FriendlyByteBuf, EnableLocationTransmitterPacket> CODEC = StreamCodec.composite(
        StreamCodec.of(
            (buf, name) -> buf.writeUtf(name),
            buf -> buf.readUtf()
        ), EnableLocationTransmitterPacket::targetPlayerName,
        EnableLocationTransmitterPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(EnableLocationTransmitterPacket packet, IPayloadContext context) {
        // This runs on the server thread
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer) {
                // Check if config allows transmitter logic
                if (!ServerConfig.locationTransmitterNeeded) {
                    return;
                }
                
                // Verify the player is targeting themselves
                String playerName = serverPlayer.getName().getString();
                if (!playerName.equals(packet.targetPlayerName)) {
                    return;
                }
                
                // Check if player already has an enabled transmitter
                boolean hasEnabled = LocationTransmitterHelper.hasEnabledLocationTransmitter(serverPlayer);
                
                if (!hasEnabled) {
                    ItemStack transmitterStack = LocationTransmitterHelper.getLocationTransmitter(serverPlayer);
                    
                    if (transmitterStack != null && !transmitterStack.isEmpty()) {
                        long worldTime = serverPlayer.level().getGameTime();
                        
                        // Timed enable as player is targeting themselves and transmitter is off
                        LocationTransmitterItem.timedEnable(transmitterStack, serverPlayer, worldTime);
                        
                        serverPlayer.displayClientMessage(Component.translatable(PackageCouriers.MODID + ".message.transmitter_enabled", ServerConfig.timedEnableTime), true);
                    }
                }
            }
        });
    }
}