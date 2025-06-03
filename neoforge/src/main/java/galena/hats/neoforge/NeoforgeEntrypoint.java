package galena.hats.neoforge;

import galena.hats.Constants;
import galena.hats.neoforge.services.NeoforgeNetwork;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class NeoforgeEntrypoint {

    public NeoforgeEntrypoint(IEventBus modBus) {
        NeoforgeNetwork.register(modBus);
    }

}
