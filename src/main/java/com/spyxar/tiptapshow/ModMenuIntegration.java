package com.spyxar.tiptapshow;

import com.spyxar.tiptapshow.config.ClothConfigScreenFactory;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.loader.api.FabricLoader;

public class ModMenuIntegration implements ModMenuApi
{
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory()
    {
        if (FabricLoader.getInstance().isModLoaded("cloth-config2"))
        {
            return ClothConfigScreenFactory::makeConfig;
        }
        else
        {
            return (parent) -> null;
        }
    }
}