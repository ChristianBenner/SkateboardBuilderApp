package com.games.crispin.skateboardbuilderapp;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Scale2D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;

public class Constants
{
    // The background colour of the application
    public static final Colour BACKGROUND_COLOR = new Colour(57, 142, 178);

    // Padding for the back button
    public static final Point2D BACK_BUTTON_PADDING = new Point2D(50.0f, 50.0f);

    // Size of the back button
    public static final Scale2D BACK_BUTTON_SIZE = new Scale2D(150.0f, 150.0f);

    // Padding for the info button
    public static final Point2D INFO_BUTTON_PADDING = new Point2D(50.0f, 50.0f);

    // Size of the info button
    public static final Scale2D INFO_BUTTON_SIZE = new Scale2D(150.0f, 150.0f);

    // Padding for the next button
    public static final Point2D NEXT_BUTTON_PADDING = new Point2D(0.0f, 50.0f);

    // Size of the next button
    public static final Scale2D NEXT_BUTTON_SIZE = new Scale2D(600.0f, 200.0f);

    // Selected object text padding
    public static final Point2D SELECTED_OBJECT_TEXT_PADDING = new Point2D(0.0f, 50.0f);

    // Padding for the select deck width dropdown
    public static final Point2D SELECT_DECK_WIDTH_DROPDOWN_PADDING = new Point2D(50.0f,
            50.0f);

    // Size for the select deck width dropdown
    public static final Scale2D SELECT_DECK_WIDTH_DROPDOWN_SIZE = new Scale2D(
            Crispin.getSurfaceWidth() - (SELECT_DECK_WIDTH_DROPDOWN_PADDING.x * 2.0f),
            100.0f);

    public static final Point2D getBackButtonPosition()
    {
        return new Point2D(BACK_BUTTON_PADDING.x,Crispin.getSurfaceHeight() -
                BACK_BUTTON_PADDING.y - BACK_BUTTON_SIZE.y);
    }

    public static final Point2D getInfoButtonPosition()
    {
        return new Point2D(Crispin.getSurfaceWidth() - Constants.INFO_BUTTON_SIZE.x -
                Constants.INFO_BUTTON_PADDING.x, getBackButtonPosition().y);
    }

    public static final Point2D getNextButtonPosition()
    {
        return new Point2D((Crispin.getSurfaceWidth() / 2.0f) - (Constants.NEXT_BUTTON_SIZE.x /
                2.0f) + Constants.NEXT_BUTTON_PADDING.x, Constants.NEXT_BUTTON_PADDING.y);
    }
}
