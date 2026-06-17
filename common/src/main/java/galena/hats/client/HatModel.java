package galena.hats.client;

import galena.hats.HatPart;
import java.util.Collection;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;

public class HatModel<T extends HumanoidRenderState> extends EntityModel<T> {

    public HatModel(ModelPart root) {
        super(root);
    }

    public void copyPropertiesFrom(HumanoidModel<T> contextModel) {
        // TODO 26.1.2 port
        // contextModel.copyPropertiesTo(this);
        // root.copyFrom(contextModel.head);
    }

    public void setupVisibleParts(Collection<HatPart> parts) {
        for (var part : HatPart.values()) {
            var modelPart = root.getChild(part.getSerializedName());
            modelPart.visible = parts.contains(part);
        }
    }
}
