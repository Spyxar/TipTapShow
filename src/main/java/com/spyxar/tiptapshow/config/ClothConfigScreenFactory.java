package com.spyxar.tiptapshow.config;

import com.spyxar.tiptapshow.TipTapShowMod;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ClothConfigScreenFactory
{
    //ToDo: dropdown config menu
    //Color setup: 0xRRGGBBAA
    public static Screen makeConfig(Screen parent)
    {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("config.tiptapshow.title"))
                .setSavingRunnable(TipTapShowMod.config::saveConfig);
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        builder.getOrCreateCategory(Text.translatable("config.tiptapshow.categories.general"))
                .addEntry(entryBuilder
                        .startBooleanToggle(
                                Text.translatable("config.tiptapshow.option.renderingui"),
                                TipTapShowMod.config.renderInGui
                        )
                        .setDefaultValue(true)
                        .setTooltip(Text.translatable("config.tiptapshow.description.renderingui"))
                        .setSaveConsumer(newValue -> TipTapShowMod.config.renderInGui = newValue)
                        .build()
                );
        builder.getOrCreateCategory(Text.translatable("config.tiptapshow.categories.general"))
                .addEntry(entryBuilder
                        .startBooleanToggle(
                                Text.translatable("config.tiptapshow.option.countclicksingui"),
                                TipTapShowMod.config.countClicksInGui
                        )
                        .setDefaultValue(false)
                        .setTooltip(Text.translatable("config.tiptapshow.description.countclicksingui"))
                        .setSaveConsumer(newValue -> TipTapShowMod.config.countClicksInGui = newValue)
                        .build()
                );
        builder.getOrCreateCategory(Text.translatable("config.tiptapshow.categories.general"))
                .addEntry(entryBuilder
                        .startBooleanToggle(
                                Text.translatable("config.tiptapshow.option.keyshadow"),
                                TipTapShowMod.config.keyShadow
                        )
                        .setDefaultValue(false)
                        .setTooltip(Text.translatable("config.tiptapshow.description.keyshadow"))
                        .setSaveConsumer(newValue -> TipTapShowMod.config.keyShadow = newValue)
                        .build()
                );
        builder.getOrCreateCategory(Text.translatable("config.tiptapshow.categories.general"))
                .addEntry(entryBuilder
                        .startAlphaColorField(
                                Text.translatable("config.tiptapshow.option.backgroundcolor"),
                                TipTapShowMod.config.backgroundColor
                        )
                        .setDefaultValue(0x373d47bf)
                        .setTooltip(Text.translatable("config.tiptapshow.description.backgroundcolor"))
                        .setSaveConsumer(newValue -> TipTapShowMod.config.backgroundColor = newValue)
                        .build()
                );
        builder.getOrCreateCategory(Text.translatable("config.tiptapshow.categories.general"))
                .addEntry(entryBuilder
                        .startAlphaColorField(
                                Text.translatable("config.tiptapshow.option.pressedbackgroundcolor"),
                                TipTapShowMod.config.pressedBackgroundColor
                        )
                        .setDefaultValue(0x373d4747)
                        .setTooltip(Text.translatable("config.tiptapshow.description.pressedbackgroundcolor"))
                        .setSaveConsumer(newValue -> TipTapShowMod.config.pressedBackgroundColor = newValue)
                        .build()
                );
        builder.getOrCreateCategory(Text.translatable("config.tiptapshow.categories.general"))
                .addEntry(entryBuilder
                        .startColorField(
                                Text.translatable("config.tiptapshow.option.keycolor"),
                                TipTapShowMod.config.keyColor
                        )
                        .setDefaultValue(16777215)
                        .setTooltip(Text.translatable("config.tiptapshow.description.keycolor"))
                        .setSaveConsumer(newValue -> TipTapShowMod.config.keyColor = newValue)
                        .build()
                );
        builder.getOrCreateCategory(Text.translatable("config.tiptapshow.categories.general"))
                .addEntry(entryBuilder
                        .startColorField(
                                Text.translatable("config.tiptapshow.option.pressedkeycolor"),
                                TipTapShowMod.config.pressedKeyColor
                        )
                        .setDefaultValue(0)
                        .setTooltip(Text.translatable("config.tiptapshow.description.pressedkeycolor"))
                        .setSaveConsumer(newValue -> TipTapShowMod.config.pressedKeyColor = newValue)
                        .build()
                );
        return builder.build();
    }
}