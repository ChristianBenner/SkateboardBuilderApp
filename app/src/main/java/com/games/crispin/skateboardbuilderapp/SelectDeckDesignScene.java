package com.games.crispin.skateboardbuilderapp;

import android.content.Context;
import android.util.Xml;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Geometry.Scale2D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Rendering.Models.Cube;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera3D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Font;
import com.games.crispin.crispinmobile.Rendering.Utilities.Material;
import com.games.crispin.crispinmobile.Rendering.Utilities.Model;
import com.games.crispin.crispinmobile.Rendering.Utilities.ModelMatrix;
import com.games.crispin.crispinmobile.Rendering.Utilities.Texture;
import com.games.crispin.crispinmobile.UserInterface.Border;
import com.games.crispin.crispinmobile.UserInterface.Button;
import com.games.crispin.crispinmobile.UserInterface.Dropdown;
import com.games.crispin.crispinmobile.UserInterface.Text;
import com.games.crispin.crispinmobile.UserInterface.TouchEvent;
import com.games.crispin.crispinmobile.UserInterface.TouchListener;
import com.games.crispin.crispinmobile.Utilities.LoadListener;
import com.games.crispin.crispinmobile.Utilities.Scene;
import com.games.crispin.crispinmobile.Utilities.ThreadedOBJLoader;

import org.xmlpull.v1.XmlSerializer;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SelectDeckDesignScene extends Scene
{
    private static final Material BOARD_PALACE_DECK = new Material(new Texture(R.drawable.palacedeck));
    private static final Material BOARD_REAL_WAIR_FLOODED = new Material(new Texture(R.drawable.real_wair_flooded));

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

    // Width select dropdown UI
    private Dropdown widthSelectDropdown;

    // Select deck width text UI
    private Text titleText;

    private Button leftButton;
    private Button rightButton;

    private ArrayList<Material> materials;
    private int materialIndex;

    private List<Skateboard> skateboards;

    void writeSave()
    {
        try
        {
            FileOutputStream fileOutputStream = Crispin.getApplicationContext().openFileOutput("saves.xml", Context.MODE_PRIVATE);
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fileOutputStream, "UTF-8");
            serializer.startDocument("UTF-8", true);
            serializer.startTag("", "saves");

            // Write skateboard XML
            for(Skateboard s : skateboards)
            {
                serializer.startTag("", "skateboard");
                serializer.attribute("", "deck", Integer.toString(s.getDeck()));
                serializer.attribute("", "grip", Integer.toString(s.getGrip()));
                serializer.attribute("", "trucks", Integer.toString(s.getTrucks()));
                serializer.attribute("", "bearings", Integer.toString(s.getBearings()));
                serializer.attribute("", "wheels", Integer.toString(s.getWheels()));
                serializer.attribute("", "name", s.getName());
                serializer.endTag("", "skateboard");
            }

            serializer.endTag("", "saves");
            serializer.endDocument();
            serializer.flush();
            fileOutputStream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public SelectDeckDesignScene()
    {
        skateboards = new ArrayList<>();
        skateboards.add(new Skateboard("Board1", 1, 1, 1, 1, 1));

        writeSave();

        // Load the config that is storing the currently selected model parts
        skateboards = SaveReader.loadSaves();

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
        titleText = new Text(aileronRegularFont, "Select Deck Design", false,
                true, Crispin.getSurfaceWidth());

        leftButton = new CustomButton(R.drawable.arrow_left);
        rightButton = new CustomButton(R.drawable.arrow_right);

        materials = new ArrayList<>();
        materials.add(new Material(R.drawable.jart_new_wave));
        materials.add(new Material(R.drawable.palacedeck));
        materials.add(new Material(R.drawable.primitive_rodriquez_thorns));
        materials.add(new Material(R.drawable.primitive_x_rick_and_morty));
        materials.add(new Material(R.drawable.real_wair_flooded));

        materialIndex = 0;

        touchRotation = new TouchRotation();
        modelMatrix = new ModelMatrix();

        ThreadedOBJLoader.loadModel(R.raw.deck8_125_uv_test_2, model ->
        {
            this.model = model;
            this.model.setMaterial(nextMaterial());
        });

        setupUI();
    }

    private Material nextMaterial()
    {
        materialIndex++;
        if(materialIndex >= materials.size())
        {
            materialIndex = 0;
        }

        return materials.get(materialIndex);
    }

    private Material previousMaterial()
    {
        materialIndex--;
        if(materialIndex < 0)
        {
            materialIndex = materials.size() - 1;
        }

        return materials.get(materialIndex);
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

        model.render(modelCamera, modelMatrix);

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

    private void setupUI()
    {
        backButton.setPosition(BACK_BUTTON_POSITION);
        backButton.setSize(BACK_BUTTON_SIZE);

        nextButton.setSize(NEXT_BUTTON_SIZE);
        nextButton.setPosition(NEXT_BUTTON_POSITION);
        nextButton.setColour(HomeScene.BACKGROUND_COLOR);
        nextButton.setBorder(new Border(Colour.WHITE, 8));
        nextButton.setTextColour(Colour.WHITE);
        nextButton.setEnabled(false);

        leftButton.setSize(240.0f, 240.0f);
        leftButton.setPosition(20.0f, (Crispin.getSurfaceHeight() / 2.0f) -
                (leftButton.getSize().y / 2.0f));
        leftButton.addTouchListener(e ->
        {
            if(e.getEvent() == TouchEvent.Event.RELEASE)
            {
                model.setMaterial(previousMaterial());
            }
        });


        rightButton.setSize(240.0f, 240.0f);
        rightButton.setPosition(Crispin.getSurfaceWidth() - 20.0f -rightButton.getSize().x,
                (Crispin.getSurfaceHeight() / 2.0f) - (rightButton.getSize().y / 2.0f));
        rightButton.addTouchListener(e ->
        {
            if(e.getEvent() == TouchEvent.Event.RELEASE)
            {
                model.setMaterial(nextMaterial());
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
                    fadeTransition.fadeOutToScence(HomeScene::new);
                    break;
            }
        });
    }
}
