package net.fabricmc.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.example.config.ExampleModConfig;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//ToDo: not ready for publish yet, change build.gradle, and fabric.mod and mixins.json and modid
public class ExampleMod implements ModInitializer
{
    //features:
    //a command to open the config(clothconfig)
    //  options: hud type, WASD or arrow pictures
    //  rainbow mode, when enabled it will wave over all colors
    //  text shadow (drawwithshadow)
    //  allow adding a custom key to be displayed as stroke aswell
    //  a setting that will sync the custom keys with the default color of the keys (wasd, space lmb, rmb)
    //  allow for moving of the display place
    //  set of settings to display certain things: movement keys, mouse keys, space, and cps(always, never, on click)
    public static final String MOD_ID = "examplemod";

    public static ExampleMod instance = null;
    public static ExampleModConfig config = null;

    static Logger LOGGER = LogManager.getLogger("ExampleMod");

    @Override
    public void onInitialize()
    {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        instance = this;
        config = ExampleModConfig.loadConfig();
        System.out.println("Hello Fabric world!");
        HudRenderCallback.EVENT.register(new KeystrokeOverlay());
    }
}