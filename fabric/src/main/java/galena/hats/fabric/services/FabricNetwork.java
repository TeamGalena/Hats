package galena.hats.fabric.services;

import galena.hats.ConfigStorage;
import galena.hats.Constants;
import galena.hats.HatConfigMessage;
import galena.hats.services.INetwork;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class FabricNetwork implements INetwork {

    private static final ResourceLocation ID = Constants.createId("hat_config");

    private static FriendlyByteBuf encode(HatConfigMessage message) {
        var buffer = PacketByteBufs.empty();
        HatConfigMessage.encode(message, buffer);
        return buffer;
    }

    @Override
    public void broadcastConfig(HatConfigMessage message) {
        ClientPlayNetworking.send(ID, encode(message));
    }

    @Override
    public void broadcastConfig(HatConfigMessage message, ServerPlayer player) {
        ServerPlayNetworking.send(player, ID, encode(message));
    }

    public static void registerClientHandler() {
        ClientPlayNetworking.registerGlobalReceiver(ID, (minecraft, listener, buffer, sender) -> {
            var packet = HatConfigMessage.decode(buffer);
            ConfigStorage.receive(packet);
        });
    }

    public static void registerServerHandler() {
        ServerPlayNetworking.registerGlobalReceiver(ID, (server, player, listener, buffer, sender) -> {
            var packet = HatConfigMessage.decode(buffer);
            packet.distribute(player);
        });
    }

}
