package galena.hats;

import java.util.Optional;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public record HatConfigMessage(@Nullable UUID player, ConfigStorage.Data data) implements CustomPacketPayload {

    public static final TypeAndCodec<FriendlyByteBuf, HatConfigMessage> TYPE = new TypeAndCodec<>(
            new Type<>(Constants.createId("hat_config")),
            StreamCodec.of(HatConfigMessage::encode, HatConfigMessage::decode)
    );

    private static void encode(FriendlyByteBuf buffer, HatConfigMessage message) {
        message.data().encode(buffer);
        buffer.writeBoolean(message.player() != null);
        Optional.ofNullable(message.player()).ifPresent(buffer::writeUUID);
    }

    private static HatConfigMessage decode(FriendlyByteBuf buffer) {
        var data = ConfigStorage.Data.decode(buffer);
        var player = buffer.readBoolean() ? buffer.readUUID() : null;
        return new HatConfigMessage(player, data);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE.type();
    }

    public void distribute(ServerPlayer sender) {
        var packet = new HatConfigMessage(sender.getUUID(), data());
        sender.server.getPlayerList().getPlayers()
                .stream()
                .filter(it -> !it.getUUID().equals(player))
                .forEach(it -> Services.NETWORK.broadcastConfig(packet, it));
    }

}
