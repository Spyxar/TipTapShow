package com.spyxar.tiptapshow.mixin;

import com.spyxar.tiptapshow.ClickCounter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
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
    public void tiptapshow$onMouseButton(long window, MouseInput mouseInput, int action, CallbackInfo ci)
    {
        if (action != GLFW.GLFW_PRESS)
        {
            return;
        }
        if (MinecraftClient.getInstance().currentScreen != null)
        {
            return;
        }

        int button = mouseInput.button();
        if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT)
        {
            ClickCounter.registerRightClick();
        }
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT)
        {
            ClickCounter.registerLeftClick();
        }
    }
}