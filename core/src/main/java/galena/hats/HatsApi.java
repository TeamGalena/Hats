package galena.hats;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

public class HatsApi {

    private static boolean loaded = false;
    private static final Collection<UUID> supporters = new HashSet<>();

    public static boolean isSupporter(UUID uuid) {
        if (!loaded) load();
        synchronized (supporters) {
            return supporters.contains(uuid);
        }
    }

    private static void load() {
        loaded = true;
        ApiClient.fetchSupporters().whenComplete((uuids, ex) -> {
            if (ex != null) {
                loaded = false;
            } else synchronized (supporters) {
                supporters.clear();
                supporters.addAll(uuids.toList());
            }
        });
    }

}
