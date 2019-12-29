package com.games.crispin.skateboardbuilderapp.Scenes;

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
import com.games.crispin.crispinmobile.Utilities.Scene;
import com.games.crispin.crispinmobile.Utilities.ThreadedOBJLoader;
import com.games.crispin.skateboardbuilderapp.ConfigReaders.DeckConfigReader;
import com.games.crispin.skateboardbuilderapp.ConfigReaders.DesignConfigReader;
import com.games.crispin.skateboardbuilderapp.ConfigReaders.GripConfigReader;
import com.games.crispin.skateboardbuilderapp.ConfigReaders.SaveManager;
import com.games.crispin.skateboardbuilderapp.CustomButton;
import com.games.crispin.skateboardbuilderapp.FadeTransition;
import com.games.crispin.skateboardbuilderapp.LoadingIcon;
import com.games.crispin.skateboardbuilderapp.R;
import com.games.crispin.skateboardbuilderapp.SkateboardComponents.Design;
import com.games.crispin.skateboardbuilderapp.SkateboardComponents.Grip;
import com.games.crispin.skateboardbuilderapp.SkateboardComponents.Skateboard;
import com.games.crispin.skateboardbuilderapp.TouchRotation;

import java.util.ArrayList;
import java.util.List;

public class SelectGripScene extends Scene
{
    // Tag used for logging
    private static final String TAG = "SelectGripScene";

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
    private Model deck;

    // Render object model for the grip
    private Model grip;

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

    private Material materialNoDesign;
    private List<Material> materials;

    private List<Grip> grips;

    private int gripIndex;

    public SelectGripScene()
    {
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

        DeckConfigReader deckConfigReader = DeckConfigReader.getInstance();
        DesignConfigReader designConfigReader = DesignConfigReader.getInstance();
        Design design = designConfigReader.getDesign(subject.getDesign());

        ThreadedOBJLoader.loadModel(deckConfigReader.getDeck(subject.getDeck()).modelId, model ->
        {
            this.deck = model;
            this.deck.setMaterial(new Material(design.resourceId));
        });


        GripConfigReader gripConfigReader = GripConfigReader.getInstance();
        grips = gripConfigReader.getGrips();
        materials = new ArrayList<>();
        materialNoDesign = new Material(R.drawable.grey);

        gripIndex = 0;

        // Load materials from the grips
        for(Grip grip : grips)
        {
            materials.add(new Material(grip.resourceId));
        }

        ThreadedOBJLoader.loadModel(deckConfigReader.getDeck(subject.getDeck()).gripModelId, model ->
        {
            this.grip = model;
            this.grip.setMaterial(nextGrip());
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

        if(deck != null && grip != null)
        {
            deck.render(modelCamera, modelMatrix);
            grip.render(modelCamera, modelMatrix);
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

    private Material nextGrip()
    {
        if(grips.isEmpty())
        {
            Logger.error(TAG, "There are no grips available");
            return materialNoDesign;
        }

        gripIndex++;
        if(gripIndex >= grips.size())
        {
            gripIndex = 0;
        }

        return materials.get(gripIndex);
    }

    private Material previousGrip()
    {
        if(materials.isEmpty())
        {
            Logger.error(TAG, "There are no grips available");
            return materialNoDesign;
        }

        gripIndex--;
        if(gripIndex < 0)
        {
            gripIndex = materials.size() - 1;
        }

        return materials.get(gripIndex);
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

        titleText = new Text(aileronRegularFont, "Select Griptape", false,
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
                subject.setGrip(grips.get(gripIndex).id);
                grip.setMaterial(previousGrip());
            }
        });


        rightButton.setSize(240.0f, 240.0f);
        rightButton.setPosition(Crispin.getSurfaceWidth() - 20.0f -rightButton.getSize().x,
                (Crispin.getSurfaceHeight() / 2.0f) - (rightButton.getSize().y / 2.0f));
        rightButton.addTouchListener(e ->
        {
            if(e.getEvent() == TouchEvent.Event.RELEASE)
            {
                subject.setDesign(grips.get(gripIndex).id);
                grip.setMaterial(nextGrip());
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

                    fadeTransition.fadeOutToScence(SelectWheelsScene::new);
                    break;
            }
        });
    }
}