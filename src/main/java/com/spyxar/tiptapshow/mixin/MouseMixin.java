package com.spyxar.tiptapshow.mixin;

import com.spyxar.tiptapshow.ClickCounter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
//? if >=1.21.10
import net.minecraft.client.input.MouseInput;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin
{
    @Inject(method = "onMouseButton", at = @At("HEAD"))
    //? if >=1.21.10 {
    public void tiptapshow$onMouseButton(long window, MouseInput input, int action, CallbackInfo ci)
    //?} else {
    /*public void tiptapshow$onMouseButton(long window, int button, int action, int mods, CallbackInfo ci)
    *///?}
    {
        if (action != GLFW.GLFW_PRESS)
        {
            return;
        }
        if (MinecraftClient.getInstance().currentScreen != null)
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