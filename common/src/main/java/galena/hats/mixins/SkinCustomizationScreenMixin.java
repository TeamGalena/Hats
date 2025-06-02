package galena.hats.mixins;

import galena.hats.HatConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.SkinCustomizationScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(SkinCustomizationScreen.class)
public class SkinCustomizationScreenMixin extends OptionsSubScreen {

    private SkinCustomizationScreenMixin(Screen screen, Options options, Component title) {
        super(screen, options, title);
    }

    @ModifyVariable(
            method = "init",
            ordinal = 0,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/SkinCustomizationScreen;addRenderableWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;",
                    ordinal = 1,
                    shift = At.Shift.AFTER
            )
    )
    public int init(int i) {
        var minecraft = Minecraft.getInstance();
        ++i;
        var button = Button.builder(Component.translatable(HatConfigScreen.TRANSLATION_KEY), $ -> minecraft.setScreen(new HatConfigScreen(this)))
                .bounds(width / 2 - 155 + i % 2 * 160, height / 6 + 24 * (i >> 1), 150, 20)
                .build();
        addRenderableWidget(button);
        return i;
    }

}
