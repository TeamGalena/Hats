package galena.hats.fabric;

import galena.hats.HatLayer;
import galena.hats.fabric.services.FabricNetwork;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;

public class FabricClientEntrypoint implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityModelLayerRegistry.registerModelLayer(HatLayer.LAYER_LOCATION, HatLayer::createLayerDefinition);

        FabricNetwork.registerClientHandler();
    }

}
