package com.games.crispin.skateboardbuilderapp.Scenes;

import android.app.AlertDialog;
import android.text.InputFilter;
import android.text.InputType;
import android.widget.EditText;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Geometry.Scale2D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera3D;
import com.games.crispin.crispinmobile.UserInterface.Font;
import com.games.crispin.crispinmobile.Rendering.Utilities.Material;
import com.games.crispin.crispinmobile.UserInterface.Border;
import com.games.crispin.crispinmobile.UserInterface.Button;
import com.games.crispin.crispinmobile.UserInterface.Text;
import com.games.crispin.crispinmobile.UserInterface.TouchEvent;
import com.games.crispin.crispinmobile.UserInterface.TouchListener;
import com.games.crispin.crispinmobile.Utilities.Scene;
import com.games.crispin.crispinmobile.Utilities.ThreadedOBJLoader;
import com.games.crispin.skateboardbuilderapp.ConfigReaders.BearingConfigReader;
import com.games.crispin.skateboardbuilderapp.ConfigReaders.DeckConfigReader;
import com.games.crispin.skateboardbuilderapp.ConfigReaders.DesignConfigReader;
import com.games.crispin.skateboardbuilderapp.ConfigReaders.GripConfigReader;
import com.games.crispin.skateboardbuilderapp.ConfigReaders.SaveManager;
import com.games.crispin.skateboardbuilderapp.ConfigReaders.TruckConfigReader;
import com.games.crispin.skateboardbuilderapp.ConfigReaders.WheelConfigReader;
import com.games.crispin.skateboardbuilderapp.Constants;
import com.games.crispin.skateboardbuilderapp.UserInterface.CustomButton;
import com.games.crispin.skateboardbuilderapp.FadeTransition;
import com.games.crispin.skateboardbuilderapp.LoadingIcon;
import com.games.crispin.skateboardbuilderapp.SkateboardComponents.ComponentModel;
import com.games.crispin.skateboardbuilderapp.R;
import com.games.crispin.skateboardbuilderapp.SkateboardComponents.Bearing;
import com.games.crispin.skateboardbuilderapp.SkateboardComponents.Deck;
import com.games.crispin.skateboardbuilderapp.SkateboardComponents.Design;
import com.games.crispin.skateboardbuilderapp.SkateboardComponents.Grip;
import com.games.crispin.skateboardbuilderapp.SkateboardComponents.Skateboard;
import com.games.crispin.skateboardbuilderapp.SkateboardComponents.Truck;
import com.games.crispin.skateboardbuilderapp.SkateboardComponents.Wheel;
import com.games.crispin.skateboardbuilderapp.TouchRotation;

import java.util.ArrayList;
import java.util.List;

/**
 * The ViewBoardScene class manages and displays a page that allows the user to view the board that
 * they have designed in the previous component selection scenes. The class
 * extends the CrispinEngine class 'Scene' which allows the engine to handle it.
 *
 * @see         Scene
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
public class ViewBoardScene extends Scene
{
    // Tag used for logging
    private static final String TAG = "ViewBoardScene";

    // Default name of the skateboard design
    private static final String DEFAULT_NAME = "Skateboard";

    // Scale of the deck model
    private static final float DECK_SCALE = 0.2f;

    // Scale of the grip model
    private static final float GRIP_SCALE = 0.2f;

    // Scale of the truck models
    private static final float TRUCK_SCALE = 0.07f;

    // Scale of the wheel models
    private static final float WHEEL_SCALE = 1.157f * TRUCK_SCALE;

    // Scale of the bearing models
    private static final float BEARING_SCALE = 3.040f * TRUCK_SCALE;

    // Camera for 2D/user interface rendering
    private Camera2D uiCamera;

    // Camera for 3D model rendering
    private Camera3D modelCamera;

    // The skateboard that is being worked on
    private Skateboard subject;

    // The list of skateboard saves from the saves config file
    private List<Skateboard> saves;

    // The given name of the skateboard
    private String name;

    // Has a name been given to the skateboard yet
    private boolean nameGiven;

    // Position of an outer bearing relative to a truck
    private Point3D bearingOuterRelativePos;

    // Position of an inner bearing relative to a truck
    private Point3D bearingInnerRelativePos;

    // Position of the first bearing
    private Point3D bearingOneRealPos;

    // Position of the second bearing
    private Point3D bearingTwoRealPos;

    // Position of the third bearing
    private Point3D bearingThreeRealPos;

    // Position of the fourth bearing
    private Point3D bearingFourRealPos;

    // Position of the fifth bearing
    private Point3D bearingFiveRealPos;

    // Position of the sixth bearing
    private Point3D bearingSixRealPos;

    // Position of the seventh bearing
    private Point3D bearingSevenRealPos;

    // Position of the eighth bearing
    private Point3D bearingEightRealPos;

    // Position of the first wheel
    private Point3D wheelOneRealPos;

    // Position of the second wheel
    private Point3D wheelTwoRealPos;

    // Position of the third wheel
    private Point3D wheelThreeRealPos;

    // Position of the fourth wheel
    private Point3D wheelFourRealPos;

    // Position of the first truck relative to the deck
    private Point3D truckOnePos;

    // Position of the second truck relative to the deck
    private Point3D truckTwoPos;

    // Position of a wheel relative to a truck
    private Point3D wheelRelativePos;

    // The deck model
    private ComponentModel deck;

    // The grip model
    private ComponentModel grip;

    // The first truck model
    private ComponentModel truckOne;

    // The second truck model
    private ComponentModel truckTwo;

    // All of the wheel models
    private ArrayList<ComponentModel> wheelModels;

    // All of the bearing models
    private ArrayList<ComponentModel> bearingModels;

    // Touch rotation object calculates the rotation of the interactable model
    private TouchRotation touchRotation;

    // Transition object that allows us to fade the scene in and out
    private FadeTransition fadeTransition;

    // The loading icon UI
    private LoadingIcon loadingIcon;

    // Back button UI (return to home screen)
    private CustomButton backButton;

    // Edit name button
    private CustomButton editNameButton;

    // Next button UI
    private Button saveButton;

    // Select deck width text UI
    private Text titleText;

    /**
     * The view board scene allows the user to see the board that they have designed throughout the
     * component selection scenes. It will read the current save configuration and build the
     * skateboard using relative positions stored in the component configuration data.
     *
     * @since   1.0
     */
    public ViewBoardScene()
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
        subject.print();

        // Load the saves
        saves = SaveManager.loadSaves();

        // Use default name to begin with
        name = subject.getName();
        nameGiven = false;

        // Load all of the components from their configs using their IDs
        final Deck SELECTED_DECK = DeckConfigReader.getInstance().getDeck(subject.getDeck());
        final Grip SELECTED_GRIP = GripConfigReader.getInstance().getGrip(subject.getGrip());
        final Design SELECTED_DESIGN = DesignConfigReader.getInstance().getDesign(
                subject.getDesign());
        final Truck SELECTED_TRUCK = TruckConfigReader.getInstance().getTruck(subject.getTrucks());
        final Wheel SELECTED_WHEEL = WheelConfigReader.getInstance().getWheel(subject.getWheels());
        final Bearing SELECTED_BEARING = BearingConfigReader.getInstance().getBearing(
                subject.getBearings());

        // Get the position for the trucks
        truckOnePos = SELECTED_DECK.truckOneRelativePosition;
        truckTwoPos = SELECTED_DECK.truckTwoRelativePosition;

        // Work out the position for each wheel
        wheelRelativePos = SELECTED_TRUCK.wheelRelativePosition;
        wheelOneRealPos = new Point3D(truckOnePos.x + wheelRelativePos.x,
                truckOnePos.y + wheelRelativePos.y,
                truckOnePos.z + wheelRelativePos.z);
        wheelTwoRealPos = new Point3D(truckOnePos.x - wheelRelativePos.x,
                truckOnePos.y + wheelRelativePos.y,
                truckOnePos.z + wheelRelativePos.z);
        wheelThreeRealPos = new Point3D(truckTwoPos.x + wheelRelativePos.x,
                truckTwoPos.y + wheelRelativePos.y,
                truckTwoPos.z - wheelRelativePos.z);
        wheelFourRealPos = new Point3D(truckTwoPos.x - wheelRelativePos.x,
                truckTwoPos.y + wheelRelativePos.y,
                truckTwoPos.z - wheelRelativePos.z);

        // Work out the position for each bearing
        bearingOuterRelativePos = SELECTED_TRUCK.bearingOneRelativePosition;
        bearingInnerRelativePos = SELECTED_TRUCK.bearingTwoRelativePosition;
        bearingOneRealPos = new Point3D(truckOnePos.x + bearingOuterRelativePos.x,
                truckOnePos.y + bearingOuterRelativePos.y,
                truckOnePos.z + bearingOuterRelativePos.z);
        bearingTwoRealPos = new Point3D(truckOnePos.x + bearingInnerRelativePos.x,
                truckOnePos.y + bearingInnerRelativePos.y,
                truckOnePos.z + bearingInnerRelativePos.z);
        bearingThreeRealPos = new Point3D(truckOnePos.x - bearingOuterRelativePos.x,
                truckOnePos.y + bearingOuterRelativePos.y,
                truckOnePos.z + bearingOuterRelativePos.z);
        bearingFourRealPos = new Point3D(truckOnePos.x - bearingInnerRelativePos.x,
                truckOnePos.y + bearingInnerRelativePos.y,
                truckOnePos.z + bearingInnerRelativePos.z);
        bearingFiveRealPos = new Point3D(truckTwoPos.x + bearingOuterRelativePos.x,
                truckTwoPos.y + bearingOuterRelativePos.y,
                truckTwoPos.z - bearingOuterRelativePos.z);
        bearingSixRealPos = new Point3D(truckTwoPos.x + bearingInnerRelativePos.x,
                truckTwoPos.y + bearingInnerRelativePos.y,
                truckTwoPos.z - bearingInnerRelativePos.z);
        bearingSevenRealPos = new Point3D(truckTwoPos.x - bearingOuterRelativePos.x,
                truckTwoPos.y + bearingOuterRelativePos.y,
                truckTwoPos.z - bearingOuterRelativePos.z);
        bearingEightRealPos = new Point3D(truckTwoPos.x - bearingInnerRelativePos.x,
                truckTwoPos.y + bearingInnerRelativePos.y,
                truckTwoPos.z - bearingInnerRelativePos.z);

        // Load all of the components
        loadDeck(SELECTED_DECK, SELECTED_DESIGN);
        loadGrip(SELECTED_DECK, SELECTED_GRIP);
        loadTrucks(SELECTED_TRUCK);
        loadWheels(SELECTED_WHEEL);
        loadBearings(SELECTED_BEARING);

        // Create touch rotation to calculate the rotation of the models from user touch
        touchRotation = new TouchRotation(-48f, 60.0f);

        // Create, position and setup the user interface
        initUI();
    }

    /**
     * Update function overridden from the Scene parent class. The update function should contain
     * the logic in the scene that needs to be updated frequently. In this function the models are
     * positioned and rotated according to the touch rotation object.
     *
     * @param deltaTime Timing value used to update logic based on time passed instead of update
     *                  frequency
     * @see             Scene
     * @since           1.0
     */
    @Override
    public void update(float deltaTime)
    {
        touchRotation.update(deltaTime);

        // Update the deck model if it exists
        if(deck != null)
        {
            deck.update(touchRotation);
        }

        // Update the grip model if it exists
        if(grip != null)
        {
            grip.update(touchRotation);
        }

        // Update the truck models if they exist
        if(truckOne != null && truckTwo != null)
        {
            // Update the trucks
            truckOne.update(touchRotation);
            truckTwo.update(touchRotation);
        }

        // Update all the wheels
        for(int i = 0; i < wheelModels.size(); i++)
        {
            wheelModels.get(i).update(touchRotation);
        }

        // Update all the bearings
        for(int i = 0; i < bearingModels.size(); i++)
        {
            bearingModels.get(i).update(touchRotation);
        }

        // Update the custom button UI
        backButton.update(deltaTime);
        editNameButton.update(deltaTime);

        // Update the fade transition object
        fadeTransition.update(deltaTime);
    }

    /**
     * Render function overridden from the Scene parent class. The render function should contain
     * the draw calls. In this function all of the 3D models are rendered
     *
     * @see     Scene
     * @since   1.0
     */
    @Override
    public void render()
    {
        if(deck != null)
        {
            deck.render(modelCamera);
        }

        if(grip != null)
        {
            grip.render(modelCamera);
        }

        if(truckOne != null && truckTwo != null)
        {
            // Render the trucks
            truckOne.render(modelCamera);
            truckTwo.render(modelCamera);
        }

        // Render all the wheels
        for(int i = 0; i < wheelModels.size(); i++)
        {
            wheelModels.get(i).render(modelCamera);
        }

        // Render all the bearings
        for(int i = 0; i < bearingModels.size(); i++)
        {
            bearingModels.get(i).render(modelCamera);
        }

        // Draw the user interface
        titleText.draw(uiCamera);
        saveButton.draw(uiCamera);
        backButton.draw(uiCamera);
        editNameButton.draw(uiCamera);
        loadingIcon.draw(uiCamera);
        fadeTransition.draw(uiCamera);
    }

    /**
     * Touch function overridden from the Scene parent class. The touch function allows you to
     * intercept user touch input and process it. The touch control on this page allows the user to
     * control the rotation of the model view.
     *
     * @param type      The type of touch event (e.g. click, release or drag)
     * @param position  The position of the touch event (x, y)
     * @see             Scene
     * @since           1.0
     */
    @Override
    public void touch(int type, Point2D position)
    {
        touchRotation.touch(type, position);
    }

    /**
     * Initialise, position and set-up all of the user interface on the page.
     *
     * @since   1.0
     */
    private void initUI()
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
        backButton.setPosition(Constants.getBackButtonPosition());
        backButton.setSize(Constants.BACK_BUTTON_SIZE);
        backButton.addTouchListener(e ->
        {
            switch (e.getEvent())
            {
                case RELEASE:
                    fadeTransition.fadeOutToScence(SelectBearingsScene::new);
                    break;
            }
        });

        saveButton = new Button(aileronRegularFont, "Save");
        saveButton.setSize(Constants.NEXT_BUTTON_SIZE);
        saveButton.setPosition(Constants.getNextButtonPosition());
        saveButton.setColour(Constants.BACKGROUND_COLOR);
        saveButton.setBorder(new Border(Colour.WHITE, 8));
        saveButton.setTextColour(Colour.WHITE);
        saveButton.addTouchListener(e ->
        {
            switch (e.getEvent())
            {
                case RELEASE:
                    if(!nameGiven)
                    {
                        enterName(false);
                    }

                    if(nameGiven)
                    {
                        subject.setName(name);

                        boolean nameExists = false;

                        // Check if the name already exists
                        for(int i = 0; i < saves.size(); i++)
                        {
                            if(saves.get(i).getName().compareTo(subject.getName()) == 0)
                            {
                                nameExists = true;
                                break;
                            }
                        }

                        if(nameExists)
                        {
                            enterName(true);
                        }
                        else
                        {
                            saves.add(subject);
                            SaveManager.writeSave(saves);
                            fadeTransition.fadeOutToScence(HomeScene::new);
                        }
                    }
                    break;
            }
        });

        titleText = new Text(aileronRegularFont, subject.getName(), false,
                true, Crispin.getSurfaceWidth());

        final Point2D TITLE_TEXT_POSITION = new Point2D(0.0f, Constants.getBackButtonPosition().y -
                Constants.BACK_BUTTON_PADDING.y - titleText.getHeight());

        titleText.setColour(Colour.WHITE);
        titleText.setPosition(TITLE_TEXT_POSITION);

        editNameButton = new CustomButton(R.drawable.edit_icon);
        Scale2D size = new Scale2D(120.0f, 120.0f);
        Point2D padding = new Point2D(0.0f, 50.0f);
        editNameButton.setPosition((Crispin.getSurfaceWidth() / 2.0f) - (size.x / 2.0f),
                titleText.getPosition().y - size.y - padding.y);
        editNameButton.setSize(size);
        editNameButton.addTouchListener(new TouchListener()
        {
            @Override
            public void touchEvent(TouchEvent e)
            {
                switch (e.getEvent())
                {
                    case RELEASE:
                        enterName(false);
                }
            }
        });
    }

    /**
     * Presents a dialogue with a text box that allows the user to enter a name for their design.
     *
     * @since   1.0
     */
    private void enterName(boolean nameExists)
    {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(Crispin.getActivity());

        if(nameExists)
        {
            alertBuilder.setTitle("A save with that name already exists, please enter a different one");
        }
        else
        {
            alertBuilder.setTitle("Enter a name for your design");
        }

        EditText textInput = new EditText(Crispin.getActivity());
        textInput.setInputType(InputType.TYPE_CLASS_TEXT);

        // Limit the input to only letters and numbers (also exclamation and question mark)
        textInput.setFilters(new InputFilter[] {
                (src, start, end, dst, dstart, dend) ->
                {
                    // Allow user to enter backspace
                    if(src.equals(""))
                    {
                        return src;
                    }

                    // Limit the text to letters and number
                    if(src.toString().matches("[a-zA-Z 0-9!?]+"))
                    {
                        return src;
                    }

                    return "";
                }
        });
        alertBuilder.setView(textInput);

        alertBuilder.setPositiveButton("Ok", (dialog, which) ->
        {
            name = textInput.getText().toString();
            nameGiven = true;
            titleText.setText(name);
        });

        alertBuilder.setNegativeButton("Cancel", (dialog, which) ->
        {
            dialog.cancel();
        });

        alertBuilder.show();
    }

    /**
     * Loads the deck model and applies a material to it. The deck model that it loads depends on
     * the ID contained in the deck parameter. The material that is applied depends on the resource
     * ID of the design parameter.
     *
     * @param deck      The deck data which contains the ID of the model to load
     * @param design    The design data which contains the resource ID of the texture to load
     * @since           1.0
     */
    private void loadDeck(Deck deck, Design design)
    {
        // Load the model on another thread
        ThreadedOBJLoader.loadModel(deck.modelId, model ->
        {
            // Apply the design to the deck model by loading its texture
            model.setMaterial(new Material(design.resourceId));

            // Create a new component model using the loaded model
            this.deck = new ComponentModel(model, new Point3D(), 0.0f, 0.0f,
                    DECK_SCALE);
        });
    }

    /**
     * Loads the grip model and applies a material to it. The grip model that it loads depends on
     * the griptape ID contained in the deck parameter. The material that is applied depends on the
     * resource ID of the grip parameter.
     *
     * @param deck  The deck data which contains the grip ID of the model to load
     * @param grip  The grip data which contains the resource ID of the texture to load
     * @since       1.0
     */
    private void loadGrip(Deck deck, Grip grip)
    {
        // Load the model on another thread
        ThreadedOBJLoader.loadModel(deck.gripModelId, model ->
        {
            // Apply the design to the griptape model by loading its texture
            model.setMaterial(new Material(grip.resourceId));

            // Create a new component model using the loaded model
            this.grip = new ComponentModel(model, new Point3D(), 0.0f, 0.0f,
                    GRIP_SCALE);
        });
    }

    /**
     * Loads the truck model and applies a material to it. The truck model that it loads depends on
     * the truck ID contained in the truck parameter. The material that is applied depends on the
     * resource ID also contained in the truck parameter.
     *
     * @param truck The truck data which contains the truck model ID and texture resource ID
     * @since       1.0
     */
    private void loadTrucks(Truck truck)
    {
        // Load the model on another thread
        ThreadedOBJLoader.loadModel(truck.modelResourceId, model ->
        {
            // Apply the design to the truck model by loading its texture
            model.setMaterial(new Material(truck.resourceId));

            // Create a new component models using the loaded model
            truckOne = new ComponentModel(model, truckOnePos, 0.0f, 0.0f,
                    TRUCK_SCALE);
            truckTwo = new ComponentModel(model, truckTwoPos, 180.0f, 0.0f,
                    TRUCK_SCALE);
        });
    }

    /**
     * Loads the wheel model and applies a material to it. The wheel model that it loads is
     * contained in the 'raw' resources folder. This is because the application does not cater for
     * differently shapes wheels as this is very uncommon in skateboarding. The material that is
     * applied depends on the resource ID contained in the wheel parameter.
     *
     * @param wheel The wheel data which contains the wheel texture resource ID
     * @since       1.0
     */
    private void loadWheels(Wheel wheel)
    {
        // Create the wheel model array list
        wheelModels = new ArrayList<>();

        // Load the model on another thread
        ThreadedOBJLoader.loadModel(R.raw.wheels, model ->
        {
            // Apply the design to the wheel model by loading its texture
            model.setMaterial(new Material(wheel.resourceId));

            // Create a new component models using the loaded model
            wheelModels.add(new ComponentModel(model, wheelOneRealPos, 0.0f,
                    90.0f, WHEEL_SCALE));
            wheelModels.add(new ComponentModel(model, wheelTwoRealPos, 0.0f,
                    270.0f, WHEEL_SCALE));
            wheelModels.add(new ComponentModel(model, wheelThreeRealPos, 180.0f,
                    270.0f, WHEEL_SCALE));
            wheelModels.add(new ComponentModel(model, wheelFourRealPos, 180.0f,
                    90.0f, WHEEL_SCALE));
        });
    }

    /**
     * Loads the bearing model and applies a material to it. The bearing model that it loads is
     * contained in the 'raw' resources folder. This is because the application does not cater for
     * differently shapes bearings as skateboard bearings come in only one shape. The material that
     * is applied depends on the resource ID contained in the bearing parameter.
     *
     * @param bearing   The bearing data which contains the bearing texture resource ID
     * @since           1.0
     */
    private void loadBearings(Bearing bearing)
    {
        // Create the bearing model array list
        bearingModels = new ArrayList<>();

        // Load the model on another thread
        ThreadedOBJLoader.loadModel(R.raw.bearings, model ->
        {
            // Apply the design to the bearing model by loading its texture
            model.setMaterial(new Material(bearing.resourceId));

            // Create a new component models using the loaded model
            bearingModels.add(new ComponentModel(model, bearingOneRealPos, 0.0f,
                    90.0f, BEARING_SCALE));
            bearingModels.add(new ComponentModel(model, bearingTwoRealPos, 0.0f,
                    270.0f, BEARING_SCALE));
            bearingModels.add(new ComponentModel(model, bearingThreeRealPos, 0.0f,
                    90.0f, BEARING_SCALE));
            bearingModels.add(new ComponentModel(model, bearingFourRealPos, 0.0f,
                    270.0f, BEARING_SCALE));
            bearingModels.add(new ComponentModel(model, bearingFiveRealPos, 180.0f,
                    90.0f, BEARING_SCALE));
            bearingModels.add(new ComponentModel(model, bearingSixRealPos, 180.0f,
                    270.0f, BEARING_SCALE));
            bearingModels.add(new ComponentModel(model, bearingSevenRealPos, 180.0f,
                    90.0f, BEARING_SCALE));
            bearingModels.add(new ComponentModel(model, bearingEightRealPos, 180.0f,
                    270.0f, BEARING_SCALE));
        });
    }
}