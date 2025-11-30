package com.spyxar.tiptapshow;

import com.spyxar.tiptapshow.config.ClothConfigScreenFactory;
import com.spyxar.tiptapshow.config.PositionGui;
import com.spyxar.tiptapshow.config.TipTapShowConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
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

    public static final Logger LOGGER = LogManager.getLogger("TipTapShow");

    private static final KeystrokeOverlay OVERLAY = new KeystrokeOverlay();

    @Override
    public void onInitialize()
    {
        instance = this;
        config = TipTapShowConfig.loadConfig();

        HudElementRegistry.addLast(Identifier.of(MOD_ID, "keystroke_overlay"), OVERLAY::onHudRender);

        KeyBinding.Category mainCategory = KeyBinding.Category.create(Identifier.of(MOD_ID, "main"));
        KeyBinding openConfigKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.tiptapshow.openconfig", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, mainCategory));
        KeyBinding positionKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.tiptapshow.openposition", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, mainCategory));
        KeyBinding toggleKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.tiptapshow.toggleoverlay", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, mainCategory));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (toggleKeyBinding.wasPressed())
            {
                config.isEnabled = !config.isEnabled;
                config.saveConfig();
            }
            if (openConfigKeyBinding.wasPressed())
            {
                if (FabricLoader.getInstance().isModLoaded("cloth-config2"))
                {
                    MinecraftClient.getInstance().setScreen(ClothConfigScreenFactory.makeConfig(null));
                }
                else
                {
                    SystemToast.add(MinecraftClient.getInstance().getToastManager(), SystemToast.Type.PERIODIC_NOTIFICATION, Text.translatable("toast.tiptapshow.openconfigfailed"), Text.translatable("toast.tiptapshow.clothconfigmissing"));
                    LOGGER.warn("Open config keybind was pressed, but ClothConfig was not found.");
                }
            }
            if (positionKeyBinding.wasPressed())
            {
                client.setScreen((new PositionGui(Text.literal("Positioning"), null)));
            }
        });
    }
}