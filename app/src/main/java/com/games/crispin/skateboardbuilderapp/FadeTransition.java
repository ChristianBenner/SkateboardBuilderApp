package com.games.crispin.skateboardbuilderapp;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Geometry.Scale2D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.games.crispin.crispinmobile.UserInterface.Plane;
import com.games.crispin.crispinmobile.Utilities.Scene;

public class FadeTransition
{
    private static final int DEFAULT_DURATION_MS = 600;
    private static final int ONE_SECOND_MS = 1000;

    private boolean completed;
    private boolean fadeIn;
    private boolean transitioning;
    private Plane transitionOverlay;
    private float alpha;
    private Scene.Constructor sceneConstructor;
    private boolean fadeOutToScene;
    private int duration;
    private float difPerFrame;

    public FadeTransition()
    {
        completed = false;
        transitionOverlay = new Plane(new Scale2D(Crispin.getSurfaceWidth(),
                Crispin.getSurfaceHeight()));
        transitioning = false;
        sceneConstructor = null;
        fadeOutToScene = false;
        setDuration(DEFAULT_DURATION_MS);
    }

    public void setDuration(int milliseconds)
    {
        this.duration = milliseconds;
        float msPerFrame = (float)ONE_SECOND_MS / Crispin.getTargetRefreshRate();
        difPerFrame = msPerFrame / (float)duration;
    }

    public int getDuration()
    {
        return duration;
    }

    public void setFadeColour(Colour colour)
    {
        transitionOverlay.setColour(colour);
    }

    public Colour getFadeColour(Colour colour)
    {
        return transitionOverlay.getColour();
    }

    public void fadeIn()
    {
        alpha = 1.0f;
        completed = false;
        transitioning = true;
        fadeIn = true;
    }

    public void fadeOut()
    {
        alpha = 0.0f;
        completed = false;
        transitioning = true;
        fadeOutToScene = false;
        fadeIn = false;
    }

    public void fadeOutToScence(Scene.Constructor sceneConstructor)
    {
        this.sceneConstructor = sceneConstructor;
        fadeOutToScene = true;
        alpha = 0.0f;
        completed = false;
        transitioning = true;
        fadeIn = false;
    }

    public boolean isTransitionCompleted()
    {
        return completed;
    }

    public void update(float deltaTime)
    {
        if(transitioning)
        {
            if(fadeIn)
            {
                alpha -= difPerFrame * deltaTime;

                if(alpha <= 0.0f)
                {
                    alpha = 0.0f;
                    transitioning = false;
                    completed = true;
                }
            }
            else
            {
                alpha += difPerFrame * deltaTime;

                if(alpha >= 1.0f)
                {
                    alpha = 1.0f;
                    transitioning = false;
                    completed = true;

                    if(fadeOutToScene && sceneConstructor != null)
                    {
                        Crispin.setScene(sceneConstructor);
                    }
                }
            }
        }
    }

    public void draw(Camera2D camera2D)
    {
        transitionOverlay.setAlpha(alpha);
        System.out.println("ooo alpha: " +alpha);
        transitionOverlay.draw(camera2D);
    }
}
