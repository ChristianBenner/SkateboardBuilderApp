package com.games.crispin.skateboardbuilderapp.Scenes;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera3D;
import com.games.crispin.crispinmobile.UserInterface.Font;
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
import com.games.crispin.skateboardbuilderapp.Constants;
import com.games.crispin.skateboardbuilderapp.CustomButton;
import com.games.crispin.skateboardbuilderapp.FadeTransition;
import com.games.crispin.skateboardbuilderapp.InfoPanel;
import com.games.crispin.skateboardbuilderapp.LoadingIcon;
import com.games.crispin.skateboardbuilderapp.R;
import com.games.crispin.skateboardbuilderapp.SkateboardComponents.Deck;
import com.games.crispin.skateboardbuilderapp.SkateboardComponents.Design;
import com.games.crispin.skateboardbuilderapp.SkateboardComponents.Grip;
import com.games.crispin.skateboardbuilderapp.SkateboardComponents.Skateboard;
import com.games.crispin.skateboardbuilderapp.TouchRotation;

import java.util.HashMap;
import java.util.List;

/**
 * The SelectGriptapeScene class manages and displays a page that allows the user to select a
 * griptape for there skateboard. Previously the user selected a design, in this scene you can see
 * that design on the also previously selected deck model. To access the scene you must select a
 * design for the skateboard on the SelectDeckDesign scene and the press the 'Next' button. The
 * class extends the CrispinEngine class 'Scene' which allows the engine to handle it.
 *
 * @see         Scene
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
public class SelectGriptapeScene extends Scene
{
    // Tag used for logging
    private static final String TAG = "SelectGriptapeScene";

    // Scale for the model
    private static final float DEFAULT_MODEL_SCALE = 0.2f;

    // Default X axis rotation for the touch rotation
    private static final float DEFAULT_ROTATION_X = 0.0f;

    // Default Y axis rotation for the touch rotation
    private static final float DEFAULT_ROTATION_Y = 90.0f;

    // Camera for 2D/user interface rendering
    private Camera2D uiCamera;

    // Camera for 3D model rendering
    private Camera3D modelCamera;

    // The skateboard that is being worked on
    private Skateboard subject;

    // The current griptape
    private Grip currentGrip;

    // List of the grips
    private List<Grip> grips;

    // Index for the list of grips
    private int gripArrayIndex;

    // Materials stored with their respective grip IDs
    private HashMap<Integer, Material> gripMaterials;

    // Material for no texture
    private Material noTexture;

    // Render object model for the deck
    private Model deckModel;

    // Render object model for the griptape
    private Model gripModel;

    // Render object model matrix
    private ModelMatrix modelMatrix;

    // Touch rotation object calculates the rotation of the interactable model
    private TouchRotation touchRotation;

    // Transition object that allows us to fade the scene in and out
    private FadeTransition fadeTransition;

    // Information panel
    private InfoPanel infoPanel;

    // The loading icon UI
    private LoadingIcon loadingIcon;

    // Back button UI (return to home screen)
    private CustomButton backButton;

    // Information button
    private CustomButton infoButton;

    // Next button UI
    private Button nextButton;

    // Left button for selecting the previous griptape in the list
    private CustomButton leftButton;

    // Right button for selecting the next griptape in the list
    private CustomButton rightButton;

    // Select deck width text UI
    private Text titleText;

    // Selected object text
    private Text selectedObjectText;

    /**
     * Constructor to create the select griptape scene. Creates all of the UI, loads the model
     * and loads all of the griptape materials.
     *
     * @since   1.0
     */
    public SelectGriptapeScene()
    {
        // Set the background to a blue colour
        Crispin.setBackgroundColour(Constants.BACKGROUND_COLOR);

        // Create the user interface camera
        uiCamera = new Camera2D(0, 0, Crispin.getSurfaceWidth(), Crispin.getSurfaceHeight());

        // Create the model camera and move it forward in-front of the origin
        modelCamera = new Camera3D();
        modelCamera.setPosition(new Point3D(0.0f, 0.0f, 7.0f));

        // Try to load the skateboard that is currently being worked on
        subject = SaveManager.loadCurrentSave();

        // Create grip object with default values
        currentGrip = new Grip();

        // Get the grips from the grip config
        grips = GripConfigReader.getInstance().getGrips();
        gripArrayIndex = 0;

        // Create the materials map
        gripMaterials = new HashMap<>();

        // Load all the materials
        for(Grip temp : grips)
        {
            gripMaterials.put(temp.id, new Material(temp.resourceId));
        }

        // Material for when no texture has been found
        noTexture = new Material(R.drawable.no_material);

        // Initialise all of the user interface on the page
        initUI();

        // Load the deck and grip
        loadModels(subject);

        // Create the model matrix for transforming the model
        modelMatrix = new ModelMatrix();

        // Create touch rotation object that calculated the rotation for the 3D view when the user
        // interacts with the page
        touchRotation = new TouchRotation(DEFAULT_ROTATION_X, DEFAULT_ROTATION_Y);
    }

    /**
     * Update function overridden from the Scene parent class. The update function should contain
     * the logic in the scene that needs to be updated frequently.
     *
     * @param deltaTime Timing value used to update logic based on time passed instead of update
     *                  frequency
     * @see             Scene
     * @since           1.0
     */
    @Override
    public void update(float deltaTime)
    {
        // Update the touch rotation object to keep updating the rotation velocity
        touchRotation.update(deltaTime);

        // Update the loading icon to spin
        loadingIcon.update(deltaTime);

        // Update the custom buttons (because they have colours when held)
        backButton.update(deltaTime);
        infoButton.update(deltaTime);
        leftButton.update(deltaTime);
        rightButton.update(deltaTime);

        // Update the model matrix to the most recent touch rotation values
        modelMatrix.reset();
        modelMatrix.rotate(touchRotation.getRotationY(), 1.0f, 0.0f, 0.0f);
        modelMatrix.rotate(touchRotation.getRotationX(), 0.0f, 1.0f, 0.0f);
        modelMatrix.scale(DEFAULT_MODEL_SCALE);

        // Update the fade transition object
        fadeTransition.update(deltaTime);
    }

    /**
     * Render function overridden from the Scene parent class. The render function should contain
     * the draw. The function is where the user interface is drawn so it is processed by the engine.
     * It also renders the 3D model.
     *
     * @see     Scene
     * @since   1.0
     */
    @Override
    public void render()
    {
        // If the model exists, render it
        if(gripModel != null && deckModel != null)
        {
            deckModel.render(modelCamera, modelMatrix);
            gripModel.render(modelCamera, modelMatrix);
        }

        // Draw all of the user interface
        nextButton.draw(uiCamera);
        backButton.draw(uiCamera);
        infoButton.draw(uiCamera);
        leftButton.draw(uiCamera);
        rightButton.draw(uiCamera);
        titleText.draw(uiCamera);
        selectedObjectText.draw(uiCamera);
        loadingIcon.draw(uiCamera);
        infoPanel.draw(uiCamera);
        fadeTransition.draw(uiCamera);
    }

    /**
     * Touch function overridden from the Scene parent class. The touch function allows you to
     * intercept user touch input and process it. The touch control on this page allows the user to
     * control the rotation of the model view. It also feeds the touch event to the info panel.
     *
     * @param type      The type of touch event (e.g. click, release or drag)
     * @param position  The position of the touch event (x, y)
     * @see             Scene
     * @since           1.0
     */
    @Override
    public void touch(int type, Point2D position)
    {
        // If the info panel is not visible, feed the touch rotation the touch event. This is
        // so that the touch does not work whilst the info is being displayed. If the info panel is
        // visible, feed the touch event to that instead.
        if(!infoPanel.isVisible())
        {
            touchRotation.touch(type, position);
        }
        else
        {
            infoPanel.touch(type, position);
        }
    }

    /**
     * Disable or enable all of the interactable user interface objects on the page
     *
     * @since   1.0
     */
    private void setEnabledStateAll(boolean state)
    {
        leftButton.setEnabled(state);
        rightButton.setEnabled(state);
        infoButton.setEnabled(state);
        backButton.setEnabled(state);
    }

    /**
     * Initialise, position and set-up all of the user interface on the page.
     *
     * @since   1.0
     */
    private void initUI()
    {
        // Create the information panel
        infoPanel = new InfoPanel();

        // Visibility listener to run code when the info panel is shown or hidden
        infoPanel.setVisibilityListener(new InfoPanel.VisibilityListener()
        {
            /**
             * Called when the information panel is shown. Used to disable all the interactable user
             * interface on the page so that they cannot be interacted with whilst the information
             * panel is visible.
             *
             * @since   1.0
             */
            @Override
            public void onShow()
            {
                // Disable UI
                setEnabledStateAll(false);
            }

            /**
             * Called when the information panel is hidden. Used to enable all the interactable user
             * interface on the page because it may have been previously disabled when the
             * information panel was last shown.
             *
             * @since   1.0
             */
            @Override
            public void onHide()
            {
                // Enable UI
                setEnabledStateAll(true);
            }
        });

        // Create the fade transition object and set it to fade in
        fadeTransition = new FadeTransition();
        fadeTransition.setFadeColour(Constants.BACKGROUND_COLOR);
        fadeTransition.fadeIn();

        // Create the loading icon
        loadingIcon = new LoadingIcon();

        // The font for the title text and next button text
        Font aileronRegularFont = new Font(R.raw.aileron_regular, 76);

        backButton = new CustomButton(R.drawable.back_icon);
        backButton.setPosition(Constants.getBackButtonPosition());
        backButton.setSize(Constants.BACK_BUTTON_SIZE);
        backButton.addTouchListener(e ->
        {
            if(e.getEvent() == TouchEvent.Event.RELEASE)
            {
                fadeTransition.fadeOutToScence(SelectDeckDesignScene::new);
            }
        });

        infoButton = new CustomButton(R.drawable.info_icon);
        infoButton.setPosition(Constants.getInfoButtonPosition());
        infoButton.setSize(Constants.INFO_BUTTON_SIZE);
        infoButton.addTouchListener(e ->
        {
            if(e.getEvent() == TouchEvent.Event.RELEASE)
            {
                infoPanel.setText(currentGrip.info);
                infoPanel.show();
            }
        });

        nextButton = new Button(aileronRegularFont, "Next");
        nextButton.setSize(Constants.NEXT_BUTTON_SIZE);
        nextButton.setPosition(Constants.getNextButtonPosition());
        nextButton.setColour(Constants.BACKGROUND_COLOR);
        nextButton.setBorder(new Border(Colour.WHITE, 8));
        nextButton.setTextColour(Colour.WHITE);
        nextButton.addTouchListener(e ->
        {
            if(e.getEvent() == TouchEvent.Event.RELEASE)
            {
                subject.setGrip(currentGrip.id);

                // Save the current skateboard
                SaveManager.writeCurrentSave(subject);

                fadeTransition.fadeOutToScence(SelectTrucksScene::new);
            }
        });

        leftButton = new CustomButton(R.drawable.arrow_left);
        leftButton.setSize(240.0f, 240.0f);
        leftButton.setPosition(20.0f, (Crispin.getSurfaceHeight() / 2.0f) -
                (leftButton.getSize().y / 2.0f));
        leftButton.addTouchListener(e ->
        {
            if(e.getEvent() == TouchEvent.Event.RELEASE)
            {
                previousGrip();
            }
        });

        rightButton = new CustomButton(R.drawable.arrow_right);
        rightButton.setSize(240.0f, 240.0f);
        rightButton.setPosition(Crispin.getSurfaceWidth() - 20.0f -rightButton.getSize().x,
                (Crispin.getSurfaceHeight() / 2.0f) - (rightButton.getSize().y / 2.0f));
        rightButton.addTouchListener(e ->
        {
            if(e.getEvent() == TouchEvent.Event.RELEASE)
            {
                nextGrip();
            }
        });

        // Create the title text
        titleText = new Text(aileronRegularFont, "Select Griptape", false,
                true, Crispin.getSurfaceWidth());

        // Calculate the position of the title text
        final Point2D TITLE_TEXT_POSITION = new Point2D(0.0f,
                Constants.getBackButtonPosition().y - Constants.BACK_BUTTON_PADDING.y -
                        titleText.getHeight());

        titleText.setColour(Colour.WHITE);
        titleText.setPosition(TITLE_TEXT_POSITION);

        // Font for the text that displays the name of the currently selected object
        Font aileronRegularFontSmall = new Font(R.raw.aileron_regular, 48);

        // Create the text that displays the name of the currently selected object
        selectedObjectText = new Text(aileronRegularFontSmall, "No selected object",
                true, true, Crispin.getSurfaceWidth());

        // Calculate the position of the selected object text
        final Point2D SELECTED_OBJECT_TEXT_POSITION = new Point2D(0.0f,TITLE_TEXT_POSITION.y -
                selectedObjectText.getHeight() - Constants.SELECTED_OBJECT_TEXT_PADDING.y);

        selectedObjectText.setColour(Colour.WHITE);
        selectedObjectText.setPosition(SELECTED_OBJECT_TEXT_POSITION);
    }

    /**
     * Set the grip using the grip array index and the grip array. Fetches the material from the
     * material map based on the grip ID. This is so that a material doesn't have to be loaded on
     * button press.
     *
     * @since   1.0
     */
    private void setGrip()
    {
        // Check if the grip array doesn't exist or have any contents
        if(grips == null || grips.isEmpty())
        {
            Logger.error(TAG, "No grips to iterate through");

            // If there is a model, set it to use the no texture material
            if(gripModel != null)
            {
                gripModel.setMaterial(noTexture);
            }
        }
        else
        {
            // Check if the grip index is within range of the array
            if(gripArrayIndex >= grips.size())
            {
                gripArrayIndex = 0;
            }
            else if(gripArrayIndex < 0)
            {
                gripArrayIndex = grips.size() - 1;
            }

            // Set the new current grip
            currentGrip = grips.get(gripArrayIndex);

            // Set the selected object text to display the name of the new grip
            selectedObjectText.setText(currentGrip.name);

            Logger.info("Switching grip to ID[" + currentGrip.id + "]: " +
                    currentGrip.name);

            // If the model exists, set the material to the new grip
            if(gripModel == null)
            {
                Logger.error(TAG, "Model does not exist. Cannot set material.");
            }
            else
            {
                gripModel.setMaterial(gripMaterials.get(currentGrip.id));
            }
        }
    }

    /**
     * Set the grip material to be the next one in the array
     *
     * @see     #setGrip()
     * @since   1.0
     */
    private void nextGrip()
    {
        gripArrayIndex++;
        setGrip();
    }

    /**
     * Set the grip material to be the previous one in the array
     *
     * @see     #setGrip()
     * @since   1.0
     */
    private void previousGrip()
    {
        gripArrayIndex--;
        setGrip();
    }

    /**
     * Load a deck and grip model for a specific skateboard.
     *
     * @param skateboard    The skateboard containing the deck and grip ID to load
     * @see                 Skateboard
     * @since               1.0
     */
    private void loadModels(Skateboard skateboard)
    {
        // Get the currently selected deck
        Deck selectedDeck = DeckConfigReader.getInstance().getDeck(skateboard.getDeck());

        // Get the currently selected design
        Design selectedDesign = DesignConfigReader.getInstance().getDesign(skateboard.getDesign());

        // If the selected deck exists
        if(selectedDeck != null)
        {
            Logger.info("Loading deck ID[" + selectedDeck.modelId + "]: and grip ID[" +
                    selectedDeck.gripModelId);

            // Load the selected deck model and apply a design to it when loaded
            ThreadedOBJLoader.loadModel(selectedDeck.modelId, model ->
            {
                this.deckModel = model;

                // Apply the design to the model
                this.deckModel.setMaterial(new Material(selectedDesign.resourceId));
            });

            // Load the grip model that is associated to the deck
            ThreadedOBJLoader.loadModel(selectedDeck.gripModelId, model ->
            {
                this.gripModel = model;

                // If the skateboard subject has a griptape, load that by default
                if(skateboard.getGrip() != Skateboard.NO_PART)
                {
                    // Set the new current grip
                    currentGrip = GripConfigReader.getInstance().
                            getGrip(skateboard.getGrip());

                    // Set the selected object text to display the name of the new grip
                    selectedObjectText.setText(currentGrip.name);

                    // Set the model material
                    model.setMaterial(gripMaterials.get(currentGrip.id));
                }
                else
                {
                    // Apply a grip material to the model
                    nextGrip();
                }
            });
        }
        else
        {
            Logger.error(TAG, "Failed to load deck because ID[" + skateboard.getDeck() +
                    "] does not exist.");
        }
    }
}
