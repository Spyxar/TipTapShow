package com.spyxar.tiptapshow.config;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import com.spyxar.tiptapshow.TipTapShowMod;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.IOException;

public final class TipTapShowConfig
{
    private transient File file;

    public boolean renderInGui = true;
    public int backgroundColor = 0x131314b3;
    public int pressedBackgroundColor = 0x131314b3;
    public int keyColor = 0x131314b3;
    public int pressedKeyColor = 0x131314b3;

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
            Toml configTOML = new Toml().read(file);
            config = configTOML.to(TipTapShowConfig.class);
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