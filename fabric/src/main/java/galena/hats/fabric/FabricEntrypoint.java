package galena.hats.fabric;

import galena.hats.fabric.services.FabricNetwork;
import galena.hats.services.CommonServices;
import net.fabricmc.api.ModInitializer;

public class FabricEntrypoint implements ModInitializer {

    @Override
    public void onInitialize() {
        FabricNetwork.registerServerHandler();

        assert CommonServices.NETWORK != null;
    }

}
