package com.games.crispin.skateboardbuilderapp;

import android.transition.Fade;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.games.crispin.crispinmobile.UserInterface.Button;
import com.games.crispin.crispinmobile.UserInterface.Dropdown;
import com.games.crispin.crispinmobile.UserInterface.Plane;
import com.games.crispin.crispinmobile.UserInterface.TouchEvent;
import com.games.crispin.crispinmobile.UserInterface.TouchListener;
import com.games.crispin.crispinmobile.Utilities.Scene;

public class SelectDeckWidthScene extends Scene
{
    // Camera for 2D/user interface rendering
    private Camera2D uiCamera;
    private Dropdown widthSelectDropdown;

    private FadeTransition fadeTransition;

    private CustomButton backButton;

    public SelectDeckWidthScene()
    {
        // Set the background to a blue colour
        Crispin.setBackgroundColour(HomeScene.BACKGROUND_COLOR);

        fadeTransition = new FadeTransition();
        fadeTransition.setFadeColour(HomeScene.BACKGROUND_COLOR);
        fadeTransition.fadeIn();

        uiCamera = new Camera2D(0, 0, Crispin.getSurfaceWidth(), Crispin.getSurfaceHeight());

        widthSelectDropdown = new Dropdown();
        widthSelectDropdown.addItem("8.125");
        widthSelectDropdown.addItem("8.25");
        widthSelectDropdown.addItem("8.375");
        widthSelectDropdown.addItem("8.5");
        widthSelectDropdown.addItem("8.6");

        widthSelectDropdown.addTouchListener(new TouchListener() {
            @Override
            public void touchEvent(TouchEvent e) {
                switch (e.getEvent())
                {
                    case CLICK:
                        if(widthSelectDropdown.isExpanded())
                        {
                            widthSelectDropdown.collapse();
                        }
                        else
                        {
                            widthSelectDropdown.expand();
                        }
                        break;
                    case RELEASE:

                        break;
                }
            }
        });

        backButton = new CustomButton(R.drawable.back_icon);
        backButton.setPosition(100.0f, Crispin.getSurfaceHeight() - 100.0f - 200.0f);
        backButton.addTouchListener(new TouchListener() {
            @Override
            public void touchEvent(TouchEvent e) {
                switch (e.getEvent())
                {
                    case CLICK:

                        break;
                    case RELEASE:
                        fadeTransition.fadeOutToScence(HomeScene::new);
                        break;
                }
            }
        });
    }

    @Override
    public void update(float deltaTime)
    {
        backButton.update(deltaTime);
        fadeTransition.update(deltaTime);
    }

    @Override
    public void render()
    {
        backButton.draw(uiCamera);
        widthSelectDropdown.draw(uiCamera);
        fadeTransition.draw(uiCamera);
    }

    @Override
    public void touch(int type, Point2D position)
    {

    }
}
