package com.spyxar.tiptapshow.config;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import com.spyxar.tiptapshow.TipTapShowMod;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.IOException;

public final class TipTapShowConfig
{
    public enum CpsType
    {
        ALWAYS,
        ON_CLICK,
        NEVER
    }

    private transient File file;

    public int backgroundColor = 0x373d47bf;
    public int pressedBackgroundColor = 0x373d4747;
    public int keyColor = 16777215;
    public int pressedKeyColor = 0;
    public boolean renderInGui = true;
    public boolean keyShadow = false;
    public boolean showMovement = true;
    public boolean showClick = true;
    public boolean showJump = true;
    public CpsType cpsType = CpsType.ALWAYS;
    public int horizontalSlider = 0;
    public int verticalSlider = 0;

    private TipTapShowConfig() {}

    public static TipTapShowConfig loadConfig()
    {
        File file = new File(
                FabricLoader.getInstance().getConfigDir().toString(),
                TipTapShowMod.MOD_ID + ".toml"
        );
        TipTapShowConfig config;
        if (file.exists())
        {
            Toml configToml = new Toml().read(file);
            config = configToml.to(TipTapShowConfig.class);
            config.file = file;
        }
        else
        {
            config = new TipTapShowConfig();
            config.file = file;
            config.saveConfig();
        }
        return config;
    }

    public void saveConfig()
    {
        TomlWriter writer = new TomlWriter();
        try
        {
            writer.write(this, file);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}