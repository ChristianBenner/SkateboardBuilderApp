package com.games.crispin.skateboardbuilderapp;

import java.lang.reflect.Field;

public class ResourceUtilities
{
    public static int getDrawableResource(String resourceName)
    {
        try
        {
            Field idField = R.drawable.class.getDeclaredField(resourceName);
            return (idField).getInt(idField);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    public static int getRawResource(String resourceName)
    {
        try
        {
            Field idField = R.raw.class.getDeclaredField(resourceName);
            return (idField).getInt(idField);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }
    }
}
