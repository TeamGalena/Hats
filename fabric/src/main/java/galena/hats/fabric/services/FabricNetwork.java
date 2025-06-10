package galena.hats.fabric.services;

import galena.hats.Constants;
import galena.hats.network.ClientboundConfigMessage;
import galena.hats.network.ServerboundConfigMessage;
import galena.hats.services.INetwork;
import galena.hats.storage.ConfigStorage;
import galena.hats.storage.ServerConfigStorage;
import java.util.function.BiConsumer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class FabricNetwork implements INetwork {

    private static final ResourceLocation CLIENTBOUND = Constants.createId("clientbound");
    private static final ResourceLocation SERVERBOUND = Constants.createId("serverbound");

    private static <T> FriendlyByteBuf encode(T message, BiConsumer<T, FriendlyByteBuf> encoder) {
        var buffer = PacketByteBufs.create();
        encoder.accept(message, buffer);
        return buffer;
    }

    @Override
    public void broadcastConfig(ServerboundConfigMessage message) {
        ClientPlayNetworking.send(SERVERBOUND, encode(message, ServerboundConfigMessage::encode));
    }

    @Override
    public void broadcastConfig(ClientboundConfigMessage message, ServerPlayer player) {
        ServerPlayNetworking.send(player, CLIENTBOUND, encode(message, ClientboundConfigMessage::encode));
    }

    public static void registerClientHandler() {
        ClientPlayNetworking.registerGlobalReceiver(CLIENTBOUND, (minecraft, listener, buffer, sender) -> {
            var packet = ClientboundConfigMessage.decode(buffer);
            packet.values().forEach(ConfigStorage::receive);
        });
    }

    public static void registerServerHandler() {
        ServerPlayNetworking.registerGlobalReceiver(SERVERBOUND, (server, player, listener, buffer, sender) -> {
            var packet = ServerboundConfigMessage.decode(buffer);
            server.execute(() -> {
                ServerConfigStorage.receive(player, packet);
            });
        });
    }

}
