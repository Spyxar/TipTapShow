package com.spyxar.tiptapshow.mixin;

import com.spyxar.tiptapshow.ClickCounter;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin
{
    @Inject(at = @At(value = "HEAD"), method = "doAttack")
    private void onAttack(CallbackInfoReturnable<Boolean> cir)
    {
        ClickCounter.registerLeftClick();
    }

    //ToDo: this works, but doItemUse also gets called when holding, probably for food items
    // if people find this an issue we'll have to find a solution
    @Inject(at = @At(value = "HEAD"), method = "doItemUse")
    private void onUse(CallbackInfo ci)
    {
        ClickCounter.registerRightClick();
    }
}