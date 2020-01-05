package com.games.crispin.skateboardbuilderapp.UserInterface;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Geometry.Scale3D;
import com.games.crispin.crispinmobile.Rendering.Utilities.ModelMatrix;
import com.games.crispin.crispinmobile.Geometry.Scale2D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Rendering.Models.Square;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Material;
import com.games.crispin.crispinmobile.Rendering.Utilities.Texture;
import com.games.crispin.skateboardbuilderapp.R;

/**
 * A loading icon for the scenes. This will position and rotate an icon around the center of the
 * view.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
public class LoadingIcon
{
    // The default rotation speed
    private static final float DEFAULT_ROT_SPEED = 3.5f;

    // The default time increment per update cycle
    private static final float DEFAULT_TIME_INCREMENT = 0.07f;

    // The default size of the icon
    private static final Scale2D DEFAULT_SIZE = new Scale2D(240.0f, 240.0f);

    // The default position of the icon (in the center of the display)
    private static final Point2D DEFAULT_POSITION = new Point2D(
            (Crispin.getSurfaceWidth() / 2.0f) - (DEFAULT_SIZE.x / 2.0f),
            (Crispin.getSurfaceHeight() / 2.0f) - (DEFAULT_SIZE.y / 2.0f));

    // The square object that holds the loading icon
    private Square square;

    // The model matrix of the square object
    private ModelMatrix modelMatrix;

    // The current rotation angle of the loading icon
    private float rotationAngle;

    // The time that the loading icon has been rotating. This dictates the speed of the rotation
    private float rotationTime;

    // Whether or not to show the loading icon
    private boolean showLoadingIcon;

    /**
     * Construct, position and scale the loading icon
     *
     * @since 1.0
     */
    public LoadingIcon()
    {
        square = new Square(new Material(new Texture(R.drawable.load_icon)), true);
        modelMatrix = new ModelMatrix();
        rotationAngle = 0.0f;
        rotationTime = 0.0f;
        showLoadingIcon = false;

        setPosition(DEFAULT_POSITION);
        setSize(DEFAULT_SIZE);
    }

    /**
     * Show the loading icon
     *
     * @since 1.0
     */
    public void show()
    {
        showLoadingIcon = true;
    }

    /**
     * Hide the loading icon
     *
     * @since 1.0
     */
    public void hide()
    {
        showLoadingIcon = false;
    }

    /**
     * Set the new visibility state of the loading icon
     *
     * @param state The new visibility state of the loading icon
     * @since 1.0
     */
    public void setVisible(boolean state)
    {
        showLoadingIcon = state;
    }

    /**
     * Get the visibility state of the loading icon
     *
     * @return  Return the visibility state of the loading icon
     * @since   1.0
     */
    public boolean isVisible()
    {
        return showLoadingIcon;
    }

    /**
     * Get the visibility state of the loading icon
     *
     * @return  Return the visibility state of the loading icon
     * @since   1.0
     */
    public boolean isShown()
    {
        return showLoadingIcon;
    }

    /**
     * Set the new colour of the loading icon
     *
     * @param colour    The new colour of the loading icon
     * @since 1.0
     */
    public void setColour(Colour colour)
    {
        square.getMaterial().setColour(colour);
    }

    /**
     * Get the colour of the loading icon
     *
     * @return  The colour of the loading icon
     * @since   1.0
     */
    public Colour getColour()
    {
        return square.getMaterial().getColour();
    }

    /**
     * Set the position of the loading icon
     *
     * @param position  The new position of the loading icon
     * @since 1.0
     */
    public void setPosition(Point2D position)
    {
        square.setPosition(position);
    }

    /**
     * Get the position of the loading icon
     *
     * @return The position of the loading icon
     * @since   1.0
     */
    public Point2D getPosition()
    {
        return square.getPosition();
    }

    /**
     * Set the size of the loading icon
     *
     * @param size  The new size of the loading icon
     * @since 1.0
     */
    public void setSize(Scale2D size)
    {
        square.setScale(size);
    }

    /**
     * Get the size of the loading icon
     *
     * @return  The size of the loading icon
     * @since   1.0
     */
    public Scale2D getSize()
    {
        return square.getScale();
    }

    /**
     * Update the loading icon to rotate
     *
     * @param deltaTime A multiplier for timing. Used to calculate a value correctly over time
     * @since 1.0
     */
    public void update(float deltaTime)
    {
        // If the loading icon is being shown, update it
        if(showLoadingIcon)
        {
            // Position, rotate and scale the loading icon
            modelMatrix.reset();
            modelMatrix.translate(new Point3D((Crispin.getSurfaceWidth() / 2.0f) - 100.0f, (Crispin.getSurfaceHeight() / 2.0f) - 100.0f, 0.0f));
            modelMatrix.rotateAroundPoint(new Point3D(100.0f, 100.0f, 0.0f), rotationAngle, 0.0f, 0.0f, 1.0f);
            modelMatrix.scale(new Scale3D(200.0f, 200.0f, 1.0f));

            // Add to the rotation time
            rotationTime += DEFAULT_TIME_INCREMENT * deltaTime;

            // Add to the rotation angle
            rotationAngle += DEFAULT_ROT_SPEED + (((Math.sin(rotationTime) + 1) / 2.0f) * 5.0f);
        }
    }

    /**
     * Draw the loading icon
     *
     * @param camera2D  The 2D camera to draw the user interface with
     * @since 1.0
     */
    public void draw(Camera2D camera2D)
    {
        // If the loading icon is being shown, draw it
        if(showLoadingIcon)
        {
            square.render(camera2D, modelMatrix);
        }
    }
}
