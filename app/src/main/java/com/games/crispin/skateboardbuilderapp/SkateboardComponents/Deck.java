package com.games.crispin.skateboardbuilderapp.SkateboardComponents;

import com.games.crispin.crispinmobile.Geometry.Point3D;

/**
 * The deck class is a data only structure that holds information about a skateboard deck component
 * such as a model resource ID. It extends the <code>ComponentBase</code> class.
 *
 * @see         ComponentBase
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
public class Deck extends ComponentBase
{
    // Model resource ID of the deck
    public int modelId = NO_ID;

    // Model resource ID of the grip
    public int gripModelId = NO_ID;

    // Location of the first truck relative to the deck model
    public Point3D truckOneRelativePosition = new Point3D();

    // Location of the second truck relative to the deck model
    public Point3D truckTwoRelativePosition = new Point3D();
}
