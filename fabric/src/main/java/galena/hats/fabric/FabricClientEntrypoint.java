package galena.hats.fabric;

import galena.hats.ClientConfigStorage;
import galena.hats.HatLayer;
import galena.hats.HatsCommand;
import galena.hats.fabric.services.FabricNetwork;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;

public class FabricClientEntrypoint implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityModelLayerRegistry.registerModelLayer(HatLayer.LAYER_LOCATION, HatLayer::createLayerDefinition);

        FabricNetwork.registerClientHandler();

        ClientPlayConnectionEvents.JOIN.register((listener, sender, minecraft) -> {
            ClientConfigStorage.broadcastConfig();
        });

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, context) -> {
            HatsCommand.registerClient(dispatcher);
        });
    }

}
