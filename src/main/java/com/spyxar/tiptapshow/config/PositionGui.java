package com.spyxar.tiptapshow.config;

import com.spyxar.tiptapshow.TipTapShowMod;
import com.spyxar.tiptapshow.components.Row;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.util.Window;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.function.Consumer;

public class PositionGui extends Screen
{
    public static final int ROW_SEPARATOR_SIZE = 1;
    public static final int ROW_WIDTH = 77;
    public static final int ROW_HEIGHT_NORMAL = 25;
    public static final int ROW_HEIGHT_SMALL = 20;

    private PositionWidget widget;

    private boolean dragging;
    private int dragX;
    private int dragY;

    private final Screen parent;

    public PositionGui(Text title, Screen previous)
    {
        super(title);
        parent = previous;
    }

    @Override
    protected void init()
    {
        TipTapShowConfig config = TipTapShowMod.config;

        if (client == null)
        {
            TipTapShowMod.LOGGER.error("Client was null, failed to open positioning screen.");
            return;
        }

        widget = new PositionWidget((int) (config.horizontalSlider / client.getWindow().getScaleFactor()), (int) (config.verticalSlider / client.getWindow().getScaleFactor()), 200, 50);
        this.addSelectableChild(widget);

        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, (button) -> close())
                .position(this.width / 2 - 100, this.height - 27)
                .size(200, 20)
                .build());
    }

    @Override
    public void close()
    {
        if (client == null)
        {
            TipTapShowMod.LOGGER.error("Client was null, failed to save position and close screen.");
            return;
        }
        client.setScreen(parent);

        TipTapShowConfig config = TipTapShowMod.config;
        config.horizontalSlider = (int) (widget.getX() * client.getWindow().getScaleFactor());
        config.verticalSlider = (int) (widget.getY() * client.getWindow().getScaleFactor());
        config.saveConfig();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta)
    {
        super.renderBackground(context, mouseX, mouseY, delta);

        widget.render(context, mouseX, mouseY, delta);

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {}

    @Override
    public void mouseMoved(double mouseX, double mouseY)
    {
        if (dragging)
        {
            Window window = MinecraftClient.getInstance().getWindow();

            int maxX = window.getScaledWidth() - widget.getWidth();
            int newX = Math.max(0, Math.min((int) (mouseX - dragX), maxX));

            int maxY = window.getScaledHeight() - widget.getHeight();
            int newY = Math.max(0, Math.min((int) (mouseY - dragY), maxY));

            widget.setX(newX);
            widget.setY(newY);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        if (button == 0)
        {
            if (isHoveringOverWidget(mouseX, mouseY))
            {
                dragging = true;
                dragX = (int) (mouseX - widget.getX());
                dragY = (int) (mouseY - widget.getY());
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button)
    {
        if (button == 0 && dragging)
        {
            dragging = false;
        }
        return true;
    }

    public boolean isHoveringOverWidget(double mouseX, double mouseY)
    {
        return mouseX > widget.getX() && mouseX < widget.getX() + widget.getWidth() && mouseY > widget.getY() && mouseY < widget.getY() + widget.getHeight();
    }

    class PositionWidget implements Drawable, Element, Widget, Selectable
    {
        protected int width;
        protected int height;
        private int x;
        private int y;

        private boolean focused;
        protected boolean hovered;

        public PositionWidget(int x, int y, int width, int height)
        {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta)
        {
            this.hovered = mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;

            TipTapShowConfig config = TipTapShowConfig.loadConfig();
            MinecraftClient client = MinecraftClient.getInstance();
            if (client == null)
            {
                TipTapShowMod.LOGGER.error("Client was null, making all renders fail.");
                return;
            }

            ArrayList<Row> unfinishedRows = new ArrayList<>();
            int updatedRowWidth = (int) (ROW_WIDTH * config.displayFactor);
            int updatedRowHeightNormal = (int) (ROW_HEIGHT_NORMAL * config.displayFactor);
            int updatedRowHeightSmall = (int) (ROW_HEIGHT_SMALL * config.displayFactor);
            while ((updatedRowWidth - 2) % 3 != 0 || (updatedRowWidth - 1) % 2 != 0)
            {
                updatedRowWidth++;
            }
            if (config.showMovement)
            {
                unfinishedRows.add(new Row(x, 0, updatedRowWidth, updatedRowHeightNormal, null, client.options.forwardKey, null));
                unfinishedRows.add(new Row(x, 0, updatedRowWidth, updatedRowHeightNormal, client.options.leftKey, client.options.backKey, client.options.rightKey));
            }
            if (config.showClick)
            {
                unfinishedRows.add(new Row(x, 0, updatedRowWidth, updatedRowHeightNormal, client.options.attackKey, client.options.useKey));
            }
            if (config.showJump)
            {
                unfinishedRows.add(new Row(x, 0, updatedRowWidth, updatedRowHeightSmall, client.options.jumpKey));
            }

            ArrayList<Row> rows = new ArrayList<>();
            //Height calculations
            for (int i = 0; i < unfinishedRows.size(); i++)
            {
                Row row = unfinishedRows.get(i);
                int height = this.y + ROW_SEPARATOR_SIZE * i;
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

            int rowHeight = 0;
            for (Row row : rows)
            {
                rowHeight += row.getHeight();
            }
            setHeight(rowHeight);
            setWidth(updatedRowWidth);

            context.fill(this.x, this.y, this.x + updatedRowWidth, rows.get(rows.size() - 1).getY() + rows.get(rows.size() - 1).getHeight(), 0x373dff47);
            Identifier texture = new Identifier(TipTapShowMod.MOD_ID, "textures/positionguitexture.png");
            //Top left
            context.drawTexture(texture, this.x - 4, this.y - 4,
                    16, 16,
                    0, 0, 6, 6, 16, 16);
            //Top right
            context.drawTexture(texture, this.x + this.width - 12, this.y - 4,
                    16, 16,
                    10, 0, 6, 6, 16, 16);
            //Bottom Left
            context.drawTexture(texture, this.x - 4, this.y + this.height - 9,
                    16, 16,
                    0, 10, 6, 6, 16, 16);
            //Bottom Right
            context.drawTexture(texture, this.x + this.width - 12, this.y + this.height - 9,
                    16, 16,
                    10, 10, 6, 6, 16, 16);
        }

        @Override
        public boolean isMouseOver(double mouseX, double mouseY)
        {
            return mouseX >= (double) this.getX() && mouseY >= (double) this.getY() && mouseX < (double) (this.getX() + this.width) && mouseY < (double) (this.getY() + this.height);
        }

        @Override
        public void setFocused(boolean focused)
        {
            this.focused = focused;
        }

        @Override
        public boolean isFocused()
        {
            return this.focused;
        }

        public boolean isHovered()
        {
            return this.hovered;
        }

        @Override
        public ScreenRect getNavigationFocus()
        {
            return Element.super.getNavigationFocus();
        }

        @Override
        public SelectionType getType()
        {
            if (this.isFocused())
            {
                return SelectionType.FOCUSED;
            }
            else
            {
                return this.hovered ? SelectionType.HOVERED : SelectionType.NONE;
            }
        }

        @Override
        public void setX(int x)
        {
            this.x = x;
        }

        @Override
        public void setY(int y)
        {
            this.y = y;
        }

        @Override
        public int getX()
        {
            return this.x;
        }

        @Override
        public int getY()
        {
            return this.y;
        }

        public void setWidth(int width)
        {
            this.width = width;
        }

        @Override
        public int getWidth()
        {
            return this.width;
        }

        public void setHeight(int height)
        {
            this.height = height;
        }

        @Override
        public int getHeight()
        {
            return this.height;
        }

        @Override
        public void forEachChild(Consumer<ClickableWidget> consumer) {}

        @Override
        public void appendNarrations(NarrationMessageBuilder builder) {}
    }
}