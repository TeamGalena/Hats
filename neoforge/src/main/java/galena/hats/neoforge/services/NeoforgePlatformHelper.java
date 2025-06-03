package galena.hats.neoforge.services;

import galena.hats.services.IPlatformHelper;

public class NeoforgePlatformHelper implements IPlatformHelper {

    @Override
    public boolean isDev() {
        return !net.neoforged.fml.loading.FMLLoader.isProduction();
    }

}
