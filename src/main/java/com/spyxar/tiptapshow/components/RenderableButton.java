package com.spyxar.tiptapshow.components;

import com.spyxar.tiptapshow.ClickCounter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import static com.spyxar.tiptapshow.TipTapShowMod.config;

public class RenderableButton
{
    private final int x;
    private final int y;
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

    private static String getDisplayText(KeyBinding key)
    {
        if (key.equals(MinecraftClient.getInstance().options.attackKey))
        {
            return Text.translatable("text.tiptapshow.lmb").getString() + "\n" + ClickCounter.getLeftCps() + Text.translatable("text.tiptapshow.cps").getString();
        }
        else if (key.equals(MinecraftClient.getInstance().options.useKey))
        {
            return Text.translatable("text.tiptapshow.rmb").getString() + "\n" + ClickCounter.getRightCps() + Text.translatable("text.tiptapshow.cps").getString();
        }
        //ToDo: Make this a continuous line
        else if (key.equals(MinecraftClient.getInstance().options.jumpKey))
        {
            return "---";
        }
        return key.getBoundKeyLocalizedText().getString().toUpperCase();
    }

    public void render(MatrixStack matrixStack)
    {
        //ToDo: Check for potential optimization
        boolean isPressed = key.isPressed();
        int fillColor = isPressed ? config.pressedBackgroundColor : config.backgroundColor;
        int textColor = isPressed ? config.pressedKeyColor : config.keyColor;

        MinecraftClient client = MinecraftClient.getInstance();
        DrawableHelper.fill(matrixStack, x, y, x + width, y + height, fillColor);

        if (displayText.contains("\n"))
        {
            String[] splitText = displayText.split("\n", 2);
            String firstPart = splitText[0];
            String secondPart = splitText[1];
            float firstLineX = width / 2F + x - client.textRenderer.getWidth(firstPart) / 2F;
            float secondLineX = width / 2F + x - client.textRenderer.getWidth(secondPart) / 2F;
            float lineY = height / 2F + y - client.textRenderer.fontHeight * 2 / 2F;
            if (config.keyShadow)
            {
                client.textRenderer.drawWithShadow(matrixStack, firstPart, firstLineX, lineY, textColor);
                client.textRenderer.drawWithShadow(matrixStack, secondPart, secondLineX, lineY + client.textRenderer.fontHeight, textColor);
                return;
            }
            client.textRenderer.draw(matrixStack, firstPart, firstLineX, lineY, textColor);
            client.textRenderer.draw(matrixStack, secondPart, secondLineX, lineY + client.textRenderer.fontHeight, textColor);
            return;
        }

        int letterWidth = client.textRenderer.getWidth(displayText);
        float lineX = width / 2F + x - letterWidth / 2F;
        float lineY = height / 2F + y - client.textRenderer.fontHeight / 2F;
        if (config.keyShadow)
        {
            client.textRenderer.drawWithShadow(matrixStack, displayText, lineX, lineY, textColor);
        }
        else
        {
            client.textRenderer.draw(matrixStack, displayText, lineX, lineY, textColor);
        }
    }
}