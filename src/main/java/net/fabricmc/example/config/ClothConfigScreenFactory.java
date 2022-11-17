package net.fabricmc.example.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.example.ExampleMod;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ClothConfigScreenFactory
{
    public static Screen makeConfig(Screen parent)
    {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("config.examplemod.title"))
                .setSavingRunnable(ExampleMod.config::saveConfig);
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        builder.getOrCreateCategory(Text.translatable("categories.examplemod.general"))
                .addEntry(entryBuilder
                        .startBooleanToggle(
                                Text.translatable("config.examplemod.option.settinga"),
                                ExampleMod.config.settingA
                        )
                        .setDefaultValue(true)
                        .setTooltip(Text.translatable("config.examplemod.description.settinga"))
                        .setSaveConsumer(newValue -> ExampleMod.config.settingA = newValue)
                        .build()
                );
        builder.getOrCreateCategory(Text.translatable("categories.examplemod.general"))
                .addEntry(entryBuilder
                        .startFloatField(
                                Text.translatable("config.examplemod.option.settingb"),
                                ExampleMod.config.settingB
                        )
                        .setDefaultValue(0.1f)
                        .setTooltip(Text.translatable("config.examplemod.description.settingb"))
                        .setSaveConsumer(newValue -> ExampleMod.config.settingB = newValue)
                        .build()
                );
        return builder.build();
    }
}