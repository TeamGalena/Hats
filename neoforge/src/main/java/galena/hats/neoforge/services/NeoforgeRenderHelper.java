package galena.hats.neoforge.services;

import galena.hats.Constants;
import galena.hats.HatType;
import galena.hats.services.IRenderHelper;
import java.util.Optional;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.util.context.ContextKey;

public class NeoforgeRenderHelper implements IRenderHelper {

    public static final ContextKey<Optional<HatType>> CONTEXT_KEY =
            new ContextKey<>(Constants.createId("hat_type"));

    @Override
    public Optional<HatType> getRendererHatType(HumanoidRenderState state) {
        return state.getRenderDataOrDefault(CONTEXT_KEY, Optional.empty());
    }

}
