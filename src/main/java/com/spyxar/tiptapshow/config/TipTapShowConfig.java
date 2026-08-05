//~ identifier
//~ component_text
package com.spyxar.tiptapshow.config;

import com.spyxar.tiptapshow.TipTapShowMod;
import dev.isxander.yacl3.api.NameableEnum;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class TipTapShowConfig
{
    public static ConfigClassHandler<TipTapShowConfig> CONFIG = ConfigClassHandler.createBuilder(TipTapShowConfig.class)
            .id(Identifier./*? >=26.1 {*/fromNamespaceAndPath/*?} else {*/ /*of *//*?}*/(TipTapShowMod.MOD_ID, "config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("tiptapshow.json5"))
                    .setJson5(true)
                    .build())
            .build();

    public enum CpsType implements NameableEnum
    {
        ALWAYS,
        ON_CLICK,
        @SuppressWarnings("unused") //Not actually unused, config scrolls through this enum
        NEVER;

        @Override
        public Component getDisplayName()
        {
            return Component.translatable("text.tiptapshow.cpstype." + name().toLowerCase().replace("_", ""));
        }
    }

    @SerialEntry
    public boolean isEnabled = true;
    @SerialEntry
    public Color backgroundColor = new Color(61, 71, 191, 55);
    @SerialEntry
    public Color pressedBackgroundColor = new Color(61, 71, 71, 55);
    @SerialEntry
    public Color keyColor = new Color(255, 255, 255, 255);
    @SerialEntry
    public Color pressedKeyColor = new Color(0, 0, 0, 255);
    @SerialEntry
    public boolean rainbowMode = false;
    @SerialEntry
    public int rainbowOffset = 9;
    @SerialEntry
    public int rainbowSpeed = 4;
    @SerialEntry
    public boolean roundedBackground = false;
    @SerialEntry
    public boolean renderInDebugHud = true;
    @SerialEntry
    public boolean renderInGui = true;
    @SerialEntry
    public boolean renderWhenPlayerListOpen = true;
    @SerialEntry
    public boolean keyShadow = false;
    @SerialEntry
    public CpsType cpsType = CpsType.ALWAYS;
    @SerialEntry
    public double displayFactor = 1;
    @SerialEntry
    public int horizontalSlider = 20;
    @SerialEntry
    public int verticalSlider = 20;
    @SerialEntry
    public boolean showMovement = true;
    @SerialEntry
    public boolean showJump = true;
    @SerialEntry
    public boolean showClick = true;

    public TipTapShowConfig()
    {
        // If an old TOML config is found convert it to json format
        File file = FabricLoader.getInstance().getConfigDir().resolve("tiptapshow.toml").toFile();
        if (file.exists())
        {
            try
            {
                List<String> lines = Files.readAllLines(file.toPath());

                StringBuilder json = new StringBuilder();
                json.append("{\n");

                boolean firstLine = true;

                for (String line : lines)
                {
                    line = line.trim().replace(" = ", ": ");

                    if (!firstLine)
                    {
                        json.append(",\n");
                    }

                    json.append("    ").append(line);
                    firstLine = false;
                }

                json.append("\n}");

                Files.writeString(FabricLoader.getInstance().getConfigDir().resolve("tiptapshow.json5"), json.toString());

                file.delete();
            }
            catch (IOException e)
            {
                TipTapShowMod.LOGGER.error("Something went wrong trying to convert the old config to the new config", e);
            }
        }
    }
}