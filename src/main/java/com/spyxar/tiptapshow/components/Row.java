package com.spyxar.tiptapshow.components;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.math.MatrixStack;

public class Row
{
    private static final int SEPARATOR_SIZE = 1;

    private final RenderableButton[] buttons;
    private final int columnCount;

    private final int x;
    private final int y;
    private final int width;
    public final int height;

    public Row(int x, int y, int width, int height, KeyBinding... keyBindings)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        buttons = createRenderableButtonsForKeyBindings(keyBindings);
        columnCount = buttons.length;
    }

    public RenderableButton[] createRenderableButtonsForKeyBindings(KeyBinding[] keyBindings)
    {
        //        STANDARD_SIDE_OFFSET + 1 * BOX_SIZE + 1, 50, BOX_SIZE, BOX_SIZE,
        RenderableButton[] renderableButtons = new RenderableButton[keyBindings.length];
        int amountOfButtons = keyBindings.length;
        int amountOfSeparators = amountOfButtons - 1;
        int buttonWidth = (this.width - amountOfSeparators * SEPARATOR_SIZE) / amountOfButtons;
        for (int i = 0; i < keyBindings.length; i++)
        {
            KeyBinding keyBinding = keyBindings[i];
            if (keyBinding == null)
            {
                continue;
            }
            int buttonX = this.x + buttonWidth * i + i * SEPARATOR_SIZE;
            int buttonY = this.y;
            int buttonHeight = this.height;

            renderableButtons[i] = new RenderableButton(buttonX, buttonY, buttonWidth, buttonHeight, keyBinding);
        }

        return renderableButtons;
    }

    public void render(MatrixStack matrixStack)
    {
        for (RenderableButton button : buttons)
        {
            if (button == null)
            {
                continue;
            }
            button.render(matrixStack);
        }
    }
}