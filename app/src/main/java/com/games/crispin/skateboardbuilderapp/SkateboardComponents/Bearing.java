package com.games.crispin.skateboardbuilderapp.SkateboardComponents;

/**
 * The bearing class is a data only structure that holds information about a bearing skateboard
 * component such as the price and texture resource ID. It extends the <code>ComponentBase</code>
 * class.
 *
 * @see         ComponentBase
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
public class Bearing extends ComponentBase
{
    // When no price has been provided
    public static final float NO_PRICE = 0.0f;

    // ID of the texture resource for the design
    public int resourceId = NO_ID;

    // The price of the design
    public float price = NO_PRICE;
}
