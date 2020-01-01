package com.games.crispin.skateboardbuilderapp.Scenes;

import android.opengl.Matrix;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Point3D;
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
import com.games.crispin.crispinmobile.Utilities.LoadListener;
import com.games.crispin.crispinmobile.Utilities.Logger;
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
import com.games.crispin.skateboardbuilderapp.CustomButton;
import com.games.crispin.skateboardbuilderapp.FadeTransition;
import com.games.crispin.skateboardbuilderapp.LoadingIcon;
import com.games.crispin.skateboardbuilderapp.R;
import com.games.crispin.skateboardbuilderapp.SkateboardComponents.Skateboard;
import com.games.crispin.skateboardbuilderapp.SkateboardComponents.Wheel;
import com.games.crispin.skateboardbuilderapp.TouchRotation;

import java.util.ArrayList;
import java.util.List;

public class ViewBoardScene extends Scene
{
    // Tag used for logging
    private static final String TAG = "ViewBoardScene";

    // Camera for 2D/user interface rendering
    private Camera2D uiCamera;

    // Camera for 3D model rendering
    private Camera3D modelCamera;

    private Model deck;
    private Model grip;
    private Model truck;
    private Model bearing;
    private Model wheel;

    // Touch rotation object calculates the rotation of the interactable model
    private TouchRotation touchRotation;

    // Transition object that allows us to fade the scene in and out
    private FadeTransition fadeTransition;

    // The loading icon UI
    private LoadingIcon loadingIcon;

    // Back button UI (return to home screen)
    private CustomButton backButton;

    // Next button UI
    private Button buyButton;

    // Select deck width text UI
    private Text titleText;

    // The skateboard that is being worked on
    private Skateboard subject;

    private ModelMatrix deckAndGripModelMatrix;
    private ModelMatrix truckOneModelMatrix;
    private ModelMatrix truckTwoModelMatrix;
    private ModelMatrix bearingOneModelMatrix;
    private ModelMatrix bearingTwoModelMatrix;
    private ModelMatrix bearingThreeModelMatrix;
    private ModelMatrix bearingFourModelMatrix;
    private ModelMatrix bearingFiveModelMatrix;
    private ModelMatrix bearingSixModelMatrix;
    private ModelMatrix bearingSevenModelMatrix;
    private ModelMatrix bearingEightModelMatrix;
    private ModelMatrix wheelOneModelMatrix;
    private ModelMatrix wheelTwoModelMatrix;
    private ModelMatrix wheelThreeModelMatrix;
    private ModelMatrix wheelFourModelMatrix;

    // Relative positions
    private Point3D truckOnePos;            // Relative to deck
    private Point3D truckTwoPos;            // Relative to deck
    private Point3D wheelRelativePos;       // Relative to trucks
    private Point3D bearingOneRelativePos;  // Relative to trucks
    private Point3D bearingTwoRelativePos;  // Relative to trucks

    // Real positions of components
    private Point3D wheelOneRealPos;
    private Point3D wheelTwoRealPos;
    private Point3D wheelThreeRealPos;
    private Point3D wheelFourRealPos;
    private Point3D bearingOneRealPos;
    private Point3D bearingTwoRealPos;
    private Point3D bearingThreeRealPos;
    private Point3D bearingFourRealPos;
    private Point3D bearingFiveRealPos;
    private Point3D bearingSixRealPos;
    private Point3D bearingSevenRealPos;
    private Point3D bearingEightRealPos;

    // Scales
    private static final float truckScale = 0.07f;
    private static final float wheelScale = 1.157f;
    private static final float bearingScale = 3.040f;
    private static final float wheelScaleAfter = wheelScale * truckScale;
    private static final float bearingScaleAfter = bearingScale * truckScale;

    public ViewBoardScene()
    {
        // Try to load the skateboard that is currently being worked on
        subject = SaveManager.loadCurrentSave();

        // If there is no subject, create one and print an error
        if(subject == null)
        {
            subject = new Skateboard();
            Logger.error(TAG, "ERROR: Failed to load subject skateboard");
        }

        truckOnePos = DeckConfigReader.getInstance().getDeck(subject.getDeck()).truckOneRelativePosition;
        truckTwoPos = DeckConfigReader.getInstance().getDeck(subject.getDeck()).truckTwoRelativePosition;
        wheelRelativePos = TruckConfigReader.getInstance().getTruck(subject.getTrucks()).wheelRelativePosition;
        bearingOneRelativePos = TruckConfigReader.getInstance().getTruck(subject.getTrucks()).bearingOneRelativePosition;
        bearingTwoRelativePos = TruckConfigReader.getInstance().getTruck(subject.getTrucks()).bearingTwoRelativePosition;

        // Real positions of components
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
        bearingOneRealPos = new Point3D(truckOnePos.x + bearingOneRelativePos.x,
                truckOnePos.y + bearingOneRelativePos.y,
                truckOnePos.z + bearingOneRelativePos.z);
        bearingTwoRealPos = new Point3D(truckOnePos.x + bearingTwoRelativePos.x,
                truckOnePos.y + bearingTwoRelativePos.y,
                truckOnePos.z + bearingTwoRelativePos.z);
        bearingThreeRealPos = new Point3D(truckOnePos.x - bearingOneRelativePos.x,
                truckOnePos.y + bearingOneRelativePos.y,
                truckOnePos.z + bearingOneRelativePos.z);
        bearingFourRealPos = new Point3D(truckOnePos.x - bearingTwoRelativePos.x,
                truckOnePos.y + bearingTwoRelativePos.y,
                truckOnePos.z + bearingTwoRelativePos.z);

        bearingFiveRealPos = new Point3D(truckTwoPos.x + bearingOneRelativePos.x,
                truckTwoPos.y + bearingOneRelativePos.y,
                truckTwoPos.z - bearingOneRelativePos.z);
        bearingSixRealPos = new Point3D(truckTwoPos.x + bearingTwoRelativePos.x,
                truckTwoPos.y + bearingTwoRelativePos.y,
                truckTwoPos.z - bearingTwoRelativePos.z);
        bearingSevenRealPos = new Point3D(truckTwoPos.x - bearingOneRelativePos.x,
                truckTwoPos.y + bearingOneRelativePos.y,
                truckTwoPos.z - bearingOneRelativePos.z);
        bearingEightRealPos = new Point3D(truckTwoPos.x - bearingTwoRelativePos.x,
                truckTwoPos.y + bearingTwoRelativePos.y,
                truckTwoPos.z - bearingTwoRelativePos.z);


        // Set the background to a blue colour
        Crispin.setBackgroundColour(Constants.BACKGROUND_COLOR);

        // Create the user interface camera
        uiCamera = new Camera2D(0, 0, Crispin.getSurfaceWidth(), Crispin.getSurfaceHeight());

        // Create the model camera and move it forward in-front of the origin
        modelCamera = new Camera3D();
        modelCamera.setPosition(new Point3D(0.0f, 0.0f, 7.0f));
        touchRotation = new TouchRotation();

        int deckModelId = DeckConfigReader.getInstance().getDeck(subject.getDeck()).modelId;
        int gripModelId = DeckConfigReader.getInstance().getDeck(subject.getDeck()).gripModelId;
        int gripTextureId = GripConfigReader.getInstance().getGrip(subject.getGrip()).resourceId;
        int designTextureId = DesignConfigReader.getInstance().getDesign(subject.getDesign()).
                resourceId;
        int truckModelId = TruckConfigReader.getInstance().getTruck(subject.getTrucks()).
                modelResourceId;
        int truckTextureId = TruckConfigReader.getInstance().getTruck(subject.getTrucks()).
                resourceId;
        int bearingModelId = R.raw.bearings;
        int bearingTextureId = BearingConfigReader.getInstance().getBearing(subject.getBearings()).
                resourceId;
        int wheelModelId = R.raw.wheels;
        int wheelTextureId = WheelConfigReader.getInstance().getWheel(subject.getWheels()).
                resourceId;

        ThreadedOBJLoader.loadModel(deckModelId, model ->
        {
            this.deck = model;
            this.deck.setMaterial(new Material(designTextureId));
        });

        ThreadedOBJLoader.loadModel(gripModelId, model ->
        {
            this.grip = model;
            this.grip.setMaterial(new Material(gripTextureId));
        });

        ThreadedOBJLoader.loadModel(truckModelId, model ->
        {
            this.truck = model;
            this.truck.setMaterial(new Material(truckTextureId));
        });

        ThreadedOBJLoader.loadModel(bearingModelId, model ->
        {
            this.bearing = model;
            this.bearing.setMaterial(new Material(bearingTextureId));
        });

        ThreadedOBJLoader.loadModel(wheelModelId, model ->
        {
            this.wheel = model;
            this.wheel.setMaterial(new Material(wheelTextureId));
        });

        deckAndGripModelMatrix = new ModelMatrix();
        truckOneModelMatrix = new ModelMatrix();
        truckTwoModelMatrix = new ModelMatrix();
        bearingOneModelMatrix = new ModelMatrix();
        bearingTwoModelMatrix = new ModelMatrix();
        bearingThreeModelMatrix = new ModelMatrix();
        bearingFourModelMatrix = new ModelMatrix();
        bearingFiveModelMatrix = new ModelMatrix();
        bearingSixModelMatrix = new ModelMatrix();
        bearingSevenModelMatrix = new ModelMatrix();
        bearingEightModelMatrix = new ModelMatrix();
        wheelOneModelMatrix = new ModelMatrix();
        wheelTwoModelMatrix = new ModelMatrix();
        wheelThreeModelMatrix = new ModelMatrix();
        wheelFourModelMatrix = new ModelMatrix();

        // Print the current subject
        subject.print();

        setupUI();
    }

    @Override
    public void update(float deltaTime)
    {
        touchRotation.update(deltaTime);

        float xRot = touchRotation.getRotationX();
        float yRot = touchRotation.getRotationY();

        deckAndGripModelMatrix.reset();
        deckAndGripModelMatrix.rotate(xRot, 0.0f, 1.0f, 0.0f);
        deckAndGripModelMatrix.rotate(yRot, 1.0f, 0.0f, 0.0f);
        deckAndGripModelMatrix.scale(0.2f);

        truckOneModelMatrix.reset();
        truckOneModelMatrix.translate(truckOnePos.x, truckOnePos.y, truckOnePos.z);
        truckOneModelMatrix.rotateAroundPoint(-truckOnePos.x, -truckOnePos.y, -truckOnePos.z, xRot,
                0.0f, 1.0f, 0.0f);
        truckOneModelMatrix.rotateAroundPoint(-truckOnePos.x, -truckOnePos.y ,-truckOnePos.z, yRot,
                1.0f, 0.0f, 0.0f);
        truckOneModelMatrix.scale(truckScale);

        truckTwoModelMatrix.reset();
        truckTwoModelMatrix.translate(truckTwoPos.x, truckTwoPos.y, truckTwoPos.z);
        truckTwoModelMatrix.rotateAroundPoint(-truckTwoPos.x, -truckTwoPos.y, -truckTwoPos.z, xRot,
                0.0f, 1.0f, 0.0f);
        truckTwoModelMatrix.rotateAroundPoint(-truckTwoPos.x, -truckTwoPos.y, -truckTwoPos.z, yRot,
                1.0f, 0.0f, 0.0f);
        truckTwoModelMatrix.rotate(180.0f, 0.0f, 1.0f, 0.0f);
        truckTwoModelMatrix.scale(truckScale);

        wheelOneModelMatrix.reset();
        wheelOneModelMatrix.translate(wheelOneRealPos.x, wheelOneRealPos.y, wheelOneRealPos.z);
        wheelOneModelMatrix.rotateAroundPoint(new Point3D(-wheelOneRealPos.x, -wheelOneRealPos.y, -wheelOneRealPos.z), xRot,
                0.0f, 1.0f, 0.0f);
        wheelOneModelMatrix.rotateAroundPoint(new Point3D(-wheelOneRealPos.x, -wheelOneRealPos.y, -wheelOneRealPos.z), yRot,
                1.0f, 0.0f, 0.0f);
        wheelOneModelMatrix.rotate(-270.0f, 0.0f, 0.0f, 1.0f);
        wheelOneModelMatrix.scale(wheelScaleAfter);

        wheelTwoModelMatrix.reset();
        wheelTwoModelMatrix.translate(wheelTwoRealPos.x, wheelTwoRealPos.y, wheelTwoRealPos.z);
        wheelTwoModelMatrix.rotateAroundPoint(new Point3D(-wheelTwoRealPos.x, -wheelTwoRealPos.y, -wheelTwoRealPos.z), xRot,
                0.0f, 1.0f, 0.0f);
        wheelTwoModelMatrix.rotateAroundPoint(new Point3D(-wheelTwoRealPos.x, -wheelTwoRealPos.y, -wheelTwoRealPos.z), yRot,
                1.0f, 0.0f, 0.0f);
        wheelTwoModelMatrix.rotate(-90.0f, 0.0f, 0.0f, 1.0f);
        wheelTwoModelMatrix.scale(wheelScaleAfter);

        wheelThreeModelMatrix.reset();
        wheelThreeModelMatrix.translate(wheelThreeRealPos.x, wheelThreeRealPos.y, wheelThreeRealPos.z);
        wheelThreeModelMatrix.rotateAroundPoint(new Point3D(-wheelThreeRealPos.x, -wheelThreeRealPos.y, -wheelThreeRealPos.z), xRot,
                0.0f, 1.0f, 0.0f);
        wheelThreeModelMatrix.rotateAroundPoint(new Point3D(-wheelThreeRealPos.x, -wheelThreeRealPos.y, -wheelThreeRealPos.z), yRot,
                1.0f, 0.0f, 0.0f);
        wheelThreeModelMatrix.rotate(-90.0f, 0.0f, 0.0f, 1.0f);
        wheelThreeModelMatrix.rotate(180.0f, 1.0f, 0.0f, 1.0f);
        wheelThreeModelMatrix.scale(wheelScaleAfter);

        wheelFourModelMatrix.reset();
        wheelFourModelMatrix.translate(wheelFourRealPos.x, wheelFourRealPos.y, wheelFourRealPos.z);
        wheelFourModelMatrix.rotateAroundPoint(new Point3D(-wheelFourRealPos.x, -wheelFourRealPos.y, -wheelFourRealPos.z), xRot,
                0.0f, 1.0f, 0.0f);
        wheelFourModelMatrix.rotateAroundPoint(new Point3D(-wheelFourRealPos.x, -wheelFourRealPos.y, -wheelFourRealPos.z), yRot,
                1.0f, 0.0f, 0.0f);
        wheelFourModelMatrix.rotate(-270.0f, 0.0f, 0.0f, 1.0f);
        wheelFourModelMatrix.rotate(180.0f, 1.0f, 0.0f, 1.0f);
        wheelFourModelMatrix.scale(wheelScaleAfter);

        bearingOneModelMatrix.reset();
        bearingOneModelMatrix.translate(bearingOneRealPos.x, bearingOneRealPos.y, bearingOneRealPos.z);
        bearingOneModelMatrix.rotateAroundPoint(new Point3D(-bearingOneRealPos.x, -bearingOneRealPos.y, -bearingOneRealPos.z), xRot,
                0.0f, 1.0f, 0.0f);
        bearingOneModelMatrix.rotateAroundPoint(new Point3D(-bearingOneRealPos.x, -bearingOneRealPos.y, -bearingOneRealPos.z), yRot,
                1.0f, 0.0f, 0.0f);
        bearingOneModelMatrix.rotate(-270.0f, 0.0f, 0.0f, 1.0f);
        bearingOneModelMatrix.scale(bearingScaleAfter);

        bearingTwoModelMatrix.reset();
        bearingTwoModelMatrix.translate(bearingTwoRealPos.x, bearingTwoRealPos.y, bearingTwoRealPos.z);
        bearingTwoModelMatrix.rotateAroundPoint(new Point3D(-bearingTwoRealPos.x, -bearingTwoRealPos.y, -bearingTwoRealPos.z), xRot,
                0.0f, 1.0f, 0.0f);
        bearingTwoModelMatrix.rotateAroundPoint(new Point3D(-bearingTwoRealPos.x, -bearingTwoRealPos.y, -bearingTwoRealPos.z), yRot,
                1.0f, 0.0f, 0.0f);
        bearingTwoModelMatrix.rotate(-90.0f, 0.0f, 0.0f, 1.0f);
        bearingTwoModelMatrix.scale(bearingScaleAfter);

        bearingThreeModelMatrix.reset();
        bearingThreeModelMatrix.translate(bearingThreeRealPos.x, bearingThreeRealPos.y, bearingThreeRealPos.z);
        bearingThreeModelMatrix.rotateAroundPoint(new Point3D(-bearingThreeRealPos.x, -bearingThreeRealPos.y, -bearingThreeRealPos.z), xRot,
                0.0f, 1.0f, 0.0f);
        bearingThreeModelMatrix.rotateAroundPoint(new Point3D(-bearingThreeRealPos.x, -bearingThreeRealPos.y, -bearingThreeRealPos.z), yRot,
                1.0f, 0.0f, 0.0f);
        bearingThreeModelMatrix.rotate(-270.0f, 0.0f, 0.0f, 1.0f);
        bearingThreeModelMatrix.scale(bearingScaleAfter);

        bearingFourModelMatrix.reset();
        bearingFourModelMatrix.translate(bearingFourRealPos.x, bearingFourRealPos.y, bearingFourRealPos.z);
        bearingFourModelMatrix.rotateAroundPoint(new Point3D(-bearingFourRealPos.x, -bearingFourRealPos.y, -bearingFourRealPos.z), xRot,
                0.0f, 1.0f, 0.0f);
        bearingFourModelMatrix.rotateAroundPoint(new Point3D(-bearingFourRealPos.x, -bearingFourRealPos.y, -bearingFourRealPos.z), yRot,
                1.0f, 0.0f, 0.0f);
        bearingFourModelMatrix.rotate(-90.0f, 0.0f, 0.0f, 1.0f);
        bearingFourModelMatrix.scale(bearingScaleAfter);



        bearingFiveModelMatrix.reset();
        bearingFiveModelMatrix.translate(bearingFiveRealPos.x, bearingFiveRealPos.y, bearingFiveRealPos.z);
        bearingFiveModelMatrix.rotateAroundPoint(new Point3D(-bearingFiveRealPos.x, -bearingFiveRealPos.y, -bearingFiveRealPos.z), xRot,
                0.0f, 1.0f, 0.0f);
        bearingFiveModelMatrix.rotateAroundPoint(new Point3D(-bearingFiveRealPos.x, -bearingFiveRealPos.y, -bearingFiveRealPos.z), yRot,
                1.0f, 0.0f, 0.0f);
        bearingFiveModelMatrix.rotate(-270.0f, 0.0f, 0.0f, 1.0f);
        bearingFiveModelMatrix.rotate(180.0f, 1.0f, 0.0f, 1.0f);
        bearingFiveModelMatrix.scale(bearingScaleAfter);

        bearingSixModelMatrix.reset();
        bearingSixModelMatrix.translate(bearingSixRealPos.x, bearingSixRealPos.y, bearingSixRealPos.z);
        bearingSixModelMatrix.rotateAroundPoint(new Point3D(-bearingSixRealPos.x, -bearingSixRealPos.y, -bearingSixRealPos.z), xRot,
                0.0f, 1.0f, 0.0f);
        bearingSixModelMatrix.rotateAroundPoint(new Point3D(-bearingSixRealPos.x, -bearingSixRealPos.y, -bearingSixRealPos.z), yRot,
                1.0f, 0.0f, 0.0f);
        bearingSixModelMatrix.rotate(-90.0f, 0.0f, 0.0f, 1.0f);
        bearingSixModelMatrix.rotate(180.0f, 1.0f, 0.0f, 1.0f);
        bearingSixModelMatrix.scale(bearingScaleAfter);

        bearingSevenModelMatrix.reset();
        bearingSevenModelMatrix.translate(bearingSevenRealPos.x, bearingSevenRealPos.y, bearingSevenRealPos.z);
        bearingSevenModelMatrix.rotateAroundPoint(new Point3D(-bearingSevenRealPos.x, -bearingSevenRealPos.y, -bearingSevenRealPos.z), xRot,
                0.0f, 1.0f, 0.0f);
        bearingSevenModelMatrix.rotateAroundPoint(new Point3D(-bearingSevenRealPos.x, -bearingSevenRealPos.y, -bearingSevenRealPos.z), yRot,
                1.0f, 0.0f, 0.0f);
        bearingSevenModelMatrix.rotate(-270.0f, 0.0f, 0.0f, 1.0f);
        bearingSevenModelMatrix.rotate(180.0f, 1.0f, 0.0f, 1.0f);
        bearingSevenModelMatrix.scale(bearingScaleAfter);

        bearingEightModelMatrix.reset();
        bearingEightModelMatrix.translate(bearingEightRealPos.x, bearingEightRealPos.y, bearingEightRealPos.z);
        bearingEightModelMatrix.rotateAroundPoint(new Point3D(-bearingEightRealPos.x, -bearingEightRealPos.y, -bearingEightRealPos.z), xRot,
                0.0f, 1.0f, 0.0f);
        bearingEightModelMatrix.rotateAroundPoint(new Point3D(-bearingEightRealPos.x, -bearingEightRealPos.y, -bearingEightRealPos.z), yRot,
                1.0f, 0.0f, 0.0f);
        bearingEightModelMatrix.rotate(-90.0f, 0.0f, 0.0f, 1.0f);
        bearingEightModelMatrix.rotate(180.0f, 1.0f, 0.0f, 1.0f);
        bearingEightModelMatrix.scale(bearingScaleAfter);

        fadeTransition.update(deltaTime);
    }

    @Override
    public void render()
    {
        if(deck != null && grip != null && truck != null && bearing != null && wheel != null)
        {
            deck.render(modelCamera, deckAndGripModelMatrix);
            grip.render(modelCamera, deckAndGripModelMatrix);
            truck.render(modelCamera, truckOneModelMatrix);
            truck.render(modelCamera, truckTwoModelMatrix);
            bearing.render(modelCamera, bearingOneModelMatrix);
            bearing.render(modelCamera, bearingTwoModelMatrix);
            bearing.render(modelCamera, bearingThreeModelMatrix);
            bearing.render(modelCamera, bearingFourModelMatrix);
            bearing.render(modelCamera, bearingFiveModelMatrix);
            bearing.render(modelCamera, bearingSixModelMatrix);
            bearing.render(modelCamera, bearingSevenModelMatrix);
            bearing.render(modelCamera, bearingEightModelMatrix);
            wheel.render(modelCamera, wheelOneModelMatrix);
            wheel.render(modelCamera, wheelTwoModelMatrix);
            wheel.render(modelCamera, wheelThreeModelMatrix);
            wheel.render(modelCamera, wheelFourModelMatrix);
        }

        titleText.draw(uiCamera);
        buyButton.draw(uiCamera);
        backButton.draw(uiCamera);


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
        buyButton = new Button(aileronRegularFont, "Buy");

        titleText = new Text(aileronRegularFont, subject.getName(), false,
                true, Crispin.getSurfaceWidth());

        backButton.setPosition(Constants.getBackButtonPosition());
        backButton.setSize(Constants.BACK_BUTTON_SIZE);

        buyButton.setSize(Constants.NEXT_BUTTON_SIZE);
        buyButton.setPosition(Constants.getNextButtonPosition());
        buyButton.setColour(Constants.BACKGROUND_COLOR);
        buyButton.setBorder(new Border(Colour.WHITE, 8));
        buyButton.setTextColour(Colour.WHITE);

        final Point2D TITLE_TEXT_POSITION = new Point2D(0.0f, Constants.getBackButtonPosition().y -
                Constants.BACK_BUTTON_PADDING.y - titleText.getHeight());

        titleText.setColour(Colour.WHITE);
        titleText.setPosition(TITLE_TEXT_POSITION);

        // Add touch listener to back button
        backButton.addTouchListener(e ->
        {
            switch (e.getEvent())
            {
                case RELEASE:
                    fadeTransition.fadeOutToScence(SelectBearingsScene::new);
                    break;
            }
        });

        // Add touch listener to next button
        buyButton.addTouchListener(e ->
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