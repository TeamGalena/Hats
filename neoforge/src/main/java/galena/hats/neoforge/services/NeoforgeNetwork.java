package galena.hats.neoforge.services;

import galena.hats.ConfigStorage;
import galena.hats.HatConfigMessage;
import galena.hats.services.INetwork;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class NeoforgeNetwork implements INetwork {

    public static void register(IEventBus modBus) {
        modBus.addListener((RegisterPayloadHandlersEvent event) -> {
            var registrar = event.registrar("2");

            registrar.playBidirectional(HatConfigMessage.TYPE.type(), HatConfigMessage.TYPE.codec(), NeoforgeNetwork::handleMessage);
        });
    }

    private static void handleMessage(HatConfigMessage message, IPayloadContext context) {
        if (context.player() instanceof ServerPlayer player) {
            message.distribute(player);
        } else {
            ConfigStorage.receive(message);
        }
    }

    @Override
    public void broadcastConfig(HatConfigMessage message) {
        PacketDistributor.sendToServer(message);
    }

    @Override
    public void broadcastConfig(HatConfigMessage message, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, message);
    }

}
