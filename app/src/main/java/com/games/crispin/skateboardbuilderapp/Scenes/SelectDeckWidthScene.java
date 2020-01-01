package com.games.crispin.skateboardbuilderapp.Scenes;

import android.widget.Toast;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Font;
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
import com.games.crispin.crispinmobile.Utilities.Scene;
import com.games.crispin.crispinmobile.Utilities.ThreadedOBJLoader;
import com.games.crispin.skateboardbuilderapp.ConfigReaders.DeckConfigReader;
import com.games.crispin.skateboardbuilderapp.ConfigReaders.SaveManager;
import com.games.crispin.skateboardbuilderapp.Constants;
import com.games.crispin.skateboardbuilderapp.CustomButton;
import com.games.crispin.skateboardbuilderapp.FadeTransition;
import com.games.crispin.skateboardbuilderapp.LoadingIcon;
import com.games.crispin.skateboardbuilderapp.R;
import com.games.crispin.skateboardbuilderapp.SkateboardComponents.Deck;
import com.games.crispin.skateboardbuilderapp.SkateboardComponents.Skateboard;
import com.games.crispin.skateboardbuilderapp.TouchRotation;

import java.util.HashMap;
import java.util.List;

import static com.games.crispin.skateboardbuilderapp.Constants.SELECT_DECK_WIDTH_DROPDOWN_PADDING;
import static com.games.crispin.skateboardbuilderapp.Constants.SELECT_DECK_WIDTH_DROPDOWN_SIZE;

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

    // The loading icon UI
    private LoadingIcon loadingIcon;

    // Back button UI (return to home screen)
    private CustomButton backButton;

    // Next button UI
    private Button nextButton;

    // Width select dropdown UI
    private Dropdown widthSelectDropdown;

    // Select deck width text UI
    private Text titleText;

    // The skateboard that is being worked on
    private Skateboard subject;

    // Decks loaded from configuration file
    private List<Deck> decks;

    // Decks with associated IDs in the dropdown user interface
    private HashMap<Integer, Deck> dropdownUIDecks;

    public SelectDeckWidthScene()
    {
        // Set the background to a blue colour
        Crispin.setBackgroundColour(Constants.BACKGROUND_COLOR);

        // Load the default board material
        BOARD_GREY = new Material(new Texture(R.drawable.grey));

        // Read the current save if there is one
        subject = SaveManager.loadCurrentSave();

        // Create the user interface camera
        uiCamera = new Camera2D(0, 0, Crispin.getSurfaceWidth(), Crispin.getSurfaceHeight());

        // Create the model camera and move it forward in-front of the origin
        modelCamera = new Camera3D();
        modelCamera.setPosition(new Point3D(0.0f, 0.0f, 7.0f));

        // Position, re-size, colour and add touch listeners to the UI
        setupUI();

        // Load the decks from the config file into the dropdown UI
        loadDeckConfig();

        // Create the model matrix for transforming the model
        modelMatrix = new ModelMatrix();

        // Create touch rotation object that calculated the rotation for the 3D view when the user
        // interacts with the page
        touchRotation = new TouchRotation(DEFAULT_ROTATION_X, DEFAULT_ROTATION_Y);
    }

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

    @Override
    public void update(float deltaTime)
    {
        // Update the touch rotation object to keep updating the rotation velocity
        touchRotation.update(deltaTime);

        // Update the loading icon to spin
        loadingIcon.update(deltaTime);

        // Update the custom buttons (because they have colours when held)
        backButton.update(deltaTime);

        // Update the model matrix to the most recent touch rotation values
        modelMatrix.reset();
        modelMatrix.rotate(touchRotation.getRotationY(), 1.0f, 0.0f, 0.0f);
        modelMatrix.rotate(touchRotation.getRotationX(), 0.0f, 1.0f, 0.0f);
        modelMatrix.scale(DEFAULT_MODEL_SCALE);

        // Update the fade transition object
        fadeTransition.update(deltaTime);
    }

    @Override
    public void render()
    {
        // If the model exists and is currently not loading, render it
        if(model != null && !loadingIcon.isShown())
        {
            model.render(modelCamera, modelMatrix);
        }

        // Draw all of the user interface
        titleText.draw(uiCamera);
        widthSelectDropdown.draw(uiCamera);
        backButton.draw(uiCamera);
        nextButton.draw(uiCamera);
        loadingIcon.draw(uiCamera);
        fadeTransition.draw(uiCamera);
    }

    @Override
    public void touch(int type, Point2D position)
    {
        touchRotation.touch(type, position);
    }

    private void setupUI()
    {
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
        nextButton = new Button(aileronRegularFont, "Next");
        titleText = new Text(aileronRegularFont, SCENE_TITLE_TEXT, false,
                true, Crispin.getSurfaceWidth());
        widthSelectDropdown = new Dropdown("Select Width");


        backButton.setPosition(Constants.getBackButtonPosition());
        backButton.setSize(Constants.BACK_BUTTON_SIZE);

        nextButton.setSize(Constants.NEXT_BUTTON_SIZE);
        nextButton.setPosition(Constants.getNextButtonPosition());
        nextButton.setColour(Constants.BACKGROUND_COLOR);
        nextButton.setBorder(new Border(Colour.WHITE, 8));
        nextButton.setTextColour(Colour.WHITE);
        nextButton.setEnabled(false);

        final Point2D TITLE_TEXT_POSITION = new Point2D(0.0f,
                Constants.getBackButtonPosition().y -
                Constants.BACK_BUTTON_PADDING.y - titleText.getHeight());

        titleText.setColour(Colour.WHITE);
        titleText.setPosition(TITLE_TEXT_POSITION);

        final Point2D SELECT_DECK_WIDTH_DROPDOWN_POSITION = new Point2D(
                SELECT_DECK_WIDTH_DROPDOWN_PADDING.x, TITLE_TEXT_POSITION.y -
                SELECT_DECK_WIDTH_DROPDOWN_SIZE.y - SELECT_DECK_WIDTH_DROPDOWN_PADDING.y);

        widthSelectDropdown.setPosition(SELECT_DECK_WIDTH_DROPDOWN_POSITION);
        widthSelectDropdown.setSize(SELECT_DECK_WIDTH_DROPDOWN_SIZE);
        widthSelectDropdown.setDisabledBorders(Dropdown.INNER_BORDERS);
        widthSelectDropdown.setColour(Constants.BACKGROUND_COLOR);
        widthSelectDropdown.setTextColour(Colour.WHITE);
        widthSelectDropdown.setBorderColour(Colour.WHITE);
        widthSelectDropdown.setStateIcons(R.drawable.expand_icon, R.drawable.collapse_icon);

        // Add touch listener to back button
        backButton.addTouchListener(e ->
        {
            switch (e.getEvent())
            {
                case RELEASE:
                    fadeTransition.fadeOutToScence(HomeScene::new);
                    break;
            }
        });

        // Add touch listener to next button
        nextButton.addTouchListener(e ->
        {
            switch (e.getEvent())
            {
                case RELEASE:

                    if(subject.getDeck() != Skateboard.NO_PART)
                    {
                        // Save the currently selected board
                        SaveManager.writeCurrentSave(subject);
                        fadeTransition.fadeOutToScence(SelectDeckDesignScene::new);
                    }
                    else
                    {
                        Toast.makeText(Crispin.getApplicationContext(),
                                "Selected part invalid",
                                Toast.LENGTH_LONG);
                    }
                    break;
            }
        });

        // Add touch listener to dropdown
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
                        Deck deck = dropdownUIDecks.get(selectedId);
                        ThreadedOBJLoader.loadModel(deck.modelId, model ->
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
                            subject.setDeck(deck.id);
                        });
                    }

                    break;
            }
        });
    }
}
