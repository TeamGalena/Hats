package galena.hats.forge.services;

import galena.hats.ConfigStorage;
import galena.hats.Constants;
import galena.hats.HatConfigMessage;
import galena.hats.services.INetwork;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class ForgeNetwork implements INetwork {

    private static final String version = "1";

    private static final SimpleChannel channel = NetworkRegistry.newSimpleChannel(
            Constants.createId("network"),
            () -> version,
            version::equals,
            version::equals
    );

    public static void register() {
        channel.registerMessage(0, HatConfigMessage.class, HatConfigMessage::encode, HatConfigMessage::decode, ForgeNetwork::handleMessage);
    }

    private static void handleMessage(HatConfigMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        var context = contextSupplier.get();

        if (context.getDirection().getReceptionSide().isClient()) {
            ConfigStorage.receive(message);
        } else {
            message.distribute(context.getSender());
        }

        context.setPacketHandled(true);
    }

    @Override
    public void broadcastConfig(HatConfigMessage message) {
        channel.send(PacketDistributor.SERVER.noArg(), message);
    }

    @Override
    public void broadcastConfig(HatConfigMessage message, ServerPlayer player) {
        channel.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

}
