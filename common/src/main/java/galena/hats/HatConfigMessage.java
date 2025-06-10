package galena.hats;

import java.util.Optional;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.Nullable;

public record HatConfigMessage(@Nullable UUID player, ConfigData data) {

    public static void encode(HatConfigMessage message, FriendlyByteBuf buffer) {
        message.data().encode(buffer);
        buffer.writeBoolean(message.player() != null);
        Optional.ofNullable(message.player()).ifPresent(buffer::writeUUID);
    }

    public static HatConfigMessage decode(FriendlyByteBuf buffer) {
        var data = ConfigData.decode(buffer);
        var player = buffer.readBoolean() ? buffer.readUUID() : null;
        return new HatConfigMessage(player, data);
    }

}
