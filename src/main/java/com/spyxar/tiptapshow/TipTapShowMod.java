package com.spyxar.tiptapshow;

import com.spyxar.tiptapshow.config.TipTapShowConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TipTapShowMod implements ModInitializer
{
    //ToDo features:
    // Rainbow color mode (textcolor, not background)
    // Allow the adding of custom keys - for this to work we will likely need to completely write our own config screens etc.
    //  because Cloth doesn't allow for adding an expandable list of keybinds like it does for things like strings
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