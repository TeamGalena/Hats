package galena.hats;

import java.util.Optional;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public record HatConfigMessage(@Nullable UUID player, ConfigStorage.Data data) {

    public static void encode(HatConfigMessage message, FriendlyByteBuf buffer) {
        message.data().encode(buffer);
        buffer.writeBoolean(message.player() != null);
        Optional.ofNullable(message.player()).ifPresent(buffer::writeUUID);
    }

    public static HatConfigMessage decode(FriendlyByteBuf buffer) {
        var data = ConfigStorage.Data.decode(buffer);
        var player = buffer.readBoolean() ? buffer.readUUID() : null;
        return new HatConfigMessage(player, data);
    }

}
