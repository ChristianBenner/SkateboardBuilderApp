package com.games.crispin.skateboardbuilderapp.SkateboardComponents;

/**
 * ComponentBase class is a base class for all of the different skateboard component data classes.
 * It contains some data types used by all skateboard component classes such as name and info.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
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

    /**
     * Construct the object. The constructor is protected so that the object can only be created by
     * a deriving class
     *
     * @since   1.0
     */
    protected ComponentBase()
    {
        // Cannot be initialised unless inherited from
    }
}
