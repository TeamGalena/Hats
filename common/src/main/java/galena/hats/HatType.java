package galena.hats;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.Locale;
import java.util.Optional;

public enum HatType implements StringRepresentable {

    BINOME,
    BIOME_VOTE,
    COPPERATIVE,
    DOOM_GLOOM,
    FERMION,
    GILDED,
    HEART_CRYSTAL,
    NIRVANA,
    OVERWEIGHT_FARMING,
    TRANS,
    TRINKETS,
    WINDSWEPT,
    ZOMBIE_FRIENDS,
    ;

    public static Optional<HatType> of(LivingEntity entity) {
        if (entity instanceof Player && ConfigStorage.isEnabled()) {
            return Optional.ofNullable(ConfigStorage.getHatType());
        }

        return Optional.empty();

    }

    public static final Codec<HatType> CODEC = StringRepresentable.fromEnum(HatType::values);

    public final ResourceLocation texture;

    HatType() {
        this.texture = Constants.createId("textures/models/" + getSerializedName() + "_tophat.png");
    }

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }

}
