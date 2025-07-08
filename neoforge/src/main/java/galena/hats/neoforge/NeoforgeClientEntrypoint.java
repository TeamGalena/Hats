package galena.hats.neoforge;

import galena.hats.Constants;
import galena.hats.HatsApi;
import galena.hats.HatsCommand;
import galena.hats.client.HatLayer;
import galena.hats.storage.ClientConfigStorage;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class NeoforgeClientEntrypoint {

    private static <T extends LivingEntity, M extends EntityModel<T>> void addLayerTo(LivingEntityRenderer<T, M> renderer, ModelPart layer) {
        renderer.addLayer(new HatLayer<>(renderer, layer));
    }

    @SubscribeEvent
    public static void setup(FMLClientSetupEvent event) {
        ClientConfigStorage.getUUID().ifPresent(HatsApi::getSupporterData);
    }

    @SubscribeEvent
    public static void addModelLayers(EntityRenderersEvent.AddLayers event) {
        var layer = event.getEntityModels().bakeLayer(HatLayer.LAYER_LOCATION);

        event.getSkins().forEach(skin -> {
            LivingEntityRenderer<Player, PlayerModel<Player>> renderer = event.getSkin(skin);
            if (renderer != null) addLayerTo(renderer, layer);
        });

        event.getEntityTypes().forEach(type -> {
            var renderer = event.getRenderer(type);
            if (renderer instanceof HumanoidMobRenderer<?, ?> humanoid) {
                addLayerTo(humanoid, layer);
            }
        });
    }

    @SubscribeEvent
    public static void registerLayersDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(HatLayer.LAYER_LOCATION, HatLayer::createLayerDefinition);
    }

    @EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
    public static class ForgeEvents {

        @SubscribeEvent
        public static void broadcastConfig(ClientPlayerNetworkEvent.LoggingIn event) {
            ClientConfigStorage.broadcastConfig();
        }

        @SubscribeEvent
        public static void registerCommand(RegisterClientCommandsEvent event) {
            HatsCommand.registerClient(event.getDispatcher());
        }

    }

}
