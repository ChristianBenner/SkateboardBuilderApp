package com.games.crispin.skateboardbuilderapp.SkateboardComponents;

/**
 * A data only class that is used to store valuable information on designs. It extends the
 * <code>ComponentBase</code> class.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
public class Design extends ComponentBase
{
    // When no price has been provided
    public static final float NO_PRICE = 0.0f;

    // ID of the deck width component that is compatible with this design component
    public int deckId = NO_ID;

    // ID of the texture resource for the design
    public int resourceId = NO_ID;

    // The price of the design
    public float price = NO_PRICE;
}
