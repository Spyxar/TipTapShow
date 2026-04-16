package com.spyxar.tiptapshow;

import com.spyxar.tiptapshow.components.Row;
import com.spyxar.tiptapshow.config.PositionGui;
import com.spyxar.tiptapshow.config.TipTapShowConfig;
//? if <1.21.8
/*import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;*/
//? if >=26.1 {
/*import net.minecraft.client.Minecraft;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.GuiGraphicsExtractor;
*///?} else {
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderTickCounter;
//?}

import java.util.ArrayList;

public class KeystrokeOverlay /*? <1.21.8 {*/ /*implements HudRenderCallback *//*?}*/
{
    public static final int ROW_SEPARATOR_SIZE = 1;
    public static final int ROW_WIDTH = 77;
    public static final int ROW_HEIGHT_NORMAL = 25;
    public static final int ROW_HEIGHT_SMALL = 20;

    @SuppressWarnings("RedundantArrayCreation")
    public void onHudRender(/*? >=26.1 {*/ /*GuiGraphicsExtractor context, DeltaTracker tickCounter*/ /*?} else {*/ DrawContext context, RenderTickCounter tickCounter /*?}*/)
    {
        TipTapShowConfig config = TipTapShowConfig.loadConfig();
        if (!config.isEnabled)
        {
            return;
        }
        //? if >=26.1 {
        /*Minecraft client = Minecraft.getInstance();
        *///?} else {
        MinecraftClient client = MinecraftClient.getInstance();
        //?}
        if (client == null)
        {
            TipTapShowMod.LOGGER.error("Client was null, making all renders fail.");
            return;
        }

        //? if >=26.1 {
        /*if (!config.renderInDebugHud && client.getDebugOverlay().showDebugScreen())
        {
            return;
        }
        if (client.screen instanceof PositionGui)
        {
            return;
        }
        if (!config.renderInGui && client.screen != null)
        {
            return;
        }
        if (!config.renderWhenPlayerListOpen && client.options.keyPlayerList.isDown() && !client.hasSingleplayerServer())
        {
            return;
        }
        if (client.options.hideGui)
        {
            return;
        }
        *///?} else {
        if (!config.renderInDebugHud && client.getDebugHud().shouldShowDebugHud())
        {
            return;
        }
        if (client.currentScreen instanceof PositionGui)
        {
            return;
        }
        if (!config.renderInGui && client.currentScreen != null)
        {
            return;
        }
        if (!config.renderWhenPlayerListOpen && client.options.playerListKey.isPressed() && !client.isConnectedToLocalServer())
        {
            return;
        }
        if (client.options.hudHidden)
        {
            return;
        }
        //?}

        ArrayList<Row> unfinishedRows = new ArrayList<>();
        //? if >=26.1 {
        /*int x = config.horizontalSlider / client.getWindow().getGuiScale();
        *///?} else if <1.21.8 {
        /*int x = (int) (config.horizontalSlider / client.getWindow().getScaleFactor());
        *///?} else {
        int x = config.horizontalSlider / client.getWindow().getScaleFactor();
        //?}
        int updatedRowWidth = (int) (ROW_WIDTH * config.displayFactor);
        int updatedRowHeightNormal = (int) (ROW_HEIGHT_NORMAL * config.displayFactor);
        int updatedRowHeightSmall = (int) (ROW_HEIGHT_SMALL * config.displayFactor);
        while ((updatedRowWidth - 2) % 3 != 0 || (updatedRowWidth - 1) % 2 != 0)
        {
            updatedRowWidth++;
        }
        //? if >=26.1 {
        /*if (config.showMovement)
        {
            unfinishedRows.add(new Row(x, 0, updatedRowWidth, updatedRowHeightNormal, new KeyMapping[]{null, client.options.keyUp, null}));
            unfinishedRows.add(new Row(x, 0, updatedRowWidth, updatedRowHeightNormal, new KeyMapping[]{client.options.keyLeft, client.options.keyDown, client.options.keyRight}));
        }
        if (config.showClick)
        {
            unfinishedRows.add(new Row(x, 0, updatedRowWidth, updatedRowHeightNormal, new KeyMapping[]{client.options.keyAttack, client.options.keyUse}));
        }
        if (config.showJump)
        {
            unfinishedRows.add(new Row(x, 0, updatedRowWidth, updatedRowHeightSmall, new KeyMapping[]{client.options.keyJump}));
        }
        *///?} else {
        if (config.showMovement)
        {
            unfinishedRows.add(new Row(x, 0, updatedRowWidth, updatedRowHeightNormal, new KeyBinding[]{null, client.options.forwardKey, null}));
            unfinishedRows.add(new Row(x, 0, updatedRowWidth, updatedRowHeightNormal, new KeyBinding[]{client.options.leftKey, client.options.backKey, client.options.rightKey}));
        }
        if (config.showClick)
        {
            unfinishedRows.add(new Row(x, 0, updatedRowWidth, updatedRowHeightNormal, new KeyBinding[]{client.options.attackKey, client.options.useKey}));
        }
        if (config.showJump)
        {
            unfinishedRows.add(new Row(x, 0, updatedRowWidth, updatedRowHeightSmall, new KeyBinding[]{client.options.jumpKey}));
        }
        //?}

        ArrayList<Row> rows = new ArrayList<>();
        //Height calculations
        for (int i = 0; i < unfinishedRows.size(); i++)
        {
            Row row = unfinishedRows.get(i);
            //? if >=26.1 {
            /*int height = (config.verticalSlider / client.getWindow().getGuiScale()) + ROW_SEPARATOR_SIZE * i;
            *///?} else if <1.21.8 {
            /*int height = (int) ((config.verticalSlider / client.getWindow().getScaleFactor()) + ROW_SEPARATOR_SIZE * i);
            *///?} else {
            int height = (config.verticalSlider / client.getWindow().getScaleFactor()) + ROW_SEPARATOR_SIZE * i;
             //?}
            for (int j = 0; j < i; j++)
            {
                height += rows.get(j).getHeight();
            }
            row.setY(height);
            rows.add(row);
        }
        for (Row row : rows)
        {
            row.render(context);
        }
    }
}
