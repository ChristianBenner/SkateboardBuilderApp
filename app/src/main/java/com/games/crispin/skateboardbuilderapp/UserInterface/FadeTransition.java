package com.games.crispin.skateboardbuilderapp.UserInterface;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Geometry.Scale2D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.games.crispin.crispinmobile.UserInterface.Plane;
import com.games.crispin.crispinmobile.Utilities.Scene;

/**
 * The FadeTransition class is designed to apply a fade in or out from a colour over time. The
 * object allows you to switch scenes and can be given a custom colour and duration. You must draw
 * the object over the other drawn objects that you wish to hide.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
public class FadeTransition
{
    // The default fade duration in milliseconds
    private static final int DEFAULT_DURATION_MS = 600;

    // The duration of one second in milliseconds
    private static final int ONE_SECOND_MS = 1000;

    // The minimum value for the alpha channel intensity
    private static final float MINIMUM_ALPHA_CHANNEL_INTENSITY = 0.0f;

    // The maximum value for the alpha channel intesnity
    private static final float MAXIMUM_ALPHA_CHANNEL_INTENSITY = 1.0f;

    // If the transition has been completed or not
    private boolean completed;

    // If the object is currently transitioning
    private boolean transitioning;

    // If the object is fading out to another scene
    private boolean fadeOutToScene;

    // If the object is fading out
    private boolean fadeIn;

    // The alpha value
    private float alpha;

    // The duration of the transition
    private int duration;

    // The value to change the alpha channel intensity per update
    private float difPerFrame;

    // The constructor of the scene to fade out to
    private Scene.Constructor sceneConstructor;

    // The transition overlay user interface object
    private Plane transitionOverlay;

    /**
     * Construct the fade transition object
     *
     * @since 1.0
     */
    public FadeTransition()
    {
        completed = false;
        transitioning = false;
        sceneConstructor = null;
        fadeOutToScene = false;
        fadeIn = false;
        alpha = 0.0f;
        setDuration(DEFAULT_DURATION_MS);
        transitionOverlay = new Plane(new Scale2D(Crispin.getSurfaceWidth(),
                Crispin.getSurfaceHeight()));
    }

    /**
     * Set the duration of the transition
     *
     * @param milliseconds  The new duration for the transition
     * @since 1.0
     */
    public void setDuration(int milliseconds)
    {
        this.duration = milliseconds;

        // Calculate how many milliseconds in every frame
        float msPerFrame = (float)ONE_SECOND_MS / Crispin.getTargetRefreshRate();

        // Calculate the difference to adjust the alpha channel by per update cycle
        difPerFrame = msPerFrame / (float)duration;
    }

    /**
     * Get the duration of the transition
     *
     * @return The duration of the transition
     * @since 1.0
     */
    public int getDuration()
    {
        return duration;
    }

    /**
     * Set the colour of the fade
     *
     * @param colour    The fade colour
     * @since 1.0
     */
    public void setFadeColour(Colour colour)
    {
        transitionOverlay.setColour(colour);
    }

    /**
     * Get the colour of the fade
     *
     * @return  The colour of the fade
     * @since   1.0
     */
    public Colour getFadeColour()
    {
        return transitionOverlay.getColour();
    }

    /**
     * Start the fade in of the object
     *
     * @since 1.0
     */
    public void fadeIn()
    {
        alpha = 1.0f;
        completed = false;
        transitioning = true;
        fadeIn = true;
        sceneConstructor = null;
    }

    /**
     * Start the fade out of the object
     *
     * @since 1.0
     */
    public void fadeOut()
    {
        alpha = 0.0f;
        completed = false;
        transitioning = true;
        fadeOutToScene = false;
        fadeIn = false;
        sceneConstructor = null;
    }

    /**
     * Fade out to a scene specified by its scene constructor
     *
     * @param sceneConstructor  The scene to fade out to
     * @see     Scene
     * @since   1.0
     */
    public void fadeOutToScence(Scene.Constructor sceneConstructor)
    {
        this.sceneConstructor = sceneConstructor;
        fadeOutToScene = true;
        alpha = 0.0f;
        completed = false;
        transitioning = true;
        fadeIn = false;
    }

    /**
     * Check if the transition is completed
     *
     * @return  Whether or not the transition has completed
     * @since   1.0
     */
    public boolean isTransitionCompleted()
    {
        return completed;
    }

    /**
     * Update the fade transition object. This will update the alpha channel of the object.
     *
     * @param deltaTime A multiplier for timing. Used to calculate a value correctly over time
     * @since           1.0
     */
    public void update(float deltaTime)
    {
        // If the object is in its transition state
        if(transitioning)
        {
            // If the transition is to fade in, decrease the alpha colour channel intensity.
            // Otherwise increase it.
            if(fadeIn)
            {
                // Decrease the alpha channel intensity
                alpha -= difPerFrame * deltaTime;

                // If the alpha channel is below the minimum value, set it to the minimum value and
                // flag the transition as complete
                if(alpha <= MINIMUM_ALPHA_CHANNEL_INTENSITY)
                {
                    alpha = MINIMUM_ALPHA_CHANNEL_INTENSITY;
                    transitioning = false;
                    completed = true;
                }
            }
            else
            {
                // Increase the alpha intensity
                alpha += difPerFrame * deltaTime;

                // If the alpha channel is above the maximum value, set it to the maximum value and
                // flag the transition as complete
                if(alpha >= MAXIMUM_ALPHA_CHANNEL_INTENSITY)
                {
                    alpha = MAXIMUM_ALPHA_CHANNEL_INTENSITY;
                    transitioning = false;
                    completed = true;

                    // If there has been a specified scene constructor, use it to set the scene
                    if(fadeOutToScene && sceneConstructor != null)
                    {
                        Crispin.setScene(sceneConstructor);
                    }
                }
            }

            // Set the transition overlay objects alpha channel intensity
            transitionOverlay.setAlpha(alpha);
        }
    }

    /**
     * Draw the fade transition object
     *
     * @param camera2D  The 2D camera object used to draw the object with
     * @since           1.0
     */
    public void draw(Camera2D camera2D)
    {
        // Draw the transition overlay object
        transitionOverlay.draw(camera2D);
    }
}
