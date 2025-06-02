package galena.hats;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class HatLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    private final HatsModel<T> model;

    public HatLayer(RenderLayerParent<T, M> parent, ModelPart model) {
        super(parent);
        this.model = new HatsModel<>(model);
    }

    public static LayerDefinition createLayerDefinition() {
        var cubeDeformation = CubeDeformation.NONE;
        var meshDefinition = new MeshDefinition();
        var partDefinition = meshDefinition.getRoot();

        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    private static ResourceLocation getTexture(LivingEntity entity) {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int i, T entity, float f, float g, float h, float j, float k, float l) {
        if (!HatsApi.isSupporter(entity.getUUID())) return;

        var texture = getTexture(entity);
        var vertexConsumer = ItemRenderer.getArmorFoilBuffer(bufferSource, RenderType.armorCutoutNoCull(texture), false, false);
        model.prepareMobModel(entity, f, g, h);

        if (getParentModel() instanceof HumanoidModel<?> parent) {
            //noinspection unchecked
            model.copyPropertiesFrom((HumanoidModel<T>) parent);
        } else {
            model.setupAnim(entity, f, g, h, i, j);
        }

        model.renderToBuffer(poseStack, vertexConsumer, i, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }

}
