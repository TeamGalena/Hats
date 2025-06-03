package galena.hats.fabric.services;

import galena.hats.ConfigStorage;
import galena.hats.HatConfigMessage;
import galena.hats.services.INetwork;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;

public class FabricNetwork implements INetwork {

    @Override
    public void broadcastConfig(HatConfigMessage message) {
        ClientPlayNetworking.send(message);
    }

    @Override
    public void broadcastConfig(HatConfigMessage message, ServerPlayer player) {
        ServerPlayNetworking.send(player, message);
    }

    public static void registerClientHandler() {
        PayloadTypeRegistry.playS2C().register(HatConfigMessage.TYPE.type(), HatConfigMessage.TYPE.codec());
        ClientPlayNetworking.registerGlobalReceiver(HatConfigMessage.TYPE.type(), (packet, context) -> {
            ConfigStorage.receive(packet);
        });
    }

    public static void registerServerHandler() {
        PayloadTypeRegistry.playC2S().register(HatConfigMessage.TYPE.type(), HatConfigMessage.TYPE.codec());
        ServerPlayNetworking.registerGlobalReceiver(HatConfigMessage.TYPE.type(), (packet, context) -> {
            packet.distribute(context.player());
        });
    }

}
