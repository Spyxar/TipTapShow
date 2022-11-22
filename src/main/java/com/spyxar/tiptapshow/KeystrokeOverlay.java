package com.spyxar.tiptapshow;

import com.spyxar.tiptapshow.components.Row;
import com.spyxar.tiptapshow.config.TipTapShowConfig;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.math.MatrixStack;

public class KeystrokeOverlay implements HudRenderCallback
{
    private static final int STANDARD_SIDE_OFFSET = 25;
    private static final int STANDARD_TOP_OFFSET = 25;
    public static final int BOX_SIZE = 25;
    public static final int ROW_SEPARATOR_SIZE = 1;
    public static final int ROW_WIDTH = 77;
    public static final int ROW_HEIGHT_NORMAL = 25;
    public static final int ROW_HEIGHT_SMALL = 20;

    @SuppressWarnings("RedundantArrayCreation")
    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta)
    {
        MinecraftClient client = MinecraftClient.getInstance();
        TipTapShowConfig config = TipTapShowConfig.loadConfig();
        if (client == null)
        {
            TipTapShowMod.LOGGER.error("Client was null, making all renders fail.");
            return;
        }
        if (client.options.debugEnabled)
        {
            return;
        }
        if (!config.renderInGui && client.currentScreen != null)
        {
            return;
        }
        //ToDo: make use of dynamic display values from the config
        // We probably will not have to use client.getWindow().getScaledWidth()/getScaledWidth()
        // because people will just be able to change the display to their liking and depending on their GUI scale/fullscreen
        // However we may need to do some more math for the text to still be in the middle with a different GUI size, needs testing

        matrixStack.push();
        //ToDo: the height stuff is ugly
        Row row1 = new Row(STANDARD_SIDE_OFFSET, STANDARD_TOP_OFFSET, ROW_WIDTH, ROW_HEIGHT_NORMAL, new KeyBinding[]{null, client.options.forwardKey, null});
        Row row2 = new Row(STANDARD_SIDE_OFFSET, STANDARD_TOP_OFFSET + row1.height + ROW_SEPARATOR_SIZE, ROW_WIDTH, ROW_HEIGHT_NORMAL, new KeyBinding[]{client.options.leftKey, client.options.backKey, client.options.rightKey});
        Row row3 = new Row(STANDARD_SIDE_OFFSET, STANDARD_TOP_OFFSET + row1.height + row2.height + ROW_SEPARATOR_SIZE * 2, ROW_WIDTH, ROW_HEIGHT_NORMAL, new KeyBinding[]{client.options.attackKey, client.options.useKey});
        Row row4 = new Row(STANDARD_SIDE_OFFSET, STANDARD_TOP_OFFSET + row1.height + row2.height + row3.height + ROW_SEPARATOR_SIZE * 3, ROW_WIDTH, ROW_HEIGHT_SMALL, new KeyBinding[]{client.options.jumpKey});
        row1.render(matrixStack);
        row2.render(matrixStack);
        row3.render(matrixStack);
        row4.render(matrixStack);
        matrixStack.pop();
    }
}