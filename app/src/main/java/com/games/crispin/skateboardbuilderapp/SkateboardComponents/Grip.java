package com.games.crispin.skateboardbuilderapp.SkateboardComponents;

/**
 * The grip class is a data only structure that holds information about a griptape component such as
 * a texture resource ID. It extends the <code>ComponentBase</code> class.
 *
 * @see         ComponentBase
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
public class Grip extends ComponentBase
{
    // When no price has been provided
    public static final float NO_PRICE = 0.0f;

    // ID of the texture resource for the grip
    public int resourceId = NO_ID;

    // The price of the grip
    public float price = NO_PRICE;
}
