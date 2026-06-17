package galena.hats.fabric;

import galena.hats.HatsApi;
import galena.hats.HatsCommand;
import galena.hats.client.HatLayer;
import galena.hats.fabric.services.FabricNetwork;
import galena.hats.storage.ClientConfigStorage;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityRenderLayerRegistrationCallback;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;

public class FabricClientEntrypoint implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        LivingEntityRenderLayerRegistrationCallback.EVENT.register((_, renderer, helper, context) -> {
            if (renderer instanceof HumanoidMobRenderer<?, ?, ?> humanoid) {
                var layer = context.bakeLayer(HatLayer.LAYER_LOCATION);
                helper.register(new HatLayer<>(humanoid, layer));
            }
        });

        FabricNetwork.registerCodecs();
        FabricNetwork.registerClientHandler();
        FabricNetwork.registerServerHandler();

        ClientPlayConnectionEvents.JOIN.register((_, _, _) -> {
            ClientConfigStorage.broadcastConfig();
        });

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, _) -> {
            HatsCommand.registerClient(dispatcher);
        });

        ClientConfigStorage.getUUID().ifPresent(HatsApi::getSupporterData);
    }

}
