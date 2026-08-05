package com.spyxar.tiptapshow;

import com.spyxar.tiptapshow.config.YaclScreenFactory;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.loader.api.FabricLoader;

public class ModMenuIntegration implements ModMenuApi
{
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory()
    {
        if (FabricLoader.getInstance().isModLoaded("yet_another_config_lib_v3"))
        {
            return YaclScreenFactory::getModConfigScreenFactory;
        }
        else
        {
            return (parent) -> null;
        }
    }
}