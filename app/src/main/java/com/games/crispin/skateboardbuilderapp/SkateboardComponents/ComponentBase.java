package com.games.crispin.skateboardbuilderapp.SkateboardComponents;

public class ComponentBase
{
    // When no ID has been provided for ID related data in the design class
    public static final int NO_ID = -1;

    // When no text has been provided for text related data in the design class
    public static final String NO_TEXT = "null";

    // ID of the component. This is used when referencing to a specific design
    public int id = NO_ID;

    // Name of the design
    public String name = NO_TEXT;

    // Some information on the design
    public String info = NO_TEXT;

    protected ComponentBase()
    {
        // Cannot be initialised unless inherited from
    }
}
