package com.spyxar.tiptapshow.components;

import com.spyxar.tiptapshow.ClickCounter;
import com.spyxar.tiptapshow.config.TipTapShowConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;

import java.awt.*;

import static com.spyxar.tiptapshow.TipTapShowMod.config;

public class RenderableButton
{
    private final int x;
    public int y;
    private final int width;
    private final int height;
    private final KeyBinding key;
    private final String displayText;

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

        MinecraftClient client = MinecraftClient.getInstance();
        context.fill(RenderLayer.getGui(), x, y, x + width, y + height, fillColor);

        if (displayText.equals("{jumpKey}"))
        {
            renderJump(context, this, textColor);
            return;
        }

        //ToDo:
        // When in GUI scale 1, the RMB + CPS doesn't render correctly, not sure what causes it or how to fix
        // When only rendering the first line (CpsType NEVER/ON_CLICK) it's fine
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
        Color color = new Color(textColor);
        context.fill(Math.round(lineX - letterWidth / 2F), (int) lineY - 1, Math.round(lineX + letterWidth / 2F), (int) lineY, ColorHelper.Argb.getArgb(255, color.getRed(), color.getGreen(), color.getBlue()));
    }

    private static void renderText(DrawContext context, String text, float lineX, float lineY, int textColor)
    {
        if (config.keyShadow)
        {
            context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, Text.of(text), (int)lineX, (int)lineY, textColor);
        }
        context.drawText(MinecraftClient.getInstance().textRenderer, text, (int)lineX, (int)lineY, textColor, false);
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
        if (!shouldRenderCps(clicks))
        {
            return key.isDefault() ? Text.translatable(label).getString() : key.getBoundKeyLocalizedText().getString().toUpperCase();
        }
        else
        {
            return (key.isDefault() ? Text.translatable(label).getString() : key.getBoundKeyLocalizedText().getString().toUpperCase()) + "\n" + clicks + " " + Text.translatable("text.tiptapshow.cps").getString();
        }
    }

    private static boolean shouldRenderCps(int clicks)
    {
        return config.cpsType == TipTapShowConfig.CpsType.ALWAYS || (config.cpsType == TipTapShowConfig.CpsType.ON_CLICK && clicks != 0);
    }

    public int getRainbowColor(double offset)
    {
        float hue = (float) (System.currentTimeMillis() % 1000L / 1000.0) + (float) (this.width + offset / this.width * (config.rainbowOffset / 10.0));
        return Color.HSBtoRGB(hue, 1.0f, 1.0f);
    }
}