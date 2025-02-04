package me.luligabi.noindium;

import me.luligabi.noindium.mixin.WarningScreenAccessor;
import net.minecraft.client.font.MultilineText;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.WarningScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;

@SuppressWarnings("ConstantConditions")
@OnlyIn(Dist.CLIENT)
public class NoIndiumWarningScreen extends WarningScreen {

    protected NoIndiumWarningScreen() {
        super(HEADER, MESSAGE, CHECK_MESSAGE, NARRATED_TEXT);
    }


    @Override
    protected void initButtons(int yOffset) {
        addDrawableChild(new ButtonWidget(width / 2 - 155, 100 + yOffset, 150, 20, NoIndium.HAS_OPTIFINE ? CURSEFORGE : OPEN_MODS_FOLDER, buttonWidget ->  {
            if(NoIndium.HAS_OPTIFINE) Util.getOperatingSystem().open("https://www.curseforge.com/minecraft/mc-mods/indium");
            else Util.getOperatingSystem().open(new File(FMLPaths.MODSDIR.get().toFile(), "mods"));
        }));

        addDrawableChild(new ButtonWidget(width / 2 - 155 + 160, 100 + yOffset, 150, 20, NoIndium.HAS_OPTIFINE ? MODRINTH : OPTIFINE_ALTERNATIVES, buttonWidget ->  {
            Util.getOperatingSystem().open("https://lambdaurora.dev/optifine_alternatives/");
        }));

        if(NoIndium.CONFIG.allowToProceed) {
            addDrawableChild(new ButtonWidget(width / 2 - 75, 130 + yOffset, 150, 20, Text.translatable("label.noindium.proceed"), buttonWidget ->  {
                if(checkbox.isChecked()) {
                    if(NoIndium.HAS_OPTIFINE) {
                        NoIndium.CONFIG.showOptifineScreen = false;
                    }
                    NoIndium.saveConfig(NoIndium.CONFIG);
                }
                client.setScreen(new TitleScreen(false));
            }));
        }
    }

    @Override
    protected void init() {
        ((WarningScreenAccessor) this).setMessageText(MultilineText.create(textRenderer, MESSAGE, width - 50));
        int yOffset = (((WarningScreenAccessor) this).getMessageText().count() + 1) * textRenderer.fontHeight * 2 - 20;
        if(NoIndium.CONFIG.allowToProceed) {
            checkbox = new CheckboxWidget(width / 2 - 155 + 80, 76 + yOffset, 150, 20, CHECK_MESSAGE, false);
            addDrawableChild(checkbox);
        }
        initButtons(yOffset);
    }


    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    private static final MutableText HEADER = Text.translatable("header.noindium.optifine").formatted(Formatting.DARK_RED, Formatting.BOLD);
    private static final Text MESSAGE = Text.translatable("message.noindium.optifine");
    private static final Text CHECK_MESSAGE = Text.translatable("multiplayerWarning.check");
    private static final MutableText NARRATED_TEXT = HEADER.copy().append("\n").append(MESSAGE);

    private static final Text CURSEFORGE = Text.of("CurseForge");
    private static final Text MODRINTH = Text.of("Modrinth");

    private static final Text OPEN_MODS_FOLDER = Text.translatable("label.noindium.open_mods_folder");
    private static final Text OPTIFINE_ALTERNATIVES = Text.translatable("label.noindium.optifine_alternatives");

}