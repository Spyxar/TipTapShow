package com.spyxar.tiptapshow.components;

import com.spyxar.tiptapshow.ClickCounter;
import com.spyxar.tiptapshow.config.TipTapShowConfig;
import me.x150.renderer.render.ExtendedDrawContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.input.MouseInput;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import org.joml.Vector4f;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static com.spyxar.tiptapshow.TipTapShowMod.config;

public class RenderableButton
{
    private final int x;
    public int y;
    private final int width;
    private final int height;

    private final KeyBinding key;
    private final String displayText;

    private static final Map<String, Integer> cachedRainbowColors = new HashMap<>();

    private static long lastUsedRainbowMillis = 0;
    private static int rainbowFramesSkipped = 0;

    public RenderableButton(int x, int y, int width, int height, KeyBinding key)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.key = key;
        this.displayText = getDisplayText(key);
    }

    public void render(DrawContext context)
    {
        boolean isPressed = key.isPressed();
        int fillColor = isPressed ? config.pressedBackgroundColor : config.backgroundColor;
        int textColor;
        if (config.rainbowMode)
        {
            maybeClearRainbowCache();
            textColor = getRainbowColor(this.x);
        }
        else if (isPressed)
        {
            textColor = config.pressedKeyColor;
        }
        else
        {
            textColor = config.keyColor;
        }

        if (config.roundedBackground)
        {
            int red = fillColor & 0xff;
            int green = (fillColor >> 8) & 0xff;
            int blue = (fillColor >> 16) & 0xff;
            int alpha = (fillColor >> 24) & 0xff;
            ExtendedDrawContext.drawRoundedRect(context, x, y, width, height, new Vector4f(5), new me.x150.renderer.util.Color(red / 255.0f, green / 255.0f, blue / 255.0f, alpha / 255.0f));
        }
        else
        {
            context.fill(x, y, x + width, y + height, fillColor);
        }

        if (displayText.equals("{jumpKey}"))
        {
            renderJump(context, this, textColor);
            return;
        }

        MinecraftClient client = MinecraftClient.getInstance();

        //ToDo:
        // When in GUI scale 1, the RMB + CPS doesn't render correctly, not sure what causes it or how to fix
        // When only rendering the first line (CpsType NEVER/ON_CLICK) it's fine
        //ToDo:
        // Text does not scale (properly) when using a displayFactor other than 1 (#7)
        if (displayText.contains("\n"))
        {
            String[] splitText = displayText.split("\n", 2);
            String firstLine = splitText[0];
            String secondLine = splitText[1];
            float firstLineX = width / 2F + x - client.textRenderer.getWidth(firstLine) / 2F;
            float secondLineX = width / 2F + x - client.textRenderer.getWidth(secondLine) / 2F;
            float lineY = height / 2F + y - client.textRenderer.fontHeight * 2 / 2F;

            renderText(context, firstLine, firstLineX, lineY, textColor);
            renderText(context, secondLine, secondLineX, lineY + client.textRenderer.fontHeight, textColor);
            return;
        }

        int letterWidth = client.textRenderer.getWidth(displayText);
        float lineX = width / 2F + x - letterWidth / 2F;
        float lineY = height / 2F + y - client.textRenderer.fontHeight / 2F;

        renderText(context, displayText, lineX, lineY, textColor);
    }

    private static void renderJump(DrawContext context, RenderableButton button, int textColor)
    {
        int letterWidth = (int) (button.width * 0.6);
        float lineX = button.width / 2F + button.x;
        float lineY = button.height / 2F + button.y;
        context.fill(Math.round(lineX - letterWidth / 2F), (int) lineY - 1, Math.round(lineX + letterWidth / 2F), (int) lineY, textColor);
    }

    private static void renderText(DrawContext context, String text, float lineX, float lineY, int textColor)
    {
        if (config.keyShadow)
        {
            context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, Text.of(text), (int) lineX, (int) lineY, textColor);
            return;
        }
        context.drawText(MinecraftClient.getInstance().textRenderer, text, (int) lineX, (int) lineY, textColor, false);
    }

    private static String getDisplayText(KeyBinding key)
    {
        if (key.equals(MinecraftClient.getInstance().options.attackKey))
        {
            return getDisplayTextForUseOrAttackKey(key, ClickCounter.getLeftCps(), "text.tiptapshow.lmb");
        }
        else if (key.equals(MinecraftClient.getInstance().options.useKey))
        {
            return getDisplayTextForUseOrAttackKey(key, ClickCounter.getRightCps(), "text.tiptapshow.rmb");
        }
        else if (key.equals(MinecraftClient.getInstance().options.jumpKey))
        {
            return "{jumpKey}";
        }
        else
        {
            return key.getBoundKeyLocalizedText().getString().toUpperCase();
        }
    }

    private static String getDisplayTextForUseOrAttackKey(KeyBinding key, int clicks, String label)
    {
        String lmbString = Text.translatable("text.tiptapshow.lmb").getString();
        String rmbString = Text.translatable("text.tiptapshow.rmb").getString();
        Click leftClick = new Click(0, 0, new MouseInput(0, 0));
        Click rightClick = new Click(0, 0, new MouseInput(1, 0));
        
        if (!shouldRenderCps(clicks))
        {
            if (key.isDefault())
            {
                return Text.translatable(label).getString();
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
                return key.getBoundKeyLocalizedText().getString().toUpperCase();
            }
        }
        else
        {
            if (key.isDefault())
            {
                return Text.translatable(label).getString() + "\n" + clicks + " " + Text.translatable("text.tiptapshow.cps").getString();
            }
            else
            {
                if (key.matchesMouse(leftClick))
                {
                    return lmbString + "\n" + clicks + " " + Text.translatable("text.tiptapshow.cps").getString();
                }
                else if (key.matchesMouse(rightClick))
                {
                    return rmbString + "\n" + clicks + " " + Text.translatable("text.tiptapshow.cps").getString();
                }
                return key.getBoundKeyLocalizedText().getString().toUpperCase() + "\n" + clicks + " " + Text.translatable("text.tiptapshow.cps").getString();
            }
        }
    }

    private static boolean shouldRenderCps(int clicks)
    {
        return config.cpsType == TipTapShowConfig.CpsType.ALWAYS || (config.cpsType == TipTapShowConfig.CpsType.ON_CLICK && clicks != 0);
    }

    public int getRainbowColor(double offset)
    {
        if (cachedRainbowColors.containsKey(this.displayText))
        {
            return cachedRainbowColors.get(this.displayText);
        }

        float hue = (float) (lastUsedRainbowMillis % 1000L / 1000.0) + (float) (this.width + offset / this.width * (config.rainbowOffset / 10.0));
        int newColor = Color.HSBtoRGB(hue, 1.0f, 1.0f);
        cachedRainbowColors.put(this.displayText, newColor);
        return newColor;
    }

    public void maybeClearRainbowCache()
    {
        //ToDo: this 7 should be dynamic, based on amount of keys displayed
        int framesToSkip = (5 + 1 - config.rainbowSpeed) * 7;
        if (framesToSkip <= rainbowFramesSkipped)
        {
            cachedRainbowColors.clear();
            lastUsedRainbowMillis += 1000 / Math.max(MinecraftClient.getInstance().getCurrentFps(), 60);
            rainbowFramesSkipped = 0;
        }
        rainbowFramesSkipped++;
    }
}