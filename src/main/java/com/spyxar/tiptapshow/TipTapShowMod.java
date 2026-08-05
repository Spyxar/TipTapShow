package com.spyxar.tiptapshow;

import com.spyxar.tiptapshow.config.YaclScreenFactory;
import com.spyxar.tiptapshow.config.PositionGui;
import com.spyxar.tiptapshow.config.TipTapShowConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
//? if >=26.1 {
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
//?} else {
/*import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
*///?}
//? if <1.21.8 {
/*import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
*///?} else {
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
 //?}
import net.fabricmc.loader.api.FabricLoader;
//? if >=26.1 {
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
//?} else {
/*import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
*///?}
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

public class TipTapShowMod implements ModInitializer
{
    //ToDo features:
    // Allow the adding of custom keys - for this to work we will likely need to write our own YACL controller and wrap that in a ListOption
    public static final String MOD_ID = "tiptapshow";

    public static TipTapShowMod instance = null;

    public static final Logger LOGGER = LogManager.getLogger("TipTapShow");

    private static final KeystrokeOverlay OVERLAY = new KeystrokeOverlay();

    //? if >=26.1 {
    public static final KeyMapping.Category MAIN_CATEGORY = KeyMapping.Category.register(Identifier.parse(MOD_ID + ":main"));
    //?} else if >=1.21.10 {
    /*public static final KeyBinding.Category MAIN_CATEGORY = KeyBinding.Category.create(Identifier.of(MOD_ID, "main"));
    *///?}

    @Override
    public void onInitialize()
    {
        instance = this;
        TipTapShowConfig.CONFIG.load();

        //? if <1.21.8 {
        /*HudRenderCallback.EVENT.register(OVERLAY);
        *///?} else if >=26.1 {
        HudElementRegistry.addLast(Identifier.parse(MOD_ID + ":keystroke_overlay"), OVERLAY::onHudRender);
        //?} else {
        /*HudElementRegistry.addLast(Identifier.of(MOD_ID, "keystroke_overlay"), OVERLAY::onHudRender);
         *///?}

        //? if >=26.1 {
        KeyMapping openConfigKeyMapping = KeyMappingHelper.registerKeyMapping(new KeyMapping("key.tiptapshow.openconfig", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, MAIN_CATEGORY));
        KeyMapping positionKeyMapping = KeyMappingHelper.registerKeyMapping(new KeyMapping("key.tiptapshow.openposition", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, MAIN_CATEGORY));
        KeyMapping toggleKeyMapping = KeyMappingHelper.registerKeyMapping(new KeyMapping("key.tiptapshow.toggleoverlay", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, MAIN_CATEGORY));
        //?} else if >=1.21.10 {
        /*KeyBinding openConfigKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.tiptapshow.openconfig", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, MAIN_CATEGORY));
        KeyBinding positionKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.tiptapshow.openposition", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, MAIN_CATEGORY));
        KeyBinding toggleKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.tiptapshow.toggleoverlay", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, MAIN_CATEGORY));
        *///?} else {
        /*KeyBinding openConfigKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.tiptapshow.openconfig", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category.tiptapshow.main"));
        KeyBinding positionKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.tiptapshow.openposition", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category.tiptapshow.main"));
        KeyBinding toggleKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.tiptapshow.toggleoverlay", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category.tiptapshow.main"));
        *///?}
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (/*? >=26.1 {*/ toggleKeyMapping.consumeClick() /*?} else {*/ /*toggleKeyBinding.wasPressed() *//*?}*/)
            {
                TipTapShowConfig.CONFIG.instance().isEnabled = !TipTapShowConfig.CONFIG.instance().isEnabled;
                TipTapShowConfig.CONFIG.save();
            }
            //? if >=26.1 {
            if (openConfigKeyMapping.consumeClick())
            {
                if (FabricLoader.getInstance().isModLoaded("yet_another_config_lib_v3"))
                {
                    Minecraft.getInstance()/*? >=26.2 {*/.gui/*?}*/.setScreen(YaclScreenFactory.getModConfigScreenFactory(null));
                }
                else
                {
                    SystemToast.add(Minecraft.getInstance()/*? >=26.2 {*/.gui.toastManager()/*?} else {*/ /*.getToastManager() *//*?}*/, SystemToast.SystemToastId.PERIODIC_NOTIFICATION, Component.translatable("toast.tiptapshow.openconfigfailed"), Component.translatable("toast.tiptapshow.yaclmissing"));
                    LOGGER.warn("Open config keybind was pressed, but YACL was not found.");
                }
            }
            if (positionKeyMapping.consumeClick())
            {
                client/*? >=26.2 {*/.gui/*?}*/.setScreen((new PositionGui(null)));
            }
            //?} else {
            /*if (openConfigKeyBinding.wasPressed())
            {
                if (FabricLoader.getInstance().isModLoaded("yet_another_config_lib_v3"))
                {
                    MinecraftClient.getInstance().setScreen(YaclScreenFactory.getModConfigScreenFactory(null));
                }
                else
                {
                    SystemToast.add(MinecraftClient.getInstance().getToastManager(), SystemToast.Type.PERIODIC_NOTIFICATION, Text.translatable("toast.tiptapshow.openconfigfailed"), Text.translatable("toast.tiptapshow.yaclmissing"));
                    LOGGER.warn("Open config keybind was pressed, but YACL was not found.");
                }
            }
            if (positionKeyBinding.wasPressed())
            {
                client.setScreen((new PositionGui(null)));
            }
            *///?}
        });
    }
}