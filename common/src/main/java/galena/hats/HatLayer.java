package galena.hats;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Optional;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
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

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Constants.createId("hat"), "main");

    private final HatModel<T> model;

    public HatLayer(RenderLayerParent<T, M> parent, ModelPart model) {
        super(parent);
        this.model = new HatModel<>(model);
    }

    public static LayerDefinition createLayerDefinition() {
        var cubeDeformation = CubeDeformation.NONE;
        var meshDefinition = new MeshDefinition();
        var partDefinition = meshDefinition.getRoot();

        partDefinition.addOrReplaceChild("main", CubeListBuilder.create()
                        .texOffs(0, 36)
                        .addBox(-5.0F, -10.0F, -5.0F, 10.0F, 2.0F, 10.0F, cubeDeformation)
                        .texOffs(16, 22)
                        .addBox(-4.0F, -16.0F, -4.0F, 8.0F, 6.0F, 8.0F, cubeDeformation),
                PartPose.offset(0.0F, 0.0F, 0.0F)
        );

        return LayerDefinition.create(meshDefinition, 48, 48);
    }

    private static Optional<ResourceLocation> getTexture(LivingEntity entity) {
        return HatType.of(entity).map(it -> it.texture);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int i, T entity, float f, float g, float h, float j, float k, float l) {
        if (!HatsApi.isSupporter(entity.getUUID()) && !Services.PLATFORM.isDev()) return;

        var texture = getTexture(entity).orElse(null);
        if (texture == null) return;

        poseStack.pushPose();

        poseStack.translate(0F, 0F, 0F);

        var vertexConsumer = ItemRenderer.getArmorFoilBuffer(bufferSource, RenderType.entityTranslucent(texture), false, false);
        model.prepareMobModel(entity, f, g, h);

        if (getParentModel() instanceof HumanoidModel<?> parent) {
            //noinspection unchecked
            model.copyPropertiesFrom((HumanoidModel<T>) parent);
        } else {
            model.setupAnim(entity, f, g, h, i, j);
        }

        model.renderToBuffer(poseStack, vertexConsumer, i, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
    }

}
