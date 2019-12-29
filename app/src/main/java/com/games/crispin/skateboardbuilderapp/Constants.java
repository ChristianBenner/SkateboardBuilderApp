package com.games.crispin.skateboardbuilderapp;

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

    // Padding for the next button
    public static final Point2D NEXT_BUTTON_PADDING = new Point2D(0.0f, 50.0f);

    // Size of the next button
    public static final Scale2D NEXT_BUTTON_SIZE = new Scale2D(600.0f, 200.0f);
}
