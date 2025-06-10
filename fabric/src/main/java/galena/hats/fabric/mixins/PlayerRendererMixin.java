package galena.hats.fabric.mixins;

import galena.hats.client.HatLayer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public PlayerRendererMixin(EntityRendererProvider.Context context, PlayerModel<AbstractClientPlayer> model, float f) {
        super(context, model, f);
    }

    @Inject(
            method = "<init>",
            at = @At("RETURN")
    )
    public void reload(EntityRendererProvider.Context context, boolean bl, CallbackInfo ci) {
        var layer = context.bakeLayer(HatLayer.LAYER_LOCATION);
        addLayer(new HatLayer<>(this, layer));
    }

}
