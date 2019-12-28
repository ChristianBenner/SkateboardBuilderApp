package com.games.crispin.skateboardbuilderapp;

import java.lang.reflect.Field;

public class ResourceUtilities
{
    public static int getResourceIdFromName(String resourceName)
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
}
