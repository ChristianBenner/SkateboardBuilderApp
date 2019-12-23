package com.games.crispin.skateboardbuilderapp;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera3D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Material;
import com.games.crispin.crispinmobile.Rendering.Utilities.Model;
import com.games.crispin.crispinmobile.Rendering.Utilities.ModelMatrix;
import com.games.crispin.crispinmobile.Rendering.Utilities.Texture;
import com.games.crispin.crispinmobile.UserInterface.Button;
import com.games.crispin.crispinmobile.UserInterface.Dropdown;
import com.games.crispin.crispinmobile.UserInterface.Text;
import com.games.crispin.crispinmobile.Utilities.Scene;

public class SelectDeckDesignScene extends Scene
{
    private static final Material BOARD_PALACE_DECK = new Material(new Texture(R.drawable.palacedeck));
    private static final Material BOARD_REAL_WAIR_FLOODED = new Material(new Texture(R.drawable.real_wair_flooded));

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

    public SelectDeckDesignScene()
    {
// Set the background to a blue colour
        Crispin.setBackgroundColour(HomeScene.BACKGROUND_COLOR);

        // Create the user interface camera
        uiCamera = new Camera2D(0, 0, Crispin.getSurfaceWidth(), Crispin.getSurfaceHeight());

        // Create the model camera and move it forward in-front of the origin
        modelCamera = new Camera3D();
        modelCamera.setPosition(new Point3D(0.0f, 0.0f, 7.0f));

        // Create the model matrix for transforming the model
        modelMatrix = new ModelMatrix();

        modelScale = 1.0f;

        touchRotation = new TouchRotation();


        // Create the fade transition object and set it to fade in
        fadeTransition = new FadeTransition();
        fadeTransition.setFadeColour(HomeScene.BACKGROUND_COLOR);
        fadeTransition.fadeIn();

        // Create the loading icon
        loadingIcon = new LoadingIcon();

        // Create the back button
        backButton = new CustomButton(R.drawable.back_icon);
        nextButton = new Button(AILERON_REGULAR_FONT, "Next");
        titleText = new Text(AILERON_REGULAR_FONT, SCENE_TITLE_TEXT, false,
                true, Crispin.getSurfaceWidth());
    }

    @Override
    public void update(float deltaTime)
    {

    }

    @Override
    public void render()
    {


    }

    @Override
    public void touch(int type, Point2D position)
    {

    }
}
