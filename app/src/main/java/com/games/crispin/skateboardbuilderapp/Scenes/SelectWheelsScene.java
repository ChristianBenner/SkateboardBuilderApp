package com.games.crispin.skateboardbuilderapp.Scenes;

import com.games.crispin.crispinmobile.Utilities.Scene;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Geometry.Scale2D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera3D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Font;
import com.games.crispin.crispinmobile.Rendering.Utilities.Material;
import com.games.crispin.crispinmobile.Rendering.Utilities.Model;
import com.games.crispin.crispinmobile.Rendering.Utilities.ModelMatrix;
import com.games.crispin.crispinmobile.UserInterface.Border;
import com.games.crispin.crispinmobile.UserInterface.Button;
import com.games.crispin.crispinmobile.UserInterface.Text;
import com.games.crispin.crispinmobile.UserInterface.TouchEvent;
import com.games.crispin.crispinmobile.Utilities.Logger;
import com.games.crispin.crispinmobile.Utilities.ThreadedOBJLoader;
import com.games.crispin.skateboardbuilderapp.ConfigReaders.SaveManager;
import com.games.crispin.skateboardbuilderapp.ConfigReaders.WheelConfigReader;
import com.games.crispin.skateboardbuilderapp.CustomButton;
import com.games.crispin.skateboardbuilderapp.FadeTransition;
import com.games.crispin.skateboardbuilderapp.LoadingIcon;
import com.games.crispin.skateboardbuilderapp.R;
import com.games.crispin.skateboardbuilderapp.SkateboardComponents.Skateboard;
import com.games.crispin.skateboardbuilderapp.SkateboardComponents.Wheel;
import com.games.crispin.skateboardbuilderapp.TouchRotation;

import java.util.ArrayList;
import java.util.List;

public class SelectWheelsScene extends Scene
{
    // Tag used for logging
    private static final String TAG = "SelectWheelsScene";

    // Padding for the back button
    private static final Point2D BACK_BUTTON_PADDING = new Point2D(50.0f, 50.0f);

    // Size of the back button
    private static final Scale2D BACK_BUTTON_SIZE = new Scale2D(150.0f, 150.0f);

    // Position of the back button
    private static final Point2D BACK_BUTTON_POSITION = new Point2D(BACK_BUTTON_PADDING.x,
            Crispin.getSurfaceHeight() - BACK_BUTTON_PADDING.y - BACK_BUTTON_SIZE.y);

    // Padding for the next button
    private static final Point2D NEXT_BUTTON_PADDING = new Point2D(0.0f, 50.0f);

    // Size of the next button
    private static final Scale2D NEXT_BUTTON_SIZE = new Scale2D(600.0f, 200.0f);

    // Next button position
    private static final Point2D NEXT_BUTTON_POSITION = new Point2D((Crispin.getSurfaceWidth() /
            2.0f) - (NEXT_BUTTON_SIZE.x / 2.0f) + NEXT_BUTTON_PADDING.x, NEXT_BUTTON_PADDING.y);

    // Camera for 2D/user interface rendering
    private Camera2D uiCamera;

    // Camera for 3D model rendering
    private Camera3D modelCamera;

    // Render object model for the deck
    private Model model;

    // Render object model matrix
    private ModelMatrix modelMatrix;

    // Touch rotation object calculates the rotation of the interactable model
    private TouchRotation touchRotation;

    // Transition object that allows us to fade the scene in and out
    private FadeTransition fadeTransition;

    // The loading icon UI
    private LoadingIcon loadingIcon;

    // Back button UI (return to home screen)
    private CustomButton backButton;

    // Next button UI
    private Button nextButton;

    // Select deck width text UI
    private Text titleText;

    // Left button for selecting the previous design in the list
    private Button leftButton;

    // Right button for selecting the next design in the list
    private Button rightButton;

    // The skateboard that is being worked on
    private Skateboard subject;

    private List<Material> materials;
    private List<Wheel> wheels;
    private int gripIndex;

    private Material materialNoDesign;
    private int wheelIndex;

    public SelectWheelsScene()
    {
        materialNoDesign = new Material(R.drawable.grey);
        wheelIndex = 0;

        // Try to load the skateboard that is currently being worked on
        subject = SaveManager.loadCurrentSave();

        // If there is no subject, create one and print an error
        if(subject == null)
        {
            subject = new Skateboard();
            Logger.error(TAG, "ERROR: Failed to load subject skateboard");
        }
        else
        {
            Logger.info("Grip scene started with deck ID[" + subject.getDeck() +
                    "] and design ID[" + subject.getDesign() + "]");
        }

        // Set the background to a blue colour
        Crispin.setBackgroundColour(HomeScene.BACKGROUND_COLOR);

        // Create the user interface camera
        uiCamera = new Camera2D(0, 0, Crispin.getSurfaceWidth(), Crispin.getSurfaceHeight());

        // Create the model camera and move it forward in-front of the origin
        modelCamera = new Camera3D();
        modelCamera.setPosition(new Point3D(0.0f, 0.0f, 7.0f));

        // Create the model matrix for transforming the model
        modelMatrix = new ModelMatrix();

        touchRotation = new TouchRotation();

        WheelConfigReader wheelConfigReader = WheelConfigReader.getInstance();
        wheels = wheelConfigReader.getWheels();

        materials = new ArrayList<>();
        // Load the wheel materials
        for(int i = 0; i < wheels.size(); i++)
        {
            materials.add(new Material(wheels.get(i).resourceId));
        }

        ThreadedOBJLoader.loadModel(R.raw.wheels_export_3, model ->
        {
            this.model = model;
            this.model.setMaterial(new Material(R.drawable.wheel_test3));
        });

        setupUI();
    }

    @Override
    public void update(float deltaTime)
    {
        touchRotation.update(deltaTime);

        modelMatrix.reset();
        modelMatrix.rotate(touchRotation.getRotationY(), 1.0f, 0.0f, 0.0f);
        modelMatrix.rotate(touchRotation.getRotationX(), 0.0f, 1.0f, 0.0f);
        modelMatrix.scale(0.2f);

        fadeTransition.update(deltaTime);
    }

    @Override
    public void render()
    {
        titleText.draw(uiCamera);
        nextButton.draw(uiCamera);
        backButton.draw(uiCamera);

        if(model != null)
        {
            model.render(modelCamera, modelMatrix);
        }

        leftButton.draw(uiCamera);
        rightButton.draw(uiCamera);
        loadingIcon.draw(uiCamera);
        fadeTransition.draw(uiCamera);
    }

    @Override
    public void touch(int type, Point2D position)
    {
        touchRotation.touch(type, position);
    }

    private Material nextWheel()
    {
        if(wheels.isEmpty())
        {
            Logger.error(TAG, "There are no wheels");
            return materialNoDesign;
        }

        wheelIndex++;
        if(wheelIndex >= materials.size())
        {
            wheelIndex = 0;
        }

        return materials.get(wheelIndex);
    }

    private Material previousWheel()
    {
        if(materials.isEmpty())
        {
            Logger.error(TAG, "There are no wheels");
            return materialNoDesign;
        }

        wheelIndex--;
        if(wheelIndex < 0)
        {
            wheelIndex = materials.size() - 1;
        }

        return materials.get(wheelIndex);
    }

    private void setupUI()
    {
        // Create the fade transition object and set it to fade in
        fadeTransition = new FadeTransition();
        fadeTransition.setFadeColour(HomeScene.BACKGROUND_COLOR);
        fadeTransition.fadeIn();

        // Create the loading icon
        loadingIcon = new LoadingIcon();

        // The font for text on the scene
        Font aileronRegularFont = new Font(R.raw.aileron_regular, 76);

        // Create the back button
        backButton = new CustomButton(R.drawable.back_icon);
        nextButton = new Button(aileronRegularFont, "Next");

        titleText = new Text(aileronRegularFont, "Select Wheels", false,
                true, Crispin.getSurfaceWidth());

        leftButton = new CustomButton(R.drawable.arrow_left);
        rightButton = new CustomButton(R.drawable.arrow_right);

        backButton.setPosition(BACK_BUTTON_POSITION);
        backButton.setSize(BACK_BUTTON_SIZE);

        nextButton.setSize(NEXT_BUTTON_SIZE);
        nextButton.setPosition(NEXT_BUTTON_POSITION);
        nextButton.setColour(HomeScene.BACKGROUND_COLOR);
        nextButton.setBorder(new Border(Colour.WHITE, 8));
        nextButton.setTextColour(Colour.WHITE);

        leftButton.setSize(240.0f, 240.0f);
        leftButton.setPosition(20.0f, (Crispin.getSurfaceHeight() / 2.0f) -
                (leftButton.getSize().y / 2.0f));
        leftButton.addTouchListener(e ->
        {
            if(e.getEvent() == TouchEvent.Event.RELEASE)
            {
                //subject.setGrip(grips.get(gripIndex).id);
                model.setMaterial(previousWheel());
            }
        });


        rightButton.setSize(240.0f, 240.0f);
        rightButton.setPosition(Crispin.getSurfaceWidth() - 20.0f -rightButton.getSize().x,
                (Crispin.getSurfaceHeight() / 2.0f) - (rightButton.getSize().y / 2.0f));
        rightButton.addTouchListener(e ->
        {
            if(e.getEvent() == TouchEvent.Event.RELEASE)
            {
               // subject.setDesign(grips.get(gripIndex).id);
                model.setMaterial(nextWheel());
            }
        });

        final Point2D TITLE_TEXT_POSITION = new Point2D(0.0f, BACK_BUTTON_POSITION.y -
                BACK_BUTTON_PADDING.y - titleText.getHeight());

        titleText.setColour(Colour.WHITE);
        titleText.setPosition(TITLE_TEXT_POSITION);

        // Add touch listener to back button
        backButton.addTouchListener(e ->
        {
            switch (e.getEvent())
            {
                case RELEASE:
                    fadeTransition.fadeOutToScence(SelectDeckDesignScene::new);
                    break;
            }
        });

        // Add touch listener to next button
        nextButton.addTouchListener(e ->
        {
            switch (e.getEvent())
            {
                case RELEASE:
                    // Save the current skateboard
                    SaveManager.writeCurrentSave(subject);

                    fadeTransition.fadeOutToScence(HomeScene::new);
                    break;
            }
        });
    }
}

