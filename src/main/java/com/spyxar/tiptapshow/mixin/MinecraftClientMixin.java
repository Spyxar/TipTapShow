package com.spyxar.tiptapshow.mixin;

import com.spyxar.tiptapshow.ClickCounter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin
{
    @Shadow
    @Final
    public GameOptions options;

    @Inject(at = @At(value = "HEAD"), method = "doAttack")
    private void onAttack(CallbackInfoReturnable<Boolean> cir)
    {
        ClickCounter.registerLeftClick();
    }

    @Inject(at = @At(value = "HEAD"), method = "handleInputEvents")
    private void onInputEvent(CallbackInfo ci)
    {
        if (this.options.useKey.wasPressed())
        {
            ClickCounter.registerRightClick();
        }
    }
}