package galena.hats.neoforge.services;

import galena.hats.services.IPlatformHelper;
import net.neoforged.fml.loading.FMLLoader;

public class NeoforgePlatformHelper implements IPlatformHelper {

    @Override
    public boolean isDev() {
        return !FMLLoader.isProduction();
    }

}
