package galena.hats.forge;

import galena.hats.Constants;
import galena.hats.HatsCommand;
import galena.hats.client.HatLayer;
import galena.hats.storage.ClientConfigStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ForgeClientEntrypoint {

    private static <T extends LivingEntity, M extends EntityModel<T>> void addLayerTo(LivingEntityRenderer<T, M> renderer, ModelPart layer) {
        renderer.addLayer(new HatLayer<>(renderer, layer));
    }

    @SubscribeEvent
    public static void addModelLayers(EntityRenderersEvent.AddLayers event) {
        var mc = Minecraft.getInstance().getEntityRenderDispatcher();
        var layer = event.getEntityModels().bakeLayer(HatLayer.LAYER_LOCATION);

        event.getSkins().forEach(skin -> {
            LivingEntityRenderer<Player, PlayerModel<Player>> renderer = event.getSkin(skin);
            if (renderer != null) addLayerTo(renderer, layer);
        });

        mc.renderers.values().forEach(it -> {
            if (it instanceof HumanoidMobRenderer<?, ?> renderer) {
                addLayerTo(renderer, layer);
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
