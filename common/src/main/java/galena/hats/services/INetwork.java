package galena.hats.services;

import galena.hats.HatConfigMessage;
import net.minecraft.server.level.ServerPlayer;

public interface INetwork {

    /**
     * client to server
     */
    void broadcastConfig(HatConfigMessage message);

    /**
     * server to client
     */
    void broadcastConfig(HatConfigMessage message, ServerPlayer player);

}
