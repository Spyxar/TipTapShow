//~ component_text
//~ minecraft_minecraftclient
package com.spyxar.tiptapshow.components;

import com.spyxar.tiptapshow.ClickCounter;
import com.spyxar.tiptapshow.config.TipTapShowConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.client.Minecraft;
//? if >=26.1 {
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.MouseButtonInfo;
//?} else {
/*//? if >=1.21.9 {
import net.minecraft.client.gui.Click;
import net.minecraft.client.input.MouseInput;
//?}
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
*///?}

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class RenderableButton
{
    private final int x;
    public int y;
    private final int width;
    private final int height;

    //? if >=26.1 {
    private final KeyMapping key;
    //?} else {
    /*private final KeyBinding key;
     *///?}
    private final String displayComponent;

    //? if >=26.1 {
    private static final MouseButtonEvent leftClick = new MouseButtonEvent(0, 0, new MouseButtonInfo(0, 0));
    private static final MouseButtonEvent rightClick = new MouseButtonEvent(0, 0, new MouseButtonInfo(1, 0));
    //?} else if >1.21.9 {
    /*private static final Click leftClick = new Click(0, 0, new MouseInput(0, 0));
    private static final Click rightClick = new Click(0, 0, new MouseInput(1, 0));
    *///?}

    private static final Map<String, Integer> cachedRainbowColors = new HashMap<>();

    private static long lastUsedRainbowMillis = 0;
    private static int rainbowFramesSkipped = 0;

    public RenderableButton(int x, int y, int width, int height, /*? >=26.1 {*/ KeyMapping /*?} else {*/ /*KeyBinding *//*?}*/ key)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.key = key;
        this.displayComponent = getDisplayComponent(key);
    }

    public void render(/*? >=26.1 {*/ GuiGraphicsExtractor /*?} else {*/ /*DrawContext *//*?}*/ context)
    {
        boolean isPressed = key./*? >=26.1 {*/ isDown() /*?} else {*/ /*isPressed() *//*?}*/;
        int fillColor = isPressed ? TipTapShowConfig.CONFIG.instance().pressedBackgroundColor.getRGB() : TipTapShowConfig.CONFIG.instance().backgroundColor.getRGB();
        int textColor;
        if (TipTapShowConfig.CONFIG.instance().rainbowMode)
        {
            maybeClearRainbowCache();
            textColor = getRainbowColor(this.x);
        }
        else if (isPressed)
        {
            textColor = TipTapShowConfig.CONFIG.instance().pressedKeyColor.getRGB();
        }
        else
        {
            textColor = TipTapShowConfig.CONFIG.instance().keyColor.getRGB();
        }

        //ToDo: Temporarily disabled until rounded backgrounds are fixed for every version (Ref: YaclScreenFactory:87)
//        if (TipTapShowConfig.CONFIG.instance().roundedBackground)
//        {
//            int red = fillColor & 0xff;
//            int green = (fillColor >> 8) & 0xff;
//            int blue = (fillColor >> 16) & 0xff;
//            int alpha = (fillColor >> 24) & 0xff;
//            //? if =1.21.4 {
//            /*// Swapping blue and red seems to produce the correct color somehow
//            Renderer2d.renderRoundedQuad(context.getMatrices(), new Color(blue / 255.0f, green / 255.0f, red / 255.0f, alpha / 255.0f), x, y, x+width, y+height, 5, 5);
//            *///?} else {
//            ExtendedDrawContext.drawRoundedRect(context, x, y, width, height, new Vector4f(5), new me.x150.renderer.util.Color(red / 255.0f, green / 255.0f, blue / 255.0f, alpha / 255.0f));
//            //?}
//        }
//        else
//        {
//            context.fill(x, y, x + width, y + height, fillColor);
//        }
        context.fill(x, y, x + width, y + height, fillColor);

        if (displayComponent.equals("{jumpKey}"))
        {
            renderJump(context, this, textColor);
            return;
        }

        //? if >=26.1 {
        var client = Minecraft.getInstance();
        //?} else {
        /*Minecraft client = Minecraft.getInstance();
         *///?}

        //ToDo:
        // When in GUI scale 1, the RMB + CPS doesn't render correctly, not sure what causes it or how to fix
        // When only rendering the first line (CpsType NEVER/ON_CLICK) it's fine
        //ToDo:
        // Component does not scale (properly) when using a displayFactor other than 1 (#7)
        if (displayComponent.contains("\n"))
        {
            String[] splitComponent = displayComponent.split("\n", 2);
            String firstLine = splitComponent[0];
            String secondLine = splitComponent[1];
            //? if >=26.1 {
            float firstLineX = width / 2F + x - client.font.width(firstLine) / 2F;
            float secondLineX = width / 2F + x - client.font.width(secondLine) / 2F;
            float lineY = height / 2F + y - client.font.lineHeight * 2 / 2F;

            renderComponent(context, firstLine, firstLineX, lineY, textColor);
            renderComponent(context, secondLine, secondLineX, lineY + client.font.lineHeight, textColor);
            //?} else {
            /*float firstLineX = width / 2F + x - client.textRenderer.getWidth(firstLine) / 2F;
            float secondLineX = width / 2F + x - client.textRenderer.getWidth(secondLine) / 2F;
            float lineY = height / 2F + y - client.textRenderer.fontHeight * 2 / 2F;

            renderComponent(context, firstLine, firstLineX, lineY, textColor);
            renderComponent(context, secondLine, secondLineX, lineY + client.textRenderer.fontHeight, textColor);
            *///?}
            return;
        }

        //? if >=26.1 {
        int letterWidth = client.font.width(displayComponent);
        float lineX = width / 2F + x - letterWidth / 2F;
        float lineY = height / 2F + y - client.font.lineHeight / 2F;
        //?} else {
        /*int letterWidth = client.textRenderer.getWidth(displayComponent);
        float lineX = width / 2F + x - letterWidth / 2F;
        float lineY = height / 2F + y - client.textRenderer.fontHeight / 2F;
        *///?}

        renderComponent(context, displayComponent, lineX, lineY, textColor);
    }

    private static void renderJump(/*? >=26.1 {*/ GuiGraphicsExtractor /*?} else {*/ /*DrawContext *//*?}*/ context, RenderableButton button, int textColor)
    {
        int letterWidth = (int) (button.width * 0.6);
        float lineX = button.width / 2F + button.x;
        float lineY = button.height / 2F + button.y;
        context.fill(Math.round(lineX - letterWidth / 2F), (int) lineY - 1, Math.round(lineX + letterWidth / 2F), (int) lineY, textColor);
    }

    private static void renderComponent(/*? >=26.1 {*/ GuiGraphicsExtractor /*?} else {*/ /*DrawContext *//*?}*/ context, String text, float lineX, float lineY, int textColor)
    {
        //? if >=26.1 {
        Font font = Minecraft.getInstance().font;
        if (TipTapShowConfig.CONFIG.instance().keyShadow)
        {
            context.text(font, text, (int) lineX, (int) lineY, textColor, true);
            return;
        }
        context.text(font, text, (int) lineX, (int) lineY, textColor, false);
        //?} else {
        /*if (TipTapShowConfig.CONFIG.instance().keyShadow)
        {
            context.drawComponentWithShadow(Minecraft.getInstance().textRenderer, Component.of(text), (int) lineX, (int) lineY, textColor);
            return;
        }
        context.drawComponent(Minecraft.getInstance().textRenderer, text, (int) lineX, (int) lineY, textColor, false);
        *///?}
    }

    //? if >=26.1 {
    private static String getDisplayComponent(KeyMapping key)
    {
        Minecraft client = Minecraft.getInstance();
        if (key.equals(client.options.keyAttack))
        {
            return getDisplayComponentForUseOrAttackKey(key, ClickCounter.getLeftCps(), "text.tiptapshow.lmb");
        }
        else if (key.equals(client.options.keyUse))
        {
            return getDisplayComponentForUseOrAttackKey(key, ClickCounter.getRightCps(), "text.tiptapshow.rmb");
        }
        else if (key.equals(client.options.keyJump))
        {
            return "{jumpKey}";
        }
        else
        {
            return key.getTranslatedKeyMessage().getString().toUpperCase();
        }
    }

    private static String getDisplayComponentForUseOrAttackKey(KeyMapping key, int clicks, String label)
    {
        String lmbString = Component.translatable("text.tiptapshow.lmb").getString();
        String rmbString = Component.translatable("text.tiptapshow.rmb").getString();
        if (!shouldRenderCps(clicks))
        {
            if (key.isDefault())
            {
                return Component.translatable(label).getString();
            }
            else
            {
                if (key.matchesMouse(leftClick))
                {
                    return lmbString;
                }
                else if (key.matchesMouse(rightClick))
                {
                    return rmbString;
                }
                return key.getTranslatedKeyMessage().getString().toUpperCase();
            }
        }
        else
        {
            if (key.isDefault())
            {
                return Component.translatable(label).getString() + "\n" + clicks + " " + Component.translatable("text.tiptapshow.cps").getString();
            }
            else
            {
                if (key.matchesMouse(leftClick))
                {
                    return lmbString + "\n" + clicks + " " + Component.translatable("text.tiptapshow.cps").getString();
                }
                else if (key.matchesMouse(rightClick))
                {
                    return rmbString + "\n" + clicks + " " + Component.translatable("text.tiptapshow.cps").getString();
                }
                return key.getTranslatedKeyMessage().getString().toUpperCase() + "\n" + clicks + " " + Component.translatable("text.tiptapshow.cps").getString();
            }
        }
    }
    //?} else {
    /*private static String getDisplayComponent(KeyBinding key)
    {
        if (key.equals(Minecraft.getInstance().options.attackKey))
        {
            return getDisplayComponentForUseOrAttackKey(key, ClickCounter.getLeftCps(), "text.tiptapshow.lmb");
        }
        else if (key.equals(Minecraft.getInstance().options.useKey))
        {
            return getDisplayComponentForUseOrAttackKey(key, ClickCounter.getRightCps(), "text.tiptapshow.rmb");
        }
        else if (key.equals(Minecraft.getInstance().options.jumpKey))
        {
            return "{jumpKey}";
        }
        else
        {
            return key.getBoundKeyLocalizedComponent().getString().toUpperCase();
        }
    }

    private static String getDisplayComponentForUseOrAttackKey(KeyBinding key, int clicks, String label)
    {
        String lmbString = Component.translatable("text.tiptapshow.lmb").getString();
        String rmbString = Component.translatable("text.tiptapshow.rmb").getString();
        if (!shouldRenderCps(clicks))
        {
            if (key.isDefault())
            {
                return Component.translatable(label).getString();
            }
            else
            {
                if (key.matchesMouse(/^? >=1.21.9 {^/ leftClick /^?} else {^/ /^0 ^//^?}^/))
                {
                    return lmbString;
                }
                else if (key.matchesMouse(/^? >=1.21.9 {^/ rightClick /^?} else {^/ /^1 ^//^?}^/))
                {
                    return rmbString;
                }
                return key.getBoundKeyLocalizedComponent().getString().toUpperCase();
            }
        }
        else
        {
            if (key.isDefault())
            {
                return Component.translatable(label).getString() + "\n" + clicks + " " + Component.translatable("text.tiptapshow.cps").getString();
            }
            else
            {
                if (key.matchesMouse(/^? >=1.21.9 {^/ leftClick /^?} else {^/ /^0 ^//^?}^/))
                {
                    return lmbString + "\n" + clicks + " " + Component.translatable("text.tiptapshow.cps").getString();
                }
                else if (key.matchesMouse(/^? >=1.21.9 {^/ rightClick /^?} else {^/ /^1 ^//^?}^/))
                {
                    return rmbString + "\n" + clicks + " " + Component.translatable("text.tiptapshow.cps").getString();
                }
                return key.getBoundKeyLocalizedComponent().getString().toUpperCase() + "\n" + clicks + " " + Component.translatable("text.tiptapshow.cps").getString();
            }
        }
    }
    *///?}

    private static boolean shouldRenderCps(int clicks)
    {
        return TipTapShowConfig.CONFIG.instance().cpsType == TipTapShowConfig.CpsType.ALWAYS || (TipTapShowConfig.CONFIG.instance().cpsType == TipTapShowConfig.CpsType.ON_CLICK && clicks != 0);
    }

    public int getRainbowColor(double offset)
    {
        if (cachedRainbowColors.containsKey(this.displayComponent))
        {
            return cachedRainbowColors.get(this.displayComponent);
        }

        float hue = (float) (lastUsedRainbowMillis % 1000L / 1000.0) + (float) (this.width + offset / this.width * (TipTapShowConfig.CONFIG.instance().rainbowOffset / 10.0));
        int newColor = Color.HSBtoRGB(hue, 1.0f, 1.0f);
        cachedRainbowColors.put(this.displayComponent, newColor);
        return newColor;
    }

    public void maybeClearRainbowCache()
    {
        //ToDo: this 7 should be dynamic, based on amount of keys displayed
        int framesToSkip = (5 + 1 - TipTapShowConfig.CONFIG.instance().rainbowSpeed) * 7;
        if (framesToSkip <= rainbowFramesSkipped)
        {
            cachedRainbowColors.clear();
            //? if >=26.1 {
            lastUsedRainbowMillis += 1000 / Math.max(Minecraft.getInstance().getFps(), 60);
            //?} else {
            /*lastUsedRainbowMillis += 1000 / Math.max(Minecraft.getInstance().getCurrentFps(), 60);
             *///?}
            rainbowFramesSkipped = 0;
        }
        rainbowFramesSkipped++;
    }
}