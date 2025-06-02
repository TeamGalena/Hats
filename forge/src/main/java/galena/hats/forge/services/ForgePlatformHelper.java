package galena.hats.forge.services;

import galena.hats.services.IPlatformHelper;
import net.minecraftforge.fml.loading.FMLLoader;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public boolean isDev() {
        return !FMLLoader.isProduction();
    }

}
