package com.spyxar.tiptapshow;

import com.google.common.collect.Lists;

import java.util.Queue;

public class ClickCounter
{
    private static final Queue<Long> leftClicks = Lists.newLinkedList();
    private static final Queue<Long> rightClicks = Lists.newLinkedList();

    public static void registerLeftClick()
    {
        leftClicks.add(System.currentTimeMillis() + 1000L);
    }

    public static void registerRightClick()
    {
        rightClicks.add(System.currentTimeMillis() + 1000L);
    }

    public static int getLeftCps()
    {
        return getClicksFromQueue(leftClicks);
    }

    public static int getRightCps()
    {
        return getClicksFromQueue(rightClicks);
    }

    private static int getClicksFromQueue(Queue<Long> clickList)
    {
        while (!clickList.isEmpty() && clickList.peek() < System.currentTimeMillis())
        {
            clickList.remove();
        }
        return clickList.size();
    }
}