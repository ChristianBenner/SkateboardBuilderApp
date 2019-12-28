package com.games.crispin.skateboardbuilderapp.SkateboardComponents;

/**
 * A data only class that is used to store valuable information on designs.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
public class Design
{
    // When no ID has been provided for ID related data in the design class
    public static final int NO_ID = -1;

    // When no text has been provided for text related data in the design class
    public static final String NO_TEXT = "null";

    // ID of the component. This is used when referencing to a specific design
    public int id = NO_ID;

    // ID of the deck width component that is compatible with this design component
    public int deckId = NO_ID;

    // ID of the texture resource for the design
    public int resourceId = NO_ID;

    // Name of the design
    public String name = NO_TEXT;

    // Some information on the design
    public String info = NO_TEXT;
}
