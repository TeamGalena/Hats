package galena.hats.fabric.services;

import galena.hats.network.ClientboundConfigMessage;
import galena.hats.network.ServerboundConfigMessage;
import galena.hats.services.INetwork;
import galena.hats.storage.ConfigStorage;
import galena.hats.storage.ServerConfigStorage;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;

public class FabricNetwork implements INetwork {

    @Override
    public void broadcastConfig(ServerboundConfigMessage message) {
        ClientPlayNetworking.send(message);
    }

    @Override
    public void broadcastConfig(ClientboundConfigMessage message, ServerPlayer player) {
        ServerPlayNetworking.send(player, message);
    }

    private static void registerHandlers() {
        PayloadTypeRegistry.playS2C().register(ClientboundConfigMessage.TYPE.type(), ClientboundConfigMessage.TYPE.codec());
        PayloadTypeRegistry.playC2S().register(ServerboundConfigMessage.TYPE.type(), ServerboundConfigMessage.TYPE.codec());
    }

    public static void registerClientHandler() {
        registerHandlers();
        ClientPlayNetworking.registerGlobalReceiver(ClientboundConfigMessage.TYPE.type(), (message, context) -> {
            message.values().forEach(ConfigStorage::receive);
        });
    }

    public static void registerServerHandler() {
        registerHandlers();
        ServerPlayNetworking.registerGlobalReceiver(ServerboundConfigMessage.TYPE.type(), (message, context) -> {
            context.server().execute(() -> {
                ServerConfigStorage.receive(context.player(), message);
            });
        });
    }

}
