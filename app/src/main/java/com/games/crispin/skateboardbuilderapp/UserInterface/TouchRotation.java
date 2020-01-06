package com.games.crispin.skateboardbuilderapp.UserInterface;

import android.view.MotionEvent;

import com.games.crispin.crispinmobile.Geometry.Point2D;

/**
 * This class was built to turn the users touch input into rotation values for rotating 3D models on
 * a scene. It takes user drag input and turns into 3 dimensional space rotation angles. It also has
 * been built to feel physically accurate with a velocity that decreases over time (simulating the
 * effect of air resistance with a drag co-efficient).
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
public class TouchRotation
{
    // The amount of samples to take in the velocity average
    private static final int MAX_SAMPLES = 5;

    // The co-efficient for air resistance/drag to apply to the velocity every update cycle
    private static final float DRAG_COEFFICIENT = 0.05f;

    // Rotation scale to control the speed of the rotation based on user drag input
    private static final float ROTATION_SCALE = 0.2f;

    // The rotation from the x-axis (horizontal movement that should rotate models on the y-axis)
    private float rotationX;

    // The rotation from the y-axis (vertical movement that should rotate models on the x-axis)
    private float rotationY;

    // The last position of the user input
    private Point2D lastPos;

    // The difference of the last position and the current position
    private Point2D difference;

    // Is the user currently dragging the model view
    private boolean dragging;

    // Array that stores the velocities from the x-axis movement
    private float[] velocitySamplesX;

    // Array that stores the velocities from the y-axis movement
    private float[] velocitySamplesY;

    // The average velocity of the samples
    private Point2D averageVelocity;

    // The velocity sample index
    private int velocitySampleIndex;

    /**
     * Create the touch rotation object starting from a certain rotation point
     *
     * @param startRotationX    The start rotation for the horizontal calculation
     * @param startRotationY    The start rotation for the vertical calculation
     * @since                   1.0
     */
    public TouchRotation(float startRotationX, float startRotationY)
    {
        rotationX = startRotationX;
        rotationY = startRotationY;
        lastPos = new Point2D();
        dragging = false;
        difference = new Point2D();
        velocitySamplesX = new float[MAX_SAMPLES];
        velocitySamplesY = new float[MAX_SAMPLES];
        averageVelocity = new Point2D();
        velocitySampleIndex = 0;
    }

    /**
     * Create the touch rotation object with no initial rotation
     *
     * @since   1.0
     */
    public TouchRotation()
    {
        this(0.0f, 0.0f);
    }

    /**
     * Get the rotation calculation from the x-axis movement (the rotation to apply to y-axis)
     *
     * @return  The x-axis movement rotation calculation (the rotation to apply to the y-axis)
     * @since   1.0
     */
    public float getRotationX()
    {
        return rotationX;
    }

    /**
     * Get the rotation calculation from the y-axis movement (the rotation to apply to x-axis)
     *
     * @return  The y-axis movement rotation calculation (the rotation to apply to the x-axis)
     * @since   1.0
     */
    public float getRotationY()
    {
        return rotationY;
    }

    /**
     * Update the touch rotation object. This will update the rotation depending on the remaining
     * velocity. The velocity will gradually be decreased depending on the drag co-efficient.
     *
     * @return  The y-axis movement rotation calculation (the rotation to apply to the x-axis)
     * @since   1.0
     */
    public void update(float deltaTime)
    {
        // If the user is dragging, update the velocity and rotation
        if(dragging == false)
        {
            // If the average velocity is not 0.0, apply change to the velocity and rotation
            if(averageVelocity.x != 0.0f && averageVelocity.y != 0.0f)
            {
                // Check if the absolute value of the x velocity is so small that is should be set
                // back to 0. This prevents the skateboard model from slowly rotating forever
                // because the velocity will only have a smaller magnitude by a multiplier and would
                // never hit zero.
                if(Math.abs(averageVelocity.x) <= 0.01f)
                {
                    averageVelocity.x = 0.0f;
                }

                // The same check as above but for the y velocity
                if(Math.abs(averageVelocity.y) <= 0.01f)
                {
                    averageVelocity.y = 0.0f;
                }

                // Calculate the new velocities based on the drag co-efficient
                averageVelocity.x -= averageVelocity.x * DRAG_COEFFICIENT * deltaTime;
                averageVelocity.y -= averageVelocity.y * DRAG_COEFFICIENT * deltaTime;

                // Change the rotation amount based on the velocity
                rotationX += averageVelocity.x;
                rotationY += averageVelocity.y;
            }
        }
    }

    /**
     * Touch input from the user. This will take the input from the user to determine if the user is
     * making a dragging motion. A velocity will be calculated based on the speed of the users drag.
     *
     * @param type      The type of motion event
     * @param position  The position of the motion event on the display
     * @since           1.0
     */
    public void touch(int type, Point2D position)
    {
        // Switch through the types of motion events
        switch (type)
        {
            case MotionEvent.ACTION_DOWN:
                // The user has just clicked down, reset all of the variables involved in the
                // calculation of the average velocity
                lastPos = position;
                dragging = true;

                // Set the samples back to 0
                for(int i = 0; i < MAX_SAMPLES; i++)
                {
                    velocitySamplesX[i] = 0.0f;
                    velocitySamplesY[i] = 0.0f;
                }
                velocitySampleIndex = 0;
            break;
            case MotionEvent.ACTION_UP:
                // The user has just stopped dragging, calculate the average velocity of the drag
                // using the samples that where captured from it
                dragging = false;

                // Calculate mean velocity
                averageVelocity.x = 0.0f;
                averageVelocity.y = 0.0f;

                // Iterate through the samples, summing them
                for(int i = 0; i < MAX_SAMPLES; i++)
                {
                    averageVelocity.x += velocitySamplesX[i];
                    averageVelocity.y += velocitySamplesY[i];
                }

                // The final calculation to find the average velocity is to divide the sum over the
                // number of samples
                averageVelocity.x /= MAX_SAMPLES;
                averageVelocity.y /= MAX_SAMPLES;
                break;
            case MotionEvent.ACTION_MOVE:
                // Check if the user has clicked and the dragging is enabled
                if(dragging)
                {
                    // If there is no last position, set the current position as the last position
                    if(lastPos == null)
                    {
                        lastPos = position;
                    }

                    // Calculate the velocity vector
                    difference.x = (position.x - lastPos.x) * ROTATION_SCALE;
                    difference.y = (position.y - lastPos.y) * ROTATION_SCALE;

                    // If the velocity sample index has hit the end of the array, loop back to the
                    // first index (like a circular buffer)
                    if(velocitySampleIndex >= MAX_SAMPLES)
                    {
                        velocitySampleIndex = 0;
                    }

                    // Set the current index of the velocity samples to the new calculated
                    // difference
                    velocitySamplesX[velocitySampleIndex] = difference.x;
                    velocitySamplesY[velocitySampleIndex] = difference.y;

                    // Iterate the velocity sample index forward by one
                    velocitySampleIndex++;

                    // Because we are currently dragging, increase the rotation by the velocity
                    rotationX += difference.x;
                    rotationY += difference.y;

                    // Set the new last to be this position (so that next cycle it will be the last
                    // position)
                    lastPos = position;
                }
                break;
        }
    }
}
