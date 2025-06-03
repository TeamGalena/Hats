package galena.hats;

import java.util.List;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;

public class HatModel<T extends LivingEntity> extends AgeableListModel<T> {

    private final ModelPart root;

    public HatModel(ModelPart root) {
        this.root = root;
    }

    @Override
    protected Iterable<ModelPart> headParts() {
        return List.of(root);
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return List.of();
    }

    @Override
    public void setupAnim(T entity, float var2, float var3, float var4, float var5, float var6) {

    }

    public void copyPropertiesFrom(HumanoidModel<T> contextModel) {
        contextModel.copyPropertiesTo(this);
        root.copyFrom(contextModel.head);
    }

}
