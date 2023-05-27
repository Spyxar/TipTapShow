package com.spyxar.tiptapshow;

import com.spyxar.tiptapshow.config.TipTapShowConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

public class TipTapShowMod implements ModInitializer
{
    //ToDo features:
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

        KeyBinding openConfigKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.tiptapshow.openconfig", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category.tiptapshow.main"));
        KeyBinding toggleKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.tiptapshow.toggleoverlay", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category.tiptapshow.main"));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (toggleKeyBinding.wasPressed())
            {
                config.isEnabled = !config.isEnabled;
                config.saveConfig();
            }
            if (openConfigKeyBinding.wasPressed())
            {
                client.setScreen(new ModMenuIntegration().getModConfigScreenFactory().create(null));
            }
        });
    }
}