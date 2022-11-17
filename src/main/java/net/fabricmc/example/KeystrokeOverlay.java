package net.fabricmc.example;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

public class KeystrokeOverlay implements HudRenderCallback
{
    //private Row[] rows = config.getrows;
    private static final int STANDARD_SIDE_OFFSET = 25;
    private static final int STANDARD_TOP_OFFSET = 25;
    private static final int BOX_SIZE = 25;

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta)
    {
        int x = 0;
        int y = 0;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null)
        {
            ExampleMod.LOGGER.error("Client was null, making all renders fail.");
            return;
        }
        //ToDo: just add basic x and y, then dynamic
        // also use scaledwidth and height to make the squares the right size
        //ToDo: also get vertical exact mid
        if (client != null)
        {
            int width = client.getWindow().getScaledWidth();
            int height = client.getWindow().getScaledHeight();

            x = width / 2;
            y = height;
        }
        //a row object would be good, so we dont have to do the +1/2/3 and *1/2/3 but can instead use a foreach
        //and have default row offsets from eachother

        //instead of binding a text, bind a keybind so we can check if its pressed or not
//        RenderableButton forwardButton = new RenderableButton(STANDARD_SIDE_OFFSET, 25, BOX_SIZE, BOX_SIZE, client.options.forwardKey);
        //mouse button size: 25 + 25 + 25 + 2 = 77
        //so 77 - 1 / 2 is the size per mouse button
        RenderableButton forwardButton = new RenderableButton(STANDARD_SIDE_OFFSET + 2 * BOX_SIZE + 2, 25 - 1, BOX_SIZE, BOX_SIZE, client.options.forwardKey);
        RenderableButton leftButton = new RenderableButton(STANDARD_SIDE_OFFSET + 1 * BOX_SIZE + 1, 50, BOX_SIZE, BOX_SIZE, client.options.leftKey);
        RenderableButton backButton = new RenderableButton(STANDARD_SIDE_OFFSET + 2 * BOX_SIZE + 2, 50, BOX_SIZE, BOX_SIZE, client.options.backKey);
        RenderableButton rightButton = new RenderableButton(STANDARD_SIDE_OFFSET + 3 * BOX_SIZE + 3, 50, BOX_SIZE, BOX_SIZE, client.options.rightKey);
        RenderableButton leftMouseButton = new RenderableButton(STANDARD_SIDE_OFFSET + 1 * BOX_SIZE + 1, 75 + 1, BOX_SIZE, BOX_SIZE, null, true);
        RenderableButton rightMouseButton = new RenderableButton(STANDARD_SIDE_OFFSET + 3 * BOX_SIZE + 3, 75 + 1, BOX_SIZE, BOX_SIZE, null, true);
        matrixStack.push();
        int offset = 25;
        int letterWidth = client.textRenderer.getWidth("A");
        int textHeight = client.textRenderer.fontHeight;
        //setup: 0x + rrggbb + alpha
//        DrawableHelper.fill(matrixStack, offset, offset, offset + BOX_SIZE, offset + BOX_SIZE, 0x131314b3);
//        client.textRenderer.draw(matrixStack, forwardButton.getText(), BOX_SIZE / 2F + offset - letterWidth / 2F, BOX_SIZE / 2F + offset - textHeight / 2F, 16777215);

        //Todo: render all buttons at the same time, same method because this is laggy when button is pressed
        renderButton(forwardButton, matrixStack);
        renderButton(leftButton, matrixStack);
        renderButton(backButton, matrixStack);
        renderButton(rightButton, matrixStack);
        renderButton(leftMouseButton, matrixStack);
        renderButton(rightMouseButton, matrixStack);

        matrixStack.pop();
    }

    public void renderButton(RenderableButton button, MatrixStack matrixStack)
    {
        //correct way of getting the key: MinecraftClient.getInstance().options.sprintKey.getBoundKeyLocalizedText().getString().toUpperCase()
        //text = client.options.forwardKey.getDefaultKey().getLocalizedText().getString().toUpperCase()
        MinecraftClient client = MinecraftClient.getInstance();
        if (button.getKey().isPressed())
        {
            DrawableHelper.fill(matrixStack, button.getX(), button.getY(), button.getX() + button.getWidth(), button.getY() + button.getHeight(), 0x131314b3);
            int letterWidth = client.textRenderer.getWidth(button.getKey().getBoundKeyLocalizedText().getString().toUpperCase());
            client.textRenderer.draw(matrixStack, button.getKey().getBoundKeyLocalizedText().getString().toUpperCase(), BOX_SIZE / 2F + button.getX() - letterWidth / 2F, BOX_SIZE / 2F + button.getY() - client.textRenderer.fontHeight / 2F, 16711680);
            return;
        }
        DrawableHelper.fill(matrixStack, button.getX(), button.getY(), button.getX() + button.getWidth(), button.getY() + button.getHeight(), 0x131314b3);
//        int letterWidth = client.textRenderer.getWidth(button.getKey().getDefaultKey().getLocalizedText().getString().toUpperCase());
        int letterWidth = client.textRenderer.getWidth(button.getKey().getBoundKeyLocalizedText().getString().toUpperCase());
//        client.textRenderer.draw(matrixStack, button.getText(), BOX_SIZE / 2F + offset - letterWidth / 2F, BOX_SIZE / 2F + offset - client.textRenderer.fontHeight / 2F, 16777215);
//        client.textRenderer.draw(matrixStack, button.getKey(), BOX_SIZE / 2F + button.getX() - letterWidth / 2F, BOX_SIZE / 2F + button.getY() - client.textRenderer.fontHeight / 2F, 16777215);
        client.textRenderer.draw(matrixStack, button.getKey().getBoundKeyLocalizedText().getString().toUpperCase(), BOX_SIZE / 2F + button.getX() - letterWidth / 2F, BOX_SIZE / 2F + button.getY() - client.textRenderer.fontHeight / 2F, 16777215);
    }
}