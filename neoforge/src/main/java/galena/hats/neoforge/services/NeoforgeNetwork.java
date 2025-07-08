package galena.hats.neoforge.services;

import galena.hats.network.ClientboundConfigMessage;
import galena.hats.network.ServerboundConfigMessage;
import galena.hats.services.INetwork;
import galena.hats.storage.ConfigStorage;
import galena.hats.storage.ServerConfigStorage;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class NeoforgeNetwork implements INetwork {

    public static void register(IEventBus modBus) {
        modBus.addListener((RegisterPayloadHandlersEvent event) -> {
            var registrar = event.registrar("2");

            registrar.playToServer(ServerboundConfigMessage.TYPE.type(), ServerboundConfigMessage.TYPE.codec(), NeoforgeNetwork::handleMessage);
            registrar.playToClient(ClientboundConfigMessage.TYPE.type(), ClientboundConfigMessage.TYPE.codec(), NeoforgeNetwork::handleMessage);
        });
    }

    private static void handleMessage(ServerboundConfigMessage message, IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerConfigStorage.receive((ServerPlayer) context.player(), message);
        });

    }

    private static void handleMessage(ClientboundConfigMessage message, IPayloadContext context) {
        context.enqueueWork(() -> {
            message.values().forEach(ConfigStorage::receive);
        });
    }

    @Override
    public void broadcastConfig(ServerboundConfigMessage message) {
        PacketDistributor.sendToServer(message);
    }

    @Override
    public void broadcastConfig(ClientboundConfigMessage message, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, message);
    }

}
