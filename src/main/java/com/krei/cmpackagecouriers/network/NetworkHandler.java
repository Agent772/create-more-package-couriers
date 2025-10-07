package com.krei.cmpackagecouriers.network;

import com.krei.cmpackagecouriers.PackageCouriers;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = PackageCouriers.MODID)
public class NetworkHandler {

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        
        registrar.playToServer(
            EnableLocationTransmitterPacket.TYPE,
            EnableLocationTransmitterPacket.CODEC,
            EnableLocationTransmitterPacket::handle
        );
    }
}