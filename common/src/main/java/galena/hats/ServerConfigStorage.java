package galena.hats;

import net.minecraft.server.level.ServerPlayer;

import java.util.concurrent.CompletableFuture;

public class ServerConfigStorage {

    private static final ConfigStorage.Data EMPTY_DATA = new ConfigStorage.Data(HatType.BINOME, false);

    public static void receive(ServerPlayer sender, HatConfigMessage message) {
        validated(sender, message).thenAccept(data -> {
            ConfigStorage.receive(sender.getUUID(), data);
            distribute(sender, data);
        });
    }

    private static CompletableFuture<ConfigStorage.Data> validated(ServerPlayer sender, HatConfigMessage message) {
        var data = message.data();

        if (!data.enabled()) return CompletableFuture.completedFuture(data);

        return HatsApi.getAsyncSupporterData(sender.getUUID())
                .thenApply(it -> it.orElse(null))
                .thenApply(supporterData -> {
                    if (supporterData == null) throw new HatNotAllowedException();
                    var allowed = HatType.allowed(supporterData).toList();
                    if (!allowed.contains(data.type())) throw new HatNotAllowedException();
                    return data;
                })
                .exceptionally($ -> EMPTY_DATA);
    }

    private static void distribute(ServerPlayer sender, ConfigStorage.Data data) {
        var packet = new HatConfigMessage(sender.getUUID(), data);
        sender.server.getPlayerList().getPlayers()
                .stream()
                .filter(it -> !it.getUUID().equals(sender))
                .forEach(it -> Services.NETWORK.broadcastConfig(packet, it));
    }

}
