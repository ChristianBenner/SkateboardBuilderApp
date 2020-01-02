package com.games.crispin.skateboardbuilderapp;

import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.UserInterface.Font;
import com.games.crispin.crispinmobile.Rendering.Utilities.Texture;
import com.games.crispin.crispinmobile.UserInterface.Button;

public class CustomButton extends Button
{
    private float greenBlueColourShift;

    public CustomButton(Font font, String text)
    {
        super(font, text);
        greenBlueColourShift = 0.0f;
    }

    public CustomButton(Texture texture)
    {
        super(texture);
        greenBlueColourShift = 0.0f;
    }

    public CustomButton(int resourceId)
    {
        super(resourceId);
        greenBlueColourShift = 0.0f;
    }

    public void update(float deltaTime)
    {
        if(super.isClicked())
        {
            if(greenBlueColourShift <= 0.0f)
            {
                greenBlueColourShift = 0.0f;
            }
            else
            {
                greenBlueColourShift -= 0.05f;
            }

            super.setColour(new Colour(1.0f, greenBlueColourShift, greenBlueColourShift));
        }
        else
        {
            if(greenBlueColourShift >= 1.0f)
            {
                greenBlueColourShift = 1.0f;
            }
            else
            {
                greenBlueColourShift += 0.05f;
            }

            super.setColour(new Colour(1.0f, greenBlueColourShift, greenBlueColourShift));
        }
    }
}
