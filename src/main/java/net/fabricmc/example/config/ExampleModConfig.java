package net.fabricmc.example.config;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import net.fabricmc.example.ExampleMod;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.IOException;

public final class ExampleModConfig
{
    private transient File file;

    public boolean settingA = true;
    public float settingB = 5f;

    private ExampleModConfig() {}

    public static ExampleModConfig loadConfig()
    {
        File file = new File(
                FabricLoader.getInstance().getConfigDir().toString(),
                ExampleMod.MOD_ID + ".toml"
        );

        ExampleModConfig config;
        if (file.exists())
        {
            Toml configTOML = new Toml().read(file);
            config = configTOML.to(ExampleModConfig.class);
            config.file = file;
        }
        else
        {
            config = new ExampleModConfig();
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