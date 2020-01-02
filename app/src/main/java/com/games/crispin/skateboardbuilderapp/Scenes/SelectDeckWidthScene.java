package com.games.crispin.skateboardbuilderapp.Scenes;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.UserInterface.Font;
import com.games.crispin.crispinmobile.Rendering.Utilities.Material;
import com.games.crispin.crispinmobile.Rendering.Utilities.Model;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera3D;
import com.games.crispin.crispinmobile.Rendering.Utilities.ModelMatrix;
import com.games.crispin.crispinmobile.Rendering.Utilities.Texture;
import com.games.crispin.crispinmobile.UserInterface.Border;
import com.games.crispin.crispinmobile.UserInterface.Button;
import com.games.crispin.crispinmobile.UserInterface.Dropdown;
import com.games.crispin.crispinmobile.UserInterface.Text;
import com.games.crispin.crispinmobile.UserInterface.TouchEvent;
import com.games.crispin.crispinmobile.Utilities.Scene;
import com.games.crispin.crispinmobile.Utilities.ThreadedOBJLoader;
import com.games.crispin.skateboardbuilderapp.ConfigReaders.DeckConfigReader;
import com.games.crispin.skateboardbuilderapp.ConfigReaders.SaveManager;
import com.games.crispin.skateboardbuilderapp.Constants;
import com.games.crispin.skateboardbuilderapp.CustomButton;
import com.games.crispin.skateboardbuilderapp.FadeTransition;
import com.games.crispin.skateboardbuilderapp.InfoPanel;
import com.games.crispin.skateboardbuilderapp.LoadingIcon;
import com.games.crispin.skateboardbuilderapp.R;
import com.games.crispin.skateboardbuilderapp.SkateboardComponents.Deck;
import com.games.crispin.skateboardbuilderapp.SkateboardComponents.Skateboard;
import com.games.crispin.skateboardbuilderapp.TouchRotation;

import java.util.HashMap;
import java.util.List;

import static com.games.crispin.skateboardbuilderapp.Constants.SELECT_DECK_WIDTH_DROPDOWN_PADDING;
import static com.games.crispin.skateboardbuilderapp.Constants.SELECT_DECK_WIDTH_DROPDOWN_SIZE;

/**
 * The SelectDeckWidthScene class manages and displays a page that allows the user to select the
 * desired deck width of there skateboard. The scene is accessed by clicking the 'New' button on the
 * HomeScene. The class extends the CrispinEngine class 'Scene' which allows the engine to handle
 * it.
 *
 * @see         HomeScene
 * @see         Scene
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
public class SelectDeckWidthScene extends Scene
{
    // Title of the scene
    private static final String SCENE_TITLE_TEXT = "Select Skateboard Width";

    // Material used on the board
    private final Material BOARD_GREY;

    // Scale for the model
    private static final float DEFAULT_MODEL_SCALE = 0.2f;

    // Default X axis rotation for the touch rotation
    private static final float DEFAULT_ROTATION_X = 180.0f;

    // Default Y axis rotation for the touch rotation
    private static final float DEFAULT_ROTATION_Y = 270.0f;

    // Camera for 2D/user interface rendering
    private Camera2D uiCamera;

    // Camera for 3D model rendering
    private Camera3D modelCamera;

    // Render object model
    private Model model;

    // Render object model matrix
    private ModelMatrix modelMatrix;

    // Scale of the model
    private float modelScale;

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

    // Select deck width text UI
    private Text titleText;

    // Width select dropdown UI
    private Dropdown widthSelectDropdown;

    // The skateboard that is being worked on
    private Skateboard subject;

    // The current deck that has been select
    private Deck currentDeck;

    // Decks loaded from configuration file
    private List<Deck> decks;

    // Decks with associated IDs in the dropdown user interface
    private HashMap<Integer, Deck> dropdownUIDecks;

    // Has a deck been selected yet
    private boolean deckSelected;

    /**
     * Constructor to create the select deck width scene. Creates all of the UI loads all of the
     * decks into the dropdown ready to be selected by the user.
     *
     * @since   1.0
     */
    public SelectDeckWidthScene()
    {
        // Set the background to a blue colour
        Crispin.setBackgroundColour(Constants.BACKGROUND_COLOR);

        // Load the default board material
        BOARD_GREY = new Material(new Texture(R.drawable.grey));

        // A deck has not been selected yet
        deckSelected = false;

        // The current deck width selected
        currentDeck = new Deck();

        // Get the board that is currently being worked on
        subject = SaveManager.loadCurrentSave();

        // Create the user interface camera
        uiCamera = new Camera2D(0, 0, Crispin.getSurfaceWidth(), Crispin.getSurfaceHeight());

        // Create the model camera and move it forward in-front of the origin
        modelCamera = new Camera3D();
        modelCamera.setPosition(new Point3D(0.0f, 0.0f, 7.0f));

        // Position, re-size, colour and add touch listeners to the UI
        initUI();

        // Load the decks from the config file into the dropdown UI
        loadDeckConfig();

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
        // If the model exists and is currently not loading, render it
        if(model != null && !loadingIcon.isShown())
        {
            model.render(modelCamera, modelMatrix);
        }

        // Draw all of the user interface
        backButton.draw(uiCamera);

        // Only draw the info button if a deck has been selected
        if(deckSelected)
        {
            infoButton.draw(uiCamera);
        }

        nextButton.draw(uiCamera);
        titleText.draw(uiCamera);
        widthSelectDropdown.draw(uiCamera);
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
        infoButton.setEnabled(state);
        backButton.setEnabled(state);
        widthSelectDropdown.setEnabled(state);
        nextButton.setEnabled(state);
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

        // The font for text on the scene
        Font aileronRegularFont = new Font(R.raw.aileron_regular, 76);

        // Create the back button
        backButton = new CustomButton(R.drawable.back_icon);
        backButton.setPosition(Constants.getBackButtonPosition());
        backButton.setSize(Constants.BACK_BUTTON_SIZE);
        backButton.addTouchListener(e ->
        {
            switch (e.getEvent())
            {
                case RELEASE:
                    fadeTransition.fadeOutToScence(HomeScene::new);
                    break;
            }
        });

        infoButton = new CustomButton(R.drawable.info_icon);
        infoButton.setEnabled(false);
        infoButton.setPosition(Constants.getInfoButtonPosition());
        infoButton.setSize(Constants.INFO_BUTTON_SIZE);
        infoButton.addTouchListener(e ->
        {
            if(e.getEvent() == TouchEvent.Event.RELEASE)
            {
                infoPanel.setText(currentDeck.info);
                infoPanel.show();
            }
        });

        nextButton = new Button(aileronRegularFont, "Next");
        nextButton.setSize(Constants.NEXT_BUTTON_SIZE);
        nextButton.setPosition(Constants.getNextButtonPosition());
        nextButton.setColour(Constants.BACKGROUND_COLOR);
        nextButton.setBorder(new Border(Colour.WHITE, 8));
        nextButton.setTextColour(Colour.WHITE);
        nextButton.setEnabled(false);
        nextButton.addTouchListener(e ->
        {
            switch (e.getEvent())
            {
                case RELEASE:
                    if(deckSelected)
                    {
                        subject.setDeck(currentDeck.id);

                        // Save the currently selected board
                        SaveManager.writeCurrentSave(subject);

                        fadeTransition.fadeOutToScence(SelectDeckDesignScene::new);
                    }
                    break;
            }
        });

        titleText = new Text(aileronRegularFont, SCENE_TITLE_TEXT, false,
                true, Crispin.getSurfaceWidth());

        final Point2D TITLE_TEXT_POSITION = new Point2D(0.0f,
                Constants.getBackButtonPosition().y -
                        Constants.BACK_BUTTON_PADDING.y - titleText.getHeight());

        titleText.setColour(Colour.WHITE);
        titleText.setPosition(TITLE_TEXT_POSITION);

        final Point2D SELECT_DECK_WIDTH_DROPDOWN_POSITION = new Point2D(
                SELECT_DECK_WIDTH_DROPDOWN_PADDING.x, TITLE_TEXT_POSITION.y -
                SELECT_DECK_WIDTH_DROPDOWN_SIZE.y - SELECT_DECK_WIDTH_DROPDOWN_PADDING.y);

        widthSelectDropdown = new Dropdown("Select Width");
        widthSelectDropdown.setPosition(SELECT_DECK_WIDTH_DROPDOWN_POSITION);
        widthSelectDropdown.setSize(SELECT_DECK_WIDTH_DROPDOWN_SIZE);
        widthSelectDropdown.setDisabledBorders(Dropdown.INNER_BORDERS);
        widthSelectDropdown.setColour(Constants.BACKGROUND_COLOR);
        widthSelectDropdown.setTextColour(Colour.WHITE);
        widthSelectDropdown.setBorderColour(Colour.WHITE);
        widthSelectDropdown.setStateIcons(R.drawable.expand_icon, R.drawable.collapse_icon);
        widthSelectDropdown.addTouchListener(e ->
        {
            switch (e.getEvent())
            {
                case CLICK:

                    break;
                case RELEASE:
                    int selectedId = widthSelectDropdown.getSelectedId();

                    if(dropdownUIDecks.containsKey(selectedId))
                    {
                        model = null;
                        loadingIcon.show();

                        // Load the deck associated to the selected ID
                        currentDeck = dropdownUIDecks.get(selectedId);

                        infoButton.setEnabled(true);
                        deckSelected = true;

                        ThreadedOBJLoader.loadModel(currentDeck.modelId, model ->
                        {
                            this.model = model;
                            this.model.setMaterial(BOARD_GREY);

                            modelScale = 0.2f;

                            modelMatrix.reset();
                            modelMatrix.rotate(touchRotation.getRotationY(), 1.0f, 0.0f, 0.0f);
                            modelMatrix.rotate(touchRotation.getRotationX(), 0.0f, 1.0f, 0.0f);
                            modelMatrix.scale(modelScale);

                            loadingIcon.hide();
                            nextButton.setEnabled(true);
                        });
                    }

                    break;
            }
        });
    }

    /**
     * Load the decks from the deck config file and put them in the dropdown user interface
     *
     * @see     DeckConfigReader
     * @since   1.0
     */
    private void loadDeckConfig()
    {
        decks = DeckConfigReader.getInstance().getDecks();
        dropdownUIDecks = new HashMap<>();

        // Add the decks that have been loaded from the configuration file to a map so that they
        // can be accessed easily later.
        for(Deck deck : decks)
        {
            final int tempId = widthSelectDropdown.addItem(deck.name);
            dropdownUIDecks.put(tempId, deck);
        }
    }
}
