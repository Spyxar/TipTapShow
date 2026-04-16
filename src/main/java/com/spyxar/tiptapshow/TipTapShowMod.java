package com.spyxar.tiptapshow;

import com.spyxar.tiptapshow.config.ClothConfigScreenFactory;
import com.spyxar.tiptapshow.config.PositionGui;
import com.spyxar.tiptapshow.config.TipTapShowConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
//? if >=26.1 {
/*import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
*///?} else {
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
//?}
//? if <1.21.8 {
/*import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
*///?} else {
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
 //?}
import net.fabricmc.loader.api.FabricLoader;
//? if >=26.1 {
/*import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
*///?} else {
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
//?}
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

    //? if >=26.1 {
    /*public static final KeyMapping.Category MAIN_CATEGORY = KeyMapping.Category.register(Identifier.parse(MOD_ID + ":main"));
    *///?} else if >=1.21.10 {
    public static final KeyBinding.Category MAIN_CATEGORY = KeyBinding.Category.create(Identifier.of(MOD_ID, "main"));
    //?}

    @Override
    public void onInitialize()
    {
        instance = this;
        config = TipTapShowConfig.loadConfig();

        //? if <1.21.8 {
        /*HudRenderCallback.EVENT.register(OVERLAY);
        *///?} else if >=26.1 {
        /*HudElementRegistry.addLast(Identifier.parse(MOD_ID + ":keystroke_overlay"), OVERLAY::onHudRender);
        *///?} else {
        HudElementRegistry.addLast(Identifier.of(MOD_ID, "keystroke_overlay"), OVERLAY::onHudRender);
         //?}

        //? if >=26.1 {
        /*KeyMapping openConfigKeyMapping = KeyMappingHelper.registerKeyMapping(new KeyMapping("key.tiptapshow.openconfig", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, MAIN_CATEGORY));
        KeyMapping positionKeyMapping = KeyMappingHelper.registerKeyMapping(new KeyMapping("key.tiptapshow.openposition", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, MAIN_CATEGORY));
        KeyMapping toggleKeyMapping = KeyMappingHelper.registerKeyMapping(new KeyMapping("key.tiptapshow.toggleoverlay", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, MAIN_CATEGORY));
        *///?} else if >=1.21.10 {
        KeyBinding openConfigKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.tiptapshow.openconfig", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, MAIN_CATEGORY));
        KeyBinding positionKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.tiptapshow.openposition", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, MAIN_CATEGORY));
        KeyBinding toggleKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.tiptapshow.toggleoverlay", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, MAIN_CATEGORY));
        //?} else {
        /*KeyBinding openConfigKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.tiptapshow.openconfig", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category.tiptapshow.main"));
        KeyBinding positionKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.tiptapshow.openposition", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category.tiptapshow.main"));
        KeyBinding toggleKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.tiptapshow.toggleoverlay", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category.tiptapshow.main"));
        *///?}
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (/*? >=26.1 {*/ /*toggleKeyMapping.consumeClick()*/ /*?} else {*/ toggleKeyBinding.wasPressed() /*?}*/)
            {
                config.isEnabled = !config.isEnabled;
                config.saveConfig();
            }
            //? if >=26.1 {
            /*if (openConfigKeyMapping.consumeClick())
            {
                if (FabricLoader.getInstance().isModLoaded("cloth-config2"))
                {
                    Minecraft.getInstance().setScreen(ClothConfigScreenFactory.makeConfig(null));
                }
                else
                {
                    SystemToast.add(Minecraft.getInstance().getToastManager(), SystemToast.SystemToastId.PERIODIC_NOTIFICATION, Component.translatable("toast.tiptapshow.openconfigfailed"), Component.translatable("toast.tiptapshow.clothconfigmissing"));
                    LOGGER.warn("Open config keybind was pressed, but ClothConfig was not found.");
                }
            }
            if (positionKeyMapping.consumeClick())
            {
                client.setScreen((new PositionGui(Component.literal("Positioning"), null)));
            }
            *///?} else {
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
            //?}
        });
    }
}
