package com.spyxar.tiptapshow;

import com.spyxar.tiptapshow.config.TipTapShowConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TipTapShowMod implements ModInitializer
{
    //ToDo features:
    // A setting to render arrows instead of WASD (or the keybind walking is set to)
    // Rainbow color mode for keys
    // Allow for adding custom keys
    // Moving the display place
    // Settings to not display certain parts
    public static final String MOD_ID = "tiptapshow";

    public static TipTapShowMod instance = null;
    public static TipTapShowConfig config = null;

    static Logger LOGGER = LogManager.getLogger("TipTapShow");

    @Override
    public void onInitialize()
    {
        instance = this;
        config = TipTapShowConfig.loadConfig();
        HudRenderCallback.EVENT.register(new KeystrokeOverlay());
    }
}