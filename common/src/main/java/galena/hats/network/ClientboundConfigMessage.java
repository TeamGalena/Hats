package galena.hats.network;

import galena.hats.ConfigData;
import java.util.Map;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;

public record ClientboundConfigMessage(Map<UUID, ConfigData> values) {

    public static void encode(ClientboundConfigMessage message, FriendlyByteBuf buffer) {
        buffer.writeMap(
                message.values(),
                FriendlyByteBuf::writeUUID,
                (b, d) -> d.encode(b)
        );
    }

    public static ClientboundConfigMessage decode(FriendlyByteBuf buffer) {
        var values = buffer.readMap(
                FriendlyByteBuf::readUUID,
                ConfigData::decode
        );
        return new ClientboundConfigMessage(values);
    }

}
