package galena.hats;

import com.mojang.serialization.Codec;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import javax.swing.text.html.Option;

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
        if (entity instanceof Player player) {
            var config = ConfigStorage.getConfig(player);
            return config
                    .filter(ConfigStorage.Data::enabled)
                    .map(ConfigStorage.Data::type);
        }

        return Optional.empty();

    }

    private static Stream<HatType> ofFlag(String flag) {
        return switch (flag) {
            case "pride" -> Stream.of(TRANS);
            default -> Stream.empty();
        };
    }

    public static Stream<HatType> allowed(UUID uuid) {
        return HatsApi.getSupporterData(uuid)
                .map(HatType::allowed)
                .orElseGet(Stream::empty);
    }

    public static Stream<HatType> allowed(SupporterData data) {
        if (data.rank() > 0) return Arrays.stream(values());
        return data.flags()
                .stream()
                .flatMap(HatType::ofFlag);
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
