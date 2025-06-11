package galena.hats.forge;

import galena.hats.Constants;
import galena.hats.HatsCommand;
import galena.hats.storage.ServerConfigStorage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.DEDICATED_SERVER)
public class ForgeServerEntrypoint {

    @SubscribeEvent
    public static void notifyCached(PlayerEvent.PlayerLoggedInEvent event) {
        ServerConfigStorage.notifyCached((ServerPlayer) event.getEntity());
    }

    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        HatsCommand.registerServer(event.getDispatcher());
    }

}
