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
import com.games.crispin.skateboardbuilderapp.ConfigReaders.SaveManager;
import com.games.crispin.skateboardbuilderapp.ConfigReaders.TruckConfigReader;
import com.games.crispin.skateboardbuilderapp.Constants;
import com.games.crispin.skateboardbuilderapp.UserInterface.CustomButton;
import com.games.crispin.skateboardbuilderapp.UserInterface.FadeTransition;
import com.games.crispin.skateboardbuilderapp.UserInterface.InfoPanel;
import com.games.crispin.skateboardbuilderapp.UserInterface.LoadingIcon;
import com.games.crispin.skateboardbuilderapp.R;
import com.games.crispin.skateboardbuilderapp.SkateboardComponents.Skateboard;
import com.games.crispin.skateboardbuilderapp.SkateboardComponents.Truck;
import com.games.crispin.skateboardbuilderapp.UserInterface.TouchRotation;
import com.games.crispin.skateboardbuilderapp.UserInterface.VisibilityListener;

import java.util.HashMap;
import java.util.List;

/**
 * The SelectTrucksScene class manages and displays a page that allows the user to select the trucks
 * for their skateboard design. The class extends the CrispinEngine class 'Scene' which allows the
 * engine to handle it.
 *
 * @see         Scene
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
public class SelectTrucksScene extends Scene
{
    // Tag used for logging
    private static final String TAG = "SelectTrucksScene";

    // Scale for the model
    private static final float DEFAULT_MODEL_SCALE = 0.2f;

    // Default X axis rotation for the touch rotation
    private static final float DEFAULT_ROTATION_X = 0.0f;

    // Default Y axis rotation for the touch rotation
    private static final float DEFAULT_ROTATION_Y = 225.0f;

    // Camera for 2D/user interface rendering
    private Camera2D uiCamera;

    // Camera for 3D model rendering
    private Camera3D modelCamera;

    // The skateboard that is being worked on
    private Skateboard subject;

    // The current truck
    private Truck currentTruck;

    // List of the trucks
    private List<Truck> trucks;

    // Index for the list of trucks
    private int truckArrayIndex;

    // Materials stored with their respective truck ID
    private HashMap<Integer, Material> truckMaterials;

    // Material for no texture
    private Material noTexture;

    // Render object model
    private Model model;

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

    // Left button for selecting the previous truck in the list
    private CustomButton leftButton;

    // Right button for selecting the next truck in the list
    private CustomButton rightButton;

    // Select deck width text UI
    private Text titleText;

    // Selected object text
    private Text selectedObjectText;

    /**
     * Constructor to create the select deck truck scene. Creates all of the UI, loads the model
     * and loads all of the trucks.
     *
     * @since   1.0
     */
    public SelectTrucksScene()
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

        // Create truck object with default values
        currentTruck = new Truck();

        // Get the trucks from the config reader
        trucks = TruckConfigReader.getInstance().getTrucks();
        truckArrayIndex = 0;

        // Create the materials map
        truckMaterials = new HashMap<>();

        // Load all the materials
        for(Truck temp : trucks)
        {
            truckMaterials.put(temp.id, new Material(temp.resourceId));
        }

        // Material for when no texture has been found
        noTexture = new Material(R.drawable.no_material);

        // Initialise all of the user interface on the page
        initUI();

        // If the skateboard subject has a truck, load that by default
        if(subject.getTrucks() != Skateboard.NO_PART)
        {
            // Set the new current grip
            currentTruck = TruckConfigReader.getInstance().getTruck(subject.getTrucks());

            // Set the selected object text to display the name of the new truck
            selectedObjectText.setText(currentTruck.name);

            // Show the loading icon
            loadingIcon.show();

            // Load new truck
            ThreadedOBJLoader.loadModel(currentTruck.modelResourceId, model ->
            {
                this.model = model;
                loadingIcon.hide();
            });

            // Set the model material
            model.setMaterial(truckMaterials.get(currentTruck.id));
        }
        else
        {
            // Apply a truck material to the model
            nextTruck();
        }

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
        if(model != null)
        {
            model.render(modelCamera, modelMatrix);
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
        infoPanel.setVisibilityListener(new VisibilityListener()
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
                fadeTransition.fadeOutToScence(SelectGriptapeScene::new);
            }
        });

        infoButton = new CustomButton(R.drawable.info_icon);
        infoButton.setPosition(Constants.getInfoButtonPosition());
        infoButton.setSize(Constants.INFO_BUTTON_SIZE);
        infoButton.addTouchListener(e ->
        {
            if(e.getEvent() == TouchEvent.Event.RELEASE)
            {
                infoPanel.setText(currentTruck.info);
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
                subject.setTrucks(currentTruck.id);

                // Save the current skateboard
                SaveManager.writeCurrentSave(subject);

                fadeTransition.fadeOutToScence(SelectBearingsScene::new);
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
                previousTruck();
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
                nextTruck();
            }
        });

        // Create the title text
        titleText = new Text(aileronRegularFont, "Select Trucks", false,
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
     *
     *
     * @since   1.0
     */
    private void setTruck()
    {
        // Check if the trucks array doesn't exist or have any contents
        if(trucks == null || trucks.isEmpty())
        {
            Logger.error(TAG, "No trucks to iterate through");

            // If there is a model, set it to use the no texture material
            if(model != null)
            {
                model.setMaterial(noTexture);
            }
        }
        else
        {
            // Check if the truck index is within range of the array
            if(truckArrayIndex >= trucks.size())
            {
                truckArrayIndex = 0;
            }
            else if(truckArrayIndex < 0)
            {
                truckArrayIndex = trucks.size() - 1;
            }

            // Set the new current truck
            currentTruck = trucks.get(truckArrayIndex);

            // Set the selected object text to display the name of the new truck
            selectedObjectText.setText(currentTruck.name);

            Logger.info("Switching truck to ID[" + currentTruck.id + "]: " +
                    currentTruck.name);

            loadingIcon.show();
            model = null;

            // Load new truck
            ThreadedOBJLoader.loadModel(currentTruck.modelResourceId, model ->
            {
                this.model = model;
                loadingIcon.hide();
            });

            // If the model exists, set the material to the new truck
            if(model == null)
            {
                Logger.error(TAG, "Model does not exist. Cannot set material.");
            }
            else
            {
                model.setMaterial(truckMaterials.get(currentTruck.id));
            }
        }
    }

    /**
     * Set the truck of the deck to the next one in the array
     *
     * @see     #setTruck() ()
     * @since   1.0
     */
    private void nextTruck()
    {
        truckArrayIndex++;
        setTruck();
    }

    /**
     * Set the truck of the deck to the previous one in the array
     *
     * @see     #setTruck() ()
     * @since   1.0
     */
    private void previousTruck()
    {
        truckArrayIndex--;
        setTruck();
    }
}
