package com.spyxar.tiptapshow;

public class Row
{
    private RenderableButton[] buttons;

    public Row(RenderableButton... button)
    {
        buttons = button;
    }

    public RenderableButton[] getButtons()
    {
        return buttons;
    }

    public void setButtons(RenderableButton[] buttons)
    {
        this.buttons = buttons;
    }
}