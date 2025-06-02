package galena.hats;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.UnaryOperator;

public class ConfigStorage {

    public record Data(HatType type, boolean enabled) {
        private static Data DEFAULT = new Data(HatType.BINOME, true);

        public static final Codec<Data> CODEC = RecordCodecBuilder.create(builder ->
                builder.group(
                        HatType.CODEC.optionalFieldOf("type").xmap(it -> it.orElse(DEFAULT.type()), Optional::ofNullable).forGetter(Data::type),
                        Codec.BOOL.optionalFieldOf("enabled").xmap(it -> it.orElse(DEFAULT.enabled()), Optional::ofNullable).forGetter(Data::enabled)
                ).apply(builder, Data::new)
        );

        public void encode(FriendlyByteBuf buffer) {
            buffer.writeEnum(type());
            buffer.writeBoolean(enabled());
        }

        public static Data decode(FriendlyByteBuf buffer) {
            return new Data(
                    buffer.readEnum(HatType.class),
                    buffer.readBoolean()
            );
        }
    }

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .setLenient()
            .create();

    private static Data local = load();
    private static Map<UUID, Data> others = new HashMap<>();

    private static Path getPath() throws IOException {
        var minecraft = Minecraft.getInstance();
        var configFolder = minecraft.gameDirectory.toPath().resolve("config");
        Files.createDirectories(configFolder);
        return configFolder.resolve(Constants.MOD_ID + ".json");
    }

    private static Data load() {
        try (var reader = new FileReader(getPath().toFile())) {
            var json = GSON.fromJson(reader, JsonObject.class);
            return Data.CODEC.parse(JsonOps.INSTANCE, json)
                    .result()
                    .orElseThrow(() -> new IllegalStateException("could not decode config data"));
        } catch (IOException | IllegalStateException ex) {
            Constants.LOGGER.error("Failed to load config", ex);
            return Data.DEFAULT;
        }
    }

    private static void save() {
        try {
            var path = getPath();
            var bytes = Data.CODEC.encodeStart(JsonOps.INSTANCE, local)
                    .result()
                    .map(GSON::toJson)
                    .map(it -> it.getBytes(StandardCharsets.UTF_8))
                    .orElseThrow(() -> new IllegalStateException("could not encode config data"));

            Files.write(path, bytes);
        } catch (IOException | IllegalStateException ex) {
            Constants.LOGGER.error("Failed to save config", ex);
        }
    }

    private static void modify(UnaryOperator<Data> modifier) {
        local = modifier.apply(local);
        Services.NETWORK.broadcastConfig(new HatConfigMessage(null, local));
        save();
    }

    public static void receive(HatConfigMessage message) {
        others.put(message.player(), message.data());
    }

    public static void setHatType(HatType value) {
        modify(it -> new Data(value, it.enabled()));
    }

    public static void setEnabled(boolean value) {
        modify(it -> new Data(it.type(), value));
    }

    public static Data getLocalConfig() {
        return local;
    }

    public static Optional<Data> getConfig(Player player) {
        if (player instanceof LocalPlayer) {
            return Optional.ofNullable(getLocalConfig());
        } else {
            return Optional.ofNullable(others.get(player.getUUID()));
        }
    }

}
