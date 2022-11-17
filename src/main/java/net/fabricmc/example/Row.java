package net.fabricmc.example;

public class Row
{
    private static RenderableButton[] buttons;

    public static RenderableButton[] getButtons()
    {
        return buttons;
    }

    public static void setButtons(RenderableButton[] buttons)
    {
        Row.buttons = buttons;
    }
}