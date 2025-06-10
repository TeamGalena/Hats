package galena.hats.network;

import galena.hats.ConfigData;
import net.minecraft.network.FriendlyByteBuf;

public record ServerboundConfigMessage(ConfigData data) {

    public static void encode(ServerboundConfigMessage message, FriendlyByteBuf buffer) {
        message.data().encode(buffer);
    }

    public static ServerboundConfigMessage decode(FriendlyByteBuf buffer) {
        var data = ConfigData.decode(buffer);
        return new ServerboundConfigMessage(data);
    }

}
