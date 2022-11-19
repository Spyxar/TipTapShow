package com.spyxar.tiptapshow;

import net.minecraft.client.option.KeyBinding;

public class RenderableButton
{
    private int x;
    private int y;
    private int width;
    private int height;
    private KeyBinding key;
    private boolean isMouseButton;
    //need to know:
    // if its a mouse button, and left or right
    // if its the space bar
    // all other keys are rendered as the same box, however those listed above should be bigger

    public RenderableButton(int x, int y, int width, int height, KeyBinding key)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.key = key;
        this.isMouseButton = false;
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