package net.fabricmc.example;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;

public class RenderableButton
{
    private int x;
    private int y;
    private int width;
    private int height;
    private KeyBinding key;
    private boolean isMouseButton;

    public RenderableButton(int x, int y, int width, int height, KeyBinding key)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.key = key;
        this.isMouseButton = false;
    }

    public RenderableButton(int x, int y, int width, int height, KeyBinding key, boolean isMouseButton)
    {
        //ToDo how to know what mouse button is which (l or r)
        //renderablemousebutton class? would make sense cuz its also bigger etc
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.key = null;
        this.isMouseButton = isMouseButton;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public KeyBinding getKey()
    {
        return key;
    }

    public void setKey(KeyBinding key)
    {
        this.key = key;
    }

    public boolean isMouseButton()
    {
        return isMouseButton;
    }

    public void setMouseButton(boolean mouseButton)
    {
        isMouseButton = mouseButton;
    }
}