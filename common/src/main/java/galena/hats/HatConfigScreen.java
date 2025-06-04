package galena.hats;

import static galena.hats.Constants.MOD_ID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class HatConfigScreen extends Screen {

    public static final String TRANSLATION_KEY = MOD_ID + ".screen";

    private final @Nullable Screen from;
    private final Collection<HatType> values;

    public HatConfigScreen(@Nullable Screen from, Collection<HatType> values) {
        super(Component.translatable(TRANSLATION_KEY + ".title"));
        this.from = from;
        this.values = values;
    }

    @Override
    protected void init() {
        var centerX = width / 2;
        var startY = height / 6;

        var config = ConfigStorage.getLocalConfig();

        addRenderableWidget(CycleButton.onOffBuilder(config.enabled())
                .create(centerX - 100, startY, 200, 20, Component.translatable(TRANSLATION_KEY + ".button.enable"), (button, value) -> ConfigStorage.setEnabled(value))
        );

        addRenderableWidget(CycleButton.<HatType>builder(type -> Component.translatable(MOD_ID + ".hat_type." + type.getSerializedName()))
                .withValues(values)
                .withInitialValue(config.type())
                .create(centerX - 100, startY + 24, 200, 20, Component.translatable(TRANSLATION_KEY + ".button.type"), (button, value) -> ConfigStorage.setHatType(value))
        );

        addRenderableWidget(
                Button.builder(CommonComponents.GUI_DONE, (button) -> onClose())
                        .bounds(centerX - 100, startY + 78, 200, 20)
                        .build()
        );
    }

    @Override
    public void onClose() {
        if (from != null) {
            Minecraft.getInstance().setScreen(from);
        }
    }

}
