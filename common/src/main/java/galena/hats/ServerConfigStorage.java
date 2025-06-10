package galena.hats;

import galena.hats.services.CommonServices;
import java.util.concurrent.CompletableFuture;
import net.minecraft.server.level.ServerPlayer;

public class ServerConfigStorage {

    private static final ConfigData EMPTY_DATA = ConfigData.DEFAULT;

    public static void receive(ServerPlayer sender, HatConfigMessage message) {
        Constants.LOGGER.debug("Received HatConfigMessage from {}", sender.getUUID());
        validated(sender, message).thenAccept(data -> {
            ConfigStorage.receive(sender.getUUID(), data);
            distribute(sender, data);
        });
    }

    private static CompletableFuture<ConfigData> validated(ServerPlayer sender, HatConfigMessage message) {
        var data = message.data();

        if (!data.enabled()) {
            Constants.LOGGER.debug("Skipping server validation for disabled config from {}", sender.getUUID());
            return CompletableFuture.completedFuture(data);
        }

        Constants.LOGGER.debug("Fetching supporter data for {}", sender.getUUID());

        return HatsApi.getAsyncSupporterData(sender.getUUID())
                .thenApply(supporterData -> {
                    var allowed = HatType.allowed(supporterData);
                    if (!allowed.contains(data.type())) throw new HatNotAllowedException();
                    Constants.LOGGER.debug("Successfully validated for {}", sender.getUUID());
                    return data;
                })
                .exceptionally(ex -> {
                    Constants.LOGGER.debug("Returning empty data for {} because of:", sender.getUUID(), ex);
                    return EMPTY_DATA;
                });
    }

    public static void notifyCached(ServerPlayer player) {
        Constants.LOGGER.debug("Notifying {} about configs of already joined players", player.getUUID());
        ConfigStorage.getConfigs()
                .filter(it -> !it.getKey().equals(player.getUUID()))
                .map(it -> new HatConfigMessage(it.getKey(), it.getValue()))
                .forEach(packet -> CommonServices.NETWORK.broadcastConfig(packet, player));
    }

    private static void distribute(ServerPlayer sender, ConfigData data) {
        Constants.LOGGER.debug("Distributing config of {}", sender.getUUID());
        var packet = new HatConfigMessage(sender.getUUID(), data);
        sender.server.getPlayerList().getPlayers()
                .stream()
                .filter(it -> !it.equals(sender))
                .forEach(it -> CommonServices.NETWORK.broadcastConfig(packet, it));
    }

    public static void clear() {
        ConfigStorage.clear();
    }

}
