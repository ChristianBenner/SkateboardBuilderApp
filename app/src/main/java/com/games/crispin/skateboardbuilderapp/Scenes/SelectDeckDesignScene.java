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
import com.games.crispin.crispinmobile.Rendering.Utilities.Texture;
import com.games.crispin.crispinmobile.UserInterface.Border;
import com.games.crispin.crispinmobile.UserInterface.Button;
import com.games.crispin.crispinmobile.UserInterface.Text;
import com.games.crispin.crispinmobile.UserInterface.TouchEvent;
import com.games.crispin.crispinmobile.Utilities.Logger;
import com.games.crispin.crispinmobile.Utilities.OBJModelLoader;
import com.games.crispin.crispinmobile.Utilities.Scene;
import com.games.crispin.crispinmobile.Utilities.ThreadedOBJLoader;
import com.games.crispin.skateboardbuilderapp.ConfigReaders.DeckConfigReader;
import com.games.crispin.skateboardbuilderapp.ConfigReaders.DesignConfigReader;
import com.games.crispin.skateboardbuilderapp.ConfigReaders.SaveManager;
import com.games.crispin.skateboardbuilderapp.Constants;
import com.games.crispin.skateboardbuilderapp.CustomButton;
import com.games.crispin.skateboardbuilderapp.FadeTransition;
import com.games.crispin.skateboardbuilderapp.LoadingIcon;
import com.games.crispin.skateboardbuilderapp.R;
import com.games.crispin.skateboardbuilderapp.SkateboardComponents.Deck;
import com.games.crispin.skateboardbuilderapp.SkateboardComponents.Design;
import com.games.crispin.skateboardbuilderapp.SkateboardComponents.Skateboard;
import com.games.crispin.skateboardbuilderapp.TouchRotation;

import java.util.ArrayList;
import java.util.List;

public class SelectDeckDesignScene extends Scene
{
    private static final String TAG = "SelectDeckDesignScene";

    // Position of the back button
    private static final Point2D BACK_BUTTON_POSITION = new Point2D(Constants.BACK_BUTTON_PADDING.x,
            Crispin.getSurfaceHeight() - Constants.BACK_BUTTON_PADDING.y -
                    Constants.BACK_BUTTON_SIZE.y);

    // Next button position
    private static final Point2D NEXT_BUTTON_POSITION = new Point2D((Crispin.getSurfaceWidth() /
            2.0f) - (Constants.NEXT_BUTTON_SIZE.x / 2.0f) + Constants.NEXT_BUTTON_PADDING.x,
            Constants.NEXT_BUTTON_PADDING.y);

    // Camera for 2D/user interface rendering
    private Camera2D uiCamera;

    // Camera for 3D model rendering
    private Camera3D modelCamera;

    // Render object model
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

    private int designIndex;

    // The skateboard that is being worked on
    private Skateboard subject;

    private Material materialNoDesign;

    // Materials stored by design ID
    private List<Material> materials;

    private List<Design> designs;

    public SelectDeckDesignScene()
    {
        materialNoDesign = new Material(R.drawable.grey);

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
            Logger.info("Design scene started with deck ID[" + subject.getDeck() + "]");
        }

        // Get the designs compatible with the subjects deck size
        designs = DesignConfigReader.getInstance().getDesigns(subject.getDeck());

        materials = new ArrayList<>();

        // Load the materials for the compatible designs
        for(Design design : designs)
        {
            materials.add(new Material(design.resourceId));
        }

        // Set the background to a blue colour
        Crispin.setBackgroundColour(Constants.BACKGROUND_COLOR);

        // Create the user interface camera
        uiCamera = new Camera2D(0, 0, Crispin.getSurfaceWidth(), Crispin.getSurfaceHeight());

        // Create the model camera and move it forward in-front of the origin
        modelCamera = new Camera3D();
        modelCamera.setPosition(new Point3D(0.0f, 0.0f, 7.0f));

        // Create the model matrix for transforming the model
        modelMatrix = new ModelMatrix();

        touchRotation = new TouchRotation();


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

        titleText = new Text(aileronRegularFont, "Select Design", false,
                true, Crispin.getSurfaceWidth());

        leftButton = new CustomButton(R.drawable.arrow_left);
        rightButton = new CustomButton(R.drawable.arrow_right);





        // Read all the designs from the config file and then create materials from them
        DesignConfigReader designConfigReader = DesignConfigReader.getInstance();
        designConfigReader.printInfo();

        List<Design> tempDesigns = designConfigReader.getDesigns();

        DeckConfigReader deckConfigReader = DeckConfigReader.getInstance();
        Deck selectedDeck = deckConfigReader.getDeck(subject.getDeck());

        for(Design design : tempDesigns)
        {
            // Only add the design if it is compatible with the deck
            if(design.deckId == selectedDeck.id)
            {
                designs.add(design);
            }
        }

        designIndex = 0;

        if(subject != null)
        {
            int id = selectedDeck.modelId;
            String name = selectedDeck.name;
            System.out.println("Loading deck id: " + id + ", name: " + name);
            ThreadedOBJLoader.loadModel(id, model ->
            {
                this.model = model;
                this.model.setMaterial(nextDesign());
            });
        }
        else
        {
            System.err.println("ERROR!");
        }

        touchRotation = new TouchRotation();
        modelMatrix = new ModelMatrix();

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

    private Material nextDesign()
    {
        if(designs.isEmpty())
        {
            Logger.error("SelectDeckDesignScene",
                    "There are no designs for the selected board");
            return materialNoDesign;
        }

        designIndex++;
        if(designIndex >= materials.size())
        {
            designIndex = 0;
        }

        return materials.get(designIndex);
    }

    private Material previousDesign()
    {
        if(materials.isEmpty())
        {
            Logger.error("SelectDeckDesignScene",
                    "There are no designs for the selected board");
            return materialNoDesign;
        }

        designIndex--;
        if(designIndex < 0)
        {
            designIndex = materials.size() - 1;
        }

        return materials.get(designIndex);
    }

    private void setupUI()
    {
        backButton.setPosition(BACK_BUTTON_POSITION);
        backButton.setSize(Constants.BACK_BUTTON_SIZE);

        nextButton.setSize(Constants.NEXT_BUTTON_SIZE);
        nextButton.setPosition(NEXT_BUTTON_POSITION);
        nextButton.setColour(Constants.BACKGROUND_COLOR);
        nextButton.setBorder(new Border(Colour.WHITE, 8));
        nextButton.setTextColour(Colour.WHITE);

        leftButton.setSize(240.0f, 240.0f);
        leftButton.setPosition(20.0f, (Crispin.getSurfaceHeight() / 2.0f) -
                (leftButton.getSize().y / 2.0f));
        leftButton.addTouchListener(e ->
        {
            if(e.getEvent() == TouchEvent.Event.RELEASE)
            {
                model.setMaterial(previousDesign());
            }
        });


        rightButton.setSize(240.0f, 240.0f);
        rightButton.setPosition(Crispin.getSurfaceWidth() - 20.0f -rightButton.getSize().x,
                (Crispin.getSurfaceHeight() / 2.0f) - (rightButton.getSize().y / 2.0f));
        rightButton.addTouchListener(e ->
        {
            if(e.getEvent() == TouchEvent.Event.RELEASE)
            {
                model.setMaterial(nextDesign());
            }
        });

        final Point2D TITLE_TEXT_POSITION = new Point2D(0.0f, BACK_BUTTON_POSITION.y -
                Constants.BACK_BUTTON_PADDING.y - titleText.getHeight());

        titleText.setColour(Colour.WHITE);
        titleText.setPosition(TITLE_TEXT_POSITION);

        // Add touch listener to back button
        backButton.addTouchListener(e ->
        {
            switch (e.getEvent())
            {
                case RELEASE:
                    fadeTransition.fadeOutToScence(SelectDeckWidthScene::new);
                    break;
            }
        });

        // Add touch listener to next button
        nextButton.addTouchListener(e ->
        {
            switch (e.getEvent())
            {
                case RELEASE:
                    subject.setDesign(designs.get(designIndex).id);

                    // Save the current skateboard
                    SaveManager.writeCurrentSave(subject);

                    fadeTransition.fadeOutToScence(SelectGripScene::new);
                    break;
            }
        });
    }
}
