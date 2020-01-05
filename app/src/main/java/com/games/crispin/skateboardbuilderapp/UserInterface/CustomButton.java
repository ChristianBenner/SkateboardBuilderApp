package com.games.crispin.skateboardbuilderapp.UserInterface;

import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.UserInterface.Font;
import com.games.crispin.crispinmobile.Rendering.Utilities.Texture;
import com.games.crispin.crispinmobile.UserInterface.Button;

/**
 * A custom button user interface. When the button is being held down, the blue and green colour
 * channels are lowered so that the remaining red colour channel makes the icon appear red. The
 * class extends the CrispinMobile modules Button class.
 *
 * @see         Button
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
public class CustomButton extends Button
{
    // The rate of change to the green and blue colour chanel intensities per update cycle
    private static final float RATE_OF_COLOUR_CHANGE = 0.05f;

    // The maximum intensity for the colour channels
    private static final float MAXIMUM_COLOUR_CHANNEL_INTENSITY = 1.0f;

    // The amount of colour shift in the green and blue colour channels
    private float greenBlueColourShift;

    /**
     * Constructor to create the custom user interface object with a specified font and text.
     *
     * @param font  The font for the specified text to be styled in
     * @param text  The text string to display over the button
     * @since       1.0
     */
    public CustomButton(Font font, String text)
    {
        super(font, text);
        greenBlueColourShift = 0.0f;
    }

    /**
     * Constructor to create the custom user interface object with a specified texture
     *
     * @param texture   The texture to apply to the button
     * @since           1.0
     */
    public CustomButton(Texture texture)
    {
        super(texture);
        greenBlueColourShift = 0.0f;
    }

    /**
     * Constructor to create the custom user interface object with a texture built from the
     * specified resource ID
     *
     * @param resourceId    The resource ID to create a texture of that will be applied to the
     *                      button
     * @since               1.0
     */
    public CustomButton(int resourceId)
    {
        super(resourceId);
        greenBlueColourShift = 0.0f;
    }

    /**
     * Update the button. This will update the colour of the button and fade it to red over time.
     *
     * @param deltaTime A multiplier for timing. Used to calculate a value correctly over time
     * @since           1.0
     */
    public void update(float deltaTime)
    {
        // If the button is currently being held, decrease the intensity of the green and blue
        // colour channels, otherwise increase it.
        if(super.isClicked())
        {
            // If the colour shift can be reduced, reduce it by the rate of change
            if(greenBlueColourShift <= 0.0f)
            {
                greenBlueColourShift = 0.0f;
            }
            else
            {
                greenBlueColourShift -= RATE_OF_COLOUR_CHANGE * deltaTime;
            }
        }
        else
        {
            // If the colour shift can be increased, increase it by the rate of change
            if(greenBlueColourShift >= MAXIMUM_COLOUR_CHANNEL_INTENSITY)
            {
                greenBlueColourShift = MAXIMUM_COLOUR_CHANNEL_INTENSITY;
            }
            else
            {
                greenBlueColourShift += RATE_OF_COLOUR_CHANGE * deltaTime;
            }
        }

        // Apply the new colour to the button
        super.setColour(new Colour(MAXIMUM_COLOUR_CHANNEL_INTENSITY, greenBlueColourShift,
                greenBlueColourShift));
    }
}
