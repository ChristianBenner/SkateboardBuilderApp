package com.games.crispin.skateboardbuilderapp;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Rendering.Models.Cube;
import com.games.crispin.crispinmobile.Rendering.Utilities.Font;
import com.games.crispin.crispinmobile.Rendering.Utilities.RotationMatrix;
import com.games.crispin.crispinmobile.Geometry.Scale2D;
import com.games.crispin.crispinmobile.Geometry.Vector3D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera3D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Material;
import com.games.crispin.crispinmobile.Rendering.Utilities.RenderObject;
import com.games.crispin.crispinmobile.Rendering.Utilities.Texture;
import com.games.crispin.crispinmobile.UserInterface.Border;
import com.games.crispin.crispinmobile.UserInterface.Button;
import com.games.crispin.crispinmobile.UserInterface.Dropdown;
import com.games.crispin.crispinmobile.UserInterface.Text;
import com.games.crispin.crispinmobile.UserInterface.TouchEvent;
import com.games.crispin.crispinmobile.UserInterface.TouchListener;
import com.games.crispin.crispinmobile.Utilities.ThreadedOBJLoader;
import com.games.crispin.crispinmobile.Utilities.Scene;

public class SelectDeckWidthScene extends Scene
{
    // Camera for 2D/user interface rendering
    private Camera2D uiCamera;
    private Camera3D camera3D;

    private Dropdown widthSelectDropdown;

    private FadeTransition fadeTransition;

    private CustomButton backButton;

    private RenderObject model;
    private RotationMatrix rotationMatrix;

    private LoadingIcon loadingIcon;
    private boolean showLoadingIcon;

    private Text selectDeckWidthText;
    private Button nextButton;

    public SelectDeckWidthScene()
    {
        // Set the background to a blue colour
        Crispin.setBackgroundColour(HomeScene.BACKGROUND_COLOR);

        fadeTransition = new FadeTransition();
        fadeTransition.setFadeColour(HomeScene.BACKGROUND_COLOR);
        fadeTransition.fadeIn();

        uiCamera = new Camera2D(0, 0, Crispin.getSurfaceWidth(), Crispin.getSurfaceHeight());

        loadingIcon = new LoadingIcon();

        Scale2D loadIconSize = new Scale2D(240.0f, 240.0f);
        loadingIcon.setPosition(new Point2D((Crispin.getSurfaceWidth() / 2.0f) - (loadIconSize.x / 2.0f),
                (Crispin.getSurfaceHeight() / 2.0f) - (loadIconSize.y / 2.0f)));
        loadingIcon.setSize(loadIconSize);

        rotationMatrix = new RotationMatrix();
        showLoadingIcon = false;

        camera3D = new Camera3D();
        camera3D.setPosition(new Point3D(0.0f, 0.0f, 7.0f));

        Font font = new Font(com.games.crispin.crispinmobile.R.raw.aileron_regular, 76);
        selectDeckWidthText = new Text(font, "Select Deck Width", false,
                true, Crispin.getSurfaceWidth());
        selectDeckWidthText.setColour(Colour.WHITE);
        selectDeckWidthText.setPosition(0.0f, (Crispin.getSurfaceHeight() * 0.7f) + 150.0f);

        nextButton = new Button(font, "Next");
        nextButton.setSize(600.0f, 200.0f);
        nextButton.setPosition((Crispin.getSurfaceWidth() / 2.0f) - 300.0f, 50.0f);
        nextButton.setColour(HomeScene.BACKGROUND_COLOR);
        nextButton.setBorder(new Border(Colour.WHITE, 8));
        nextButton.setTextColour(Colour.WHITE);
        nextButton.setEnabled(false);
        nextButton.addTouchListener(e ->
        {
            switch (e.getEvent())
            {
                case RELEASE:
                    fadeTransition.fadeOutToScence(HomeScene::new);
                    break;
            }
        });

        widthSelectDropdown = new Dropdown("Select Width");
        widthSelectDropdown.setPosition(Crispin.getSurfaceWidth() * 0.05f,
                Crispin.getSurfaceHeight() * 0.7f);
        widthSelectDropdown.setSize(Crispin.getSurfaceWidth() * 0.9f, 100.0f);
        widthSelectDropdown.setDisabledBorders(Dropdown.INNER_BORDERS);
        widthSelectDropdown.setColour(HomeScene.BACKGROUND_COLOR);
        widthSelectDropdown.setTextColour(Colour.WHITE);
        widthSelectDropdown.setBorderColour(Colour.WHITE);
        widthSelectDropdown.setStateIcons(R.drawable.expand_icon, R.drawable.collapse_icon);

        final int width8 = widthSelectDropdown.addItem("8.0\"");
        final int width81 = widthSelectDropdown.addItem("8.1\"");
        final int width825 = widthSelectDropdown.addItem("8.25\"");
        final int width8375 = widthSelectDropdown.addItem("8.375\"");
        final int width85 = widthSelectDropdown.addItem("8.5\"");

        Material dark = new Material();
        dark.setColour(Colour.DARK_GREY);

        widthSelectDropdown.addTouchListener(new TouchListener() {
            @Override
            public void touchEvent(TouchEvent e) {
                switch (e.getEvent())
                {
                    case CLICK:

                        break;
                    case RELEASE:
                        int selectedId = widthSelectDropdown.getSelectedId();

                        if(selectedId == width8)
                        {
                            model = new Cube();
                            model.setColour(Colour.GREEN);
                        }

                        if(selectedId == width81)
                        {
                            model = new Cube();
                            model.setColour(Colour.RED);
                        }

                        if(selectedId == width825)
                        {
                            model = new Cube();
                            model.setColour(Colour.BLUE);
                        }

                        if(selectedId == width8375)
                        {
                            model = new Cube();
                            model.setColour(Colour.MAGENTA);
                        }

                        if(selectedId == width85)
                        {
                            model = new Cube();
                            model.setColour(Colour.YELLOW);
                        }

                        if(model != null)
                        {
                            nextButton.setEnabled(true);
                        }

                        model.setRotation(rotationMatrix);

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

    float yAngle = 45.0f;
    @Override
    public void update(float deltaTime)
    {
        backButton.update(deltaTime);
        fadeTransition.update(deltaTime);

        yAngle += 0.5f;
        rotationMatrix.reset();
        rotationMatrix.applyRotation(new Vector3D(0.0f, 1.0f, 0.0f), yAngle + 45.0f);
        rotationMatrix.applyRotation(new Vector3D(1.0f, 0.0f, 0.0f), 45.0f + 270.0f);

        if(showLoadingIcon)
        {
            loadingIcon.update(deltaTime);
        }

        if(model != null)
        {
            model.setRotation(rotationMatrix);
        }
    }

    @Override
    public void render()
    {
        if(model != null && !showLoadingIcon)
        {
            model.newRender(camera3D);
        }

        backButton.draw(uiCamera);
        widthSelectDropdown.draw(uiCamera);
        selectDeckWidthText.draw(uiCamera);
        nextButton.draw(uiCamera);

        if(showLoadingIcon)
        {
            loadingIcon.draw(uiCamera);
        }

        fadeTransition.draw(uiCamera);
    }

    @Override
    public void touch(int type, Point2D position)
    {

    }
}
