package galena.hats.neoforge;

import galena.hats.Constants;
import galena.hats.HatsCommand;
import galena.hats.storage.ServerConfigStorage;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.DEDICATED_SERVER)
public class NeoforgeServerEntrypoint {

    @SubscribeEvent
    public static void notifyCached(PlayerEvent.PlayerLoggedInEvent event) {
        ServerConfigStorage.notifyCached((ServerPlayer) event.getEntity());
    }

    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        HatsCommand.registerServer(event.getDispatcher());
    }

}
