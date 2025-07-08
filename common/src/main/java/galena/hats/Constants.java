package galena.hats;

import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Constants {

    public static final String MOD_ID = "galena_hats";

    public static final Logger LOGGER = LogManager.getLogger("Galena Hats");

    public static ResourceLocation createId(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

}
