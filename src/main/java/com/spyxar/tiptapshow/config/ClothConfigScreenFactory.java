package com.spyxar.tiptapshow.config;

import com.spyxar.tiptapshow.TipTapShowMod;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ClothConfigScreenFactory
{
    public static Screen makeConfig(Screen parent)
    {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("config.tiptapshow.title"))
                .setSavingRunnable(TipTapShowMod.config::saveConfig);
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory general = builder.getOrCreateCategory(Text.translatable("config.tiptapshow.categories.general"));
        general.addEntry(entryBuilder.startBooleanToggle(Text.translatable("config.tiptapshow.option.isenabled"), TipTapShowMod.config.isEnabled)
                .setDefaultValue(true)
                .setTooltip(Text.translatable("config.tiptapshow.description.isenabled"))
                .setSaveConsumer(newValue -> TipTapShowMod.config.isEnabled = newValue).build());
        SubCategoryBuilder colors = entryBuilder.startSubCategory(Text.translatable("config.tiptapshow.categories.colors")).setExpanded(true);
        colors.add(entryBuilder.startAlphaColorField(Text.translatable("config.tiptapshow.option.backgroundcolor"), TipTapShowMod.config.backgroundColor)
                .setDefaultValue(0x373d47bf)
                .setTooltip(Text.translatable("config.tiptapshow.description.backgroundcolor"))
                .setSaveConsumer(newValue -> TipTapShowMod.config.backgroundColor = newValue).build());
        colors.add(entryBuilder.startAlphaColorField(Text.translatable("config.tiptapshow.option.pressedbackgroundcolor"), TipTapShowMod.config.pressedBackgroundColor)
                .setDefaultValue(0x373d4747)
                .setTooltip(Text.translatable("config.tiptapshow.description.pressedbackgroundcolor"))
                .setSaveConsumer(newValue -> TipTapShowMod.config.pressedBackgroundColor = newValue).build());
        colors.add(entryBuilder.startColorField(Text.translatable("config.tiptapshow.option.keycolor"), TipTapShowMod.config.keyColor)
                .setDefaultValue(16777215)
                .setTooltip(Text.translatable("config.tiptapshow.description.keycolor"))
                .setSaveConsumer(newValue -> TipTapShowMod.config.keyColor = newValue).build());
        colors.add(entryBuilder.startColorField(Text.translatable("config.tiptapshow.option.pressedkeycolor"), TipTapShowMod.config.pressedKeyColor)
                .setDefaultValue(0)
                .setTooltip(Text.translatable("config.tiptapshow.description.pressedkeycolor"))
                .setSaveConsumer(newValue -> TipTapShowMod.config.pressedKeyColor = newValue).build());
        colors.add(entryBuilder.startBooleanToggle(Text.translatable("config.tiptapshow.option.rainbowmode"), TipTapShowMod.config.rainbowMode)
                .setDefaultValue(false)
                .setTooltip(Text.translatable("config.tiptapshow.description.rainbowmode"))
                .setSaveConsumer(newValue -> TipTapShowMod.config.rainbowMode = newValue).build());
        colors.add(entryBuilder.startIntSlider(Text.translatable("config.tiptapshow.option.rainbowoffset"), TipTapShowMod.config.rainbowOffset, 1, 10)
                .setDefaultValue(9)
                .setTooltip(Text.translatable("config.tiptapshow.description.rainbowoffset"))
                .setSaveConsumer(newValue -> TipTapShowMod.config.rainbowOffset = newValue).build());
        SubCategoryBuilder display = entryBuilder.startSubCategory(Text.translatable("config.tiptapshow.categories.display")).setExpanded(true);
        display.add(entryBuilder.startBooleanToggle(Text.translatable("config.tiptapshow.option.renderingui"), TipTapShowMod.config.renderInGui)
                .setDefaultValue(true)
                .setTooltip(Text.translatable("config.tiptapshow.description.renderingui"))
                .setSaveConsumer(newValue -> TipTapShowMod.config.renderInGui = newValue).build());
        display.add(entryBuilder.startBooleanToggle(Text.translatable("config.tiptapshow.option.keyshadow"), TipTapShowMod.config.keyShadow)
                .setDefaultValue(false)
                .setTooltip(Text.translatable("config.tiptapshow.description.keyshadow"))
                .setSaveConsumer(newValue -> TipTapShowMod.config.keyShadow = newValue).build());
        display.add(entryBuilder.startEnumSelector(Text.translatable("config.tiptapshow.option.cpstype"), TipTapShowConfig.CpsType.class, TipTapShowMod.config.cpsType)
                .setDefaultValue(TipTapShowConfig.CpsType.ALWAYS)
                .setEnumNameProvider(x -> Text.translatable("text.tiptapshow.cpstype." + x.name().toLowerCase().replace("_", "")))
                .setTooltip(Text.translatable("config.tiptapshow.description.cpstype"))
                .setSaveConsumer(newValue -> TipTapShowMod.config.cpsType = newValue).build());
        display.add(entryBuilder.startDoubleField(Text.translatable("config.tiptapshow.option.displayfactor"), TipTapShowMod.config.displayFactor)
                .setDefaultValue(1)
                .setTooltip(Text.translatable("config.tiptapshow.description.displayfactor"))
                .setSaveConsumer(newValue -> TipTapShowMod.config.displayFactor = newValue).build());
        display.add(entryBuilder.startIntSlider(Text.translatable("config.tiptapshow.option.horizontalslider"), TipTapShowMod.config.horizontalSlider, 0,
                        (int) MinecraftClient.getInstance().getWindow().getScaleFactor() * MinecraftClient.getInstance().getWindow().getScaledWidth())
                .setDefaultValue(20)
                .setTooltip(Text.translatable("config.tiptapshow.description.horizontalslider"))
                .setSaveConsumer(newValue -> TipTapShowMod.config.horizontalSlider = newValue).build());
        display.add(entryBuilder.startIntSlider(Text.translatable("config.tiptapshow.option.verticalslider"), TipTapShowMod.config.verticalSlider, 0,
                        (int) MinecraftClient.getInstance().getWindow().getScaleFactor() * MinecraftClient.getInstance().getWindow().getScaledHeight())
                .setDefaultValue(20)
                .setTooltip(Text.translatable("config.tiptapshow.description.verticalslider"))
                .setSaveConsumer(newValue -> TipTapShowMod.config.verticalSlider = newValue).build());
        SubCategoryBuilder keyDisplay = entryBuilder.startSubCategory(Text.translatable("config.tiptapshow.categories.keydisplay")).setExpanded(true);
        keyDisplay.add(entryBuilder.startBooleanToggle(Text.translatable("config.tiptapshow.option.showmovement"), TipTapShowMod.config.showMovement)
                .setDefaultValue(true)
                .setTooltip(Text.translatable("config.tiptapshow.description.showmovement"))
                .setSaveConsumer(newValue -> TipTapShowMod.config.showMovement = newValue).build());
        keyDisplay.add(entryBuilder.startBooleanToggle(Text.translatable("config.tiptapshow.option.showjump"), TipTapShowMod.config.showJump)
                .setDefaultValue(true)
                .setTooltip(Text.translatable("config.tiptapshow.description.showjump"))
                .setSaveConsumer(newValue -> TipTapShowMod.config.showJump = newValue).build());
        keyDisplay.add(entryBuilder.startBooleanToggle(Text.translatable("config.tiptapshow.option.showclick"), TipTapShowMod.config.showClick)
                .setDefaultValue(true)
                .setTooltip(Text.translatable("config.tiptapshow.description.showclick"))
                .setSaveConsumer(newValue -> TipTapShowMod.config.showClick = newValue).build());
        display.add(keyDisplay.build());
        general.addEntry(colors.build());
        general.addEntry(display.build());
        return builder.build();
    }
}