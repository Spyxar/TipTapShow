package com.spyxar.tiptapshow;

import com.spyxar.tiptapshow.components.Row;
import com.spyxar.tiptapshow.config.TipTapShowConfig;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;

public class KeystrokeOverlay implements HudRenderCallback
{
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

        matrixStack.push();
        ArrayList<Row> unfinishedRows = new ArrayList<>();
        int x = (int) (config.horizontalSlider / client.getWindow().getScaleFactor());
        if (config.showMovement)
        {
            unfinishedRows.add(new Row(x, 0, ROW_WIDTH, ROW_HEIGHT_NORMAL, new KeyBinding[]{null, client.options.forwardKey, null}));
            unfinishedRows.add(new Row(x, 0, ROW_WIDTH, ROW_HEIGHT_NORMAL, new KeyBinding[]{client.options.leftKey, client.options.backKey, client.options.rightKey}));
        }
        if (config.showClick)
        {
            unfinishedRows.add(new Row(x, 0, ROW_WIDTH, ROW_HEIGHT_NORMAL, new KeyBinding[]{client.options.attackKey, client.options.useKey}));
        }
        if (config.showJump)
        {
            unfinishedRows.add(new Row(x, 0, ROW_WIDTH, ROW_HEIGHT_SMALL, new KeyBinding[]{client.options.jumpKey}));
        }

        ArrayList<Row> rows = new ArrayList<>();
        //Height calculations
        for (int i = 0; i < unfinishedRows.size(); i++)
        {
            Row row = unfinishedRows.get(i);
            int height = (int) (config.verticalSlider / client.getWindow().getScaleFactor()) + ROW_SEPARATOR_SIZE * i;
            for (int j = 0; j < i; j++)
            {
                height += rows.get(j).getHeight();
            }
            row.setY(height);
            rows.add(row);
        }
        for (Row row : rows)
        {
            row.render(matrixStack);
        }
        matrixStack.pop();
    }
}