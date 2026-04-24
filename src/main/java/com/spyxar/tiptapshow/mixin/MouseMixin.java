package com.spyxar.tiptapshow.mixin;

import com.spyxar.tiptapshow.ClickCounter;
//? if >=26.1 {
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.input.MouseButtonInfo;
//?} else {
/*import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
//? if >=1.21.10
import net.minecraft.client.input.MouseInput;
*///?}
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(/*? >=26.1 {*/ MouseHandler.class /*?} else {*/ /*Mouse.class *//*?}*/)
public class MouseMixin
{
    //? if >=26.1 {
    @Inject(method = "onButton", at = @At("HEAD"))
    public void tiptapshow$onMouseButton(long window, MouseButtonInfo input, int action, CallbackInfo ci)
    //?} else {
    /*@Inject(method = "onMouseButton", at = @At("HEAD"))
    //? if >=1.21.10 {
    public void tiptapshow$onMouseButton(long window, MouseInput input, int action, CallbackInfo ci)
    //?} else {
    /^public void tiptapshow$onMouseButton(long window, int button, int action, int mods, CallbackInfo ci)
    ^///?}
    *///?}
    {
        if (action != GLFW.GLFW_PRESS)
        {
            return;
        }
        if (/*? >=26.1 {*/ Minecraft.getInstance().screen /*?} else {*/ /*MinecraftClient.getInstance().currentScreen *//*?}*/ != null)
        {
            return;
        }

        if (/*? >=1.21.10 {*/ input.button() /*?} else {*/ /*button *//*?}*/ == GLFW.GLFW_MOUSE_BUTTON_RIGHT)
        {
            ClickCounter.registerRightClick();
        }
        if (/*? >=1.21.10 {*/ input.button() /*?} else {*/ /*button *//*?}*/ == GLFW.GLFW_MOUSE_BUTTON_LEFT)
        {
            ClickCounter.registerLeftClick();
        }
    }
}