package com.games.crispin.skateboardbuilderapp.SkateboardComponents;

import com.games.crispin.crispinmobile.Geometry.Point3D;

/**
 * The truck class is a data only structure that holds information about a skateboard truck
 * component such as model and texture resource ID. It extends the <code>ComponentBase</code> class.
 *
 * @see         ComponentBase
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
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

    // The location of a wheel relative to the truck model
    public Point3D wheelRelativePosition = new Point3D();

    // The location of an outer bearing relative to the truck model
    public Point3D bearingOneRelativePosition = new Point3D();

    // The location of an inner bearing relative to the truck model
    public Point3D bearingTwoRelativePosition = new Point3D();
}
