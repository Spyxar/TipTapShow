package com.spyxar.tiptapshow.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.spyxar.tiptapshow.ClickCounter;
import com.spyxar.tiptapshow.config.TipTapShowConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static com.spyxar.tiptapshow.TipTapShowMod.config;

public class RenderableButton
{
    private static final int ROUNDED_CORNER_RADIUS = 5;
    private static final int ROUNDED_CORNER_SAMPLES = 40;

    private final int x;
    public int y;
    private final int width;
    private final int height;

    private final KeyBinding key;
    private final String displayText;

    private static final Map<String, Integer> cachedRainbowColors = new HashMap<>();

    private static long lastUsedRainbowMillis = 0;
    private static int rainbowFramesSkipped = 0;

    private static final float[][] cornerCache = new float[4][3];

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
            renderRoundedRectangle(context.getMatrices(), fillColor, x, y, width, height, ROUNDED_CORNER_RADIUS, ROUNDED_CORNER_SAMPLES);
        }
        else
        {
            context.fill(RenderLayer.getGuiOverlay(), x, y, x + width, y + height, fillColor);
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

    private static void renderRoundedRectangle(MatrixStack stack, int color, float x, float y, float width, float height, float radius, float samples)
    {
        renderRoundedRectangle(stack, x, y, width, height, radius, radius, radius, radius, color, samples);
    }

    private static void renderRoundedRectangle(MatrixStack matrices, float x, float y, float width, float height, float radiusTL, float radiusTR, float radiusBL, float radiusBR, int color, float samples)
    {
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);

        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);

        float endX = x + width;
        float endY = y + height;

        fillCornerCache(radiusBR, endX - radiusBR, endY - radiusBR, 0);
        fillCornerCache(radiusTR, endX - radiusTR, y + radiusTR, 1);
        fillCornerCache(radiusTL, x + radiusTL, y + radiusTL, 2);
        fillCornerCache(radiusBL, x + radiusBL, endY - radiusBL, 3);

        for (int i = 0; i < 4; i++)
        {
            float[] current = cornerCache[i];
            float radius = current[0];
            for (float angle = i * 90; angle <= (i + 1) * 90; angle += 90 / samples)
            {
                float radianAngle = (float) Math.toRadians(angle);
                float sin = (float) (Math.sin(radianAngle) * radius);
                float cos = (float) (Math.cos(radianAngle) * radius);

                bufferBuilder.vertex(matrices.peek().getPositionMatrix(), current[1] + sin, current[2] + cos, 0).color(color);
            }
        }

        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());

        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    private static void fillCornerCache(float a, float b, float c, int i)
    {
        cornerCache[i][0] = a;
        cornerCache[i][1] = b;
        cornerCache[i][2] = c;
    }
}