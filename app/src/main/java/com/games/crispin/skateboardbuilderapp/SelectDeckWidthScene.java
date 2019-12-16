package com.games.crispin.skateboardbuilderapp;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Geometry.RotationMatrix;
import com.games.crispin.crispinmobile.Geometry.Vector3D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera3D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Material;
import com.games.crispin.crispinmobile.Rendering.Utilities.RenderObject;
import com.games.crispin.crispinmobile.Rendering.Utilities.Texture;
import com.games.crispin.crispinmobile.UserInterface.Dropdown;
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


    public SelectDeckWidthScene()
    {
        // Set the background to a blue colour
        Crispin.setBackgroundColour(HomeScene.BACKGROUND_COLOR);

        fadeTransition = new FadeTransition();
        fadeTransition.setFadeColour(HomeScene.BACKGROUND_COLOR);
        fadeTransition.fadeIn();

        uiCamera = new Camera2D(0, 0, Crispin.getSurfaceWidth(), Crispin.getSurfaceHeight());

        rotationMatrix = new RotationMatrix();

/*        widthSelectDropdown = new Dropdown("Select a deck width");
        widthSelectDropdown.setPosition(Crispin.getSurfaceWidth() * 0.05f, Crispin.getSurfaceHeight() * 0.7f);
        widthSelectDropdown.setSize(Crispin.getSurfaceWidth() * 0.9f, 100.0f);
        widthSelectDropdown.setDisabledBorders(Dropdown.INNER_BORDERS);
        widthSelectDropdown.setColour(HomeScene.BACKGROUND_COLOR);
        widthSelectDropdown.setTextColour(Colour.WHITE);
        widthSelectDropdown.setBorderColour(Colour.WHITE);
        widthSelectDropdown.setStateIcons(R.drawable.expand_icon, R.drawable.collapse_icon);

        final int id8125 = widthSelectDropdown.addItem("8.125");
        final int id825 = widthSelectDropdown.addItem("8.25");
        final int id8375 = widthSelectDropdown.addItem("8.375");
        final int id85 = widthSelectDropdown.addItem("8.5");
        final int id86 = widthSelectDropdown.addItem("8.6");*/

        camera3D = new Camera3D();
        camera3D.setPosition(new Point3D(0.0f, 0.0f, 7.0f));

        widthSelectDropdown = new Dropdown("Select an object to view");
        widthSelectDropdown.setPosition(Crispin.getSurfaceWidth() * 0.05f, Crispin.getSurfaceHeight() * 0.7f);
        widthSelectDropdown.setSize(Crispin.getSurfaceWidth() * 0.9f, 100.0f);
        widthSelectDropdown.setDisabledBorders(Dropdown.INNER_BORDERS);
        widthSelectDropdown.setColour(HomeScene.BACKGROUND_COLOR);
        widthSelectDropdown.setTextColour(Colour.WHITE);
        widthSelectDropdown.setBorderColour(Colour.WHITE);
        widthSelectDropdown.setStateIcons(R.drawable.expand_icon, R.drawable.collapse_icon);

        final int trucks = widthSelectDropdown.addItem("Trucks");
        final int deckOne = widthSelectDropdown.addItem("Deck One");
        final int deckTwo = widthSelectDropdown.addItem("Deck Two");

        Material grey = new Material(new Texture(R.drawable.grey));
        Material palace = new Material(new Texture(R.drawable.palacedeck));
        Material jart = new Material(new Texture(R.drawable.jart_new_wave));

        widthSelectDropdown.addTouchListener(new TouchListener() {
            @Override
            public void touchEvent(TouchEvent e) {
                switch (e.getEvent())
                {
                    case CLICK:

                        break;
                    case RELEASE:
                        int selectedId = widthSelectDropdown.getSelectedId();

                        if(selectedId == trucks)
                        {
                            ThreadedOBJLoader.loadModel(R.raw.trucktest, renderObject ->
                            {
                                model = renderObject;
                                model.setScale(0.6f, 0.6f, 0.6f);
                                model.setMaterial(grey);
                                model.setRotation(rotationMatrix);
                            });
                        }
                        else if(selectedId == deckOne)
                        {
                            ThreadedOBJLoader.loadModel(R.raw.deck8_125_uv_test, renderObject ->
                            {
                                model = renderObject;
                                model.setMaterial(palace);
                                model.setScale(0.2f, 0.2f, 0.2f);
                                model.setRotation(rotationMatrix);
                            });
                        }
                        else if(selectedId == deckTwo)
                        {
                            ThreadedOBJLoader.loadModel(R.raw.deck8_125_uv_test, renderObject ->
                            {
                                model = renderObject;
                                model.setMaterial(jart);
                                model.setScale(0.2f, 0.2f, 0.2f);
                                model.setRotation(rotationMatrix);
                            });
                        }
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

        if(model != null)
        {
            model.setRotation(rotationMatrix);
        }
    }

    @Override
    public void render()
    {
        if(model != null)
        {
            model.newRender(camera3D);
        }

        backButton.draw(uiCamera);
        widthSelectDropdown.draw(uiCamera);
        fadeTransition.draw(uiCamera);
    }

    @Override
    public void touch(int type, Point2D position)
    {

    }
}
