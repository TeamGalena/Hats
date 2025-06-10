package galena.hats.forge.services;

import galena.hats.Constants;
import galena.hats.network.ClientboundConfigMessage;
import galena.hats.network.ServerboundConfigMessage;
import galena.hats.services.INetwork;
import galena.hats.storage.ConfigStorage;
import galena.hats.storage.ServerConfigStorage;
import java.util.function.Supplier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ForgeNetwork implements INetwork {

    private static final String version = "1";

    private static final SimpleChannel channel = NetworkRegistry.newSimpleChannel(
            Constants.createId("network"),
            () -> version,
            version::equals,
            version::equals
    );

    public static void register() {
        channel.registerMessage(0, ServerboundConfigMessage.class, ServerboundConfigMessage::encode, ServerboundConfigMessage::decode, ForgeNetwork::handleMessage);
        channel.registerMessage(1, ClientboundConfigMessage.class, ClientboundConfigMessage::encode, ClientboundConfigMessage::decode, ForgeNetwork::handleMessage);
    }

    private static void handleMessage(ServerboundConfigMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        var context = contextSupplier.get();

        context.enqueueWork(() -> {
            ServerConfigStorage.receive(context.getSender(), message);
        });

        context.setPacketHandled(true);
    }

    private static void handleMessage(ClientboundConfigMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        var context = contextSupplier.get();

        context.enqueueWork(() -> {
            message.values().forEach(ConfigStorage::receive);
        });

        context.setPacketHandled(true);
    }

    @Override
    public void broadcastConfig(ServerboundConfigMessage message) {
        channel.send(PacketDistributor.SERVER.noArg(), message);
    }

    @Override
    public void broadcastConfig(ClientboundConfigMessage message, ServerPlayer player) {
        channel.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

}
