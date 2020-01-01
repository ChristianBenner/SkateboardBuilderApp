package com.games.crispin.skateboardbuilderapp.SkateboardComponents;

import com.games.crispin.crispinmobile.Geometry.Point3D;

public class Truck extends ComponentBase
{
    // When no price has been provided
    public static final float NO_PRICE = 0.0f;

    // ID of the texture resource for the design
    public int resourceId = NO_ID;

    // ID of the model resource
    public int modelResourceId = NO_ID;

    // The price of the design
    public float price = NO_PRICE;

    public Point3D wheelRelativePosition = new Point3D();
    public Point3D bearingOneRelativePosition = new Point3D();
    public Point3D bearingTwoRelativePosition = new Point3D();
}
