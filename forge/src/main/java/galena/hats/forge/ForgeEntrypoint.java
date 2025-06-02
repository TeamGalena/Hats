package galena.hats.forge;

import galena.hats.Constants;
import galena.hats.forge.services.ForgeNetwork;
import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class ForgeEntrypoint {

    public ForgeEntrypoint() {
        ForgeNetwork.register();
    }

}
