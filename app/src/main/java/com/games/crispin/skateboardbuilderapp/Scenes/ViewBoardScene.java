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

        float truckPosY = -0.32f;
        float truckPosZ = -1.65f;

        truckOneModelMatrix.reset();
        truckOneModelMatrix.translate(0.0f, truckPosY, truckPosZ);
        truckOneModelMatrix.rotateAroundPoint(new Point3D(0.0f, -truckPosY, -truckPosZ), xRot,
                0.0f, 1.0f, 0.0f);
        truckOneModelMatrix.rotateAroundPoint(new Point3D(0.0f, -truckPosY, -truckPosZ), yRot,
                1.0f, 0.0f, 0.0f);
        truckOneModelMatrix.scale(0.07f);

        float truckTwoX = 0.0f;
        float truckTwoY = truckPosY;
        float truckTwoZ = 1.85f;

        truckTwoModelMatrix.reset();
        truckTwoModelMatrix.translate(0.0f, truckPosY, truckTwoZ);
        truckTwoModelMatrix.rotateAroundPoint(new Point3D(0.0f, -truckPosY, -truckTwoZ), xRot,
                0.0f, 1.0f, 0.0f);
        truckTwoModelMatrix.rotateAroundPoint(new Point3D(0.0f, -truckPosY, -truckTwoZ), yRot,
                1.0f, 0.0f, 0.0f);
        truckTwoModelMatrix.rotate(180.0f, 0.0f, 1.0f, 0.0f);
        truckTwoModelMatrix.scale(0.07f);

        float bearingScale = 3.040f;
        float wheelScale = 1.157f;
        float wheelScaleAfter = 1.157f * 0.07f;

        float truckOneX = 0.0f;
        float truckOneY = truckPosY;
        float truckOneZ = truckPosZ;

        Point3D wheelOneRelativePos = new Point3D(0.65f, -0.22f, -0.12f);
        Point3D wheelOneRealPos = new Point3D(truckOneX + wheelOneRelativePos.x,
                truckOneY + wheelOneRelativePos.y,
                truckOneZ + wheelOneRelativePos.z);

        wheelOneModelMatrix.reset();
        wheelOneModelMatrix.translate(wheelOneRealPos.x, wheelOneRealPos.y, wheelOneRealPos.z);
        wheelOneModelMatrix.rotateAroundPoint(new Point3D(-wheelOneRealPos.x, -wheelOneRealPos.y, -wheelOneRealPos.z), xRot,
                0.0f, 1.0f, 0.0f);
        wheelOneModelMatrix.rotateAroundPoint(new Point3D(-wheelOneRealPos.x, -wheelOneRealPos.y, -wheelOneRealPos.z), yRot,
                1.0f, 0.0f, 0.0f);
        wheelOneModelMatrix.rotate(-270.0f, 0.0f, 0.0f, 1.0f);
        wheelOneModelMatrix.scale(wheelScaleAfter);

        Point3D wheelTwoRelativePos = new Point3D(-wheelOneRelativePos.x, wheelOneRelativePos.y, wheelOneRelativePos.z);
        Point3D wheelTwoRealPos = new Point3D(truckOneX + wheelTwoRelativePos.x,
                truckOneY + wheelTwoRelativePos.y,
                truckOneZ + wheelTwoRelativePos.z);

        wheelTwoModelMatrix.reset();
        wheelTwoModelMatrix.translate(wheelTwoRealPos.x, wheelTwoRealPos.y, wheelTwoRealPos.z);
        wheelTwoModelMatrix.rotateAroundPoint(new Point3D(-wheelTwoRealPos.x, -wheelTwoRealPos.y, -wheelTwoRealPos.z), xRot,
                0.0f, 1.0f, 0.0f);
        wheelTwoModelMatrix.rotateAroundPoint(new Point3D(-wheelTwoRealPos.x, -wheelTwoRealPos.y, -wheelTwoRealPos.z), yRot,
                1.0f, 0.0f, 0.0f);
        wheelTwoModelMatrix.rotate(-90.0f, 0.0f, 0.0f, 1.0f);
        wheelTwoModelMatrix.scale(wheelScaleAfter);

        Point3D wheelThreeRelativePos = new Point3D(0.65f, -0.22f, 0.115f);
        Point3D wheelThreeRealPos = new Point3D(truckTwoX + wheelThreeRelativePos.x,
                truckTwoY + wheelThreeRelativePos.y,
                truckTwoZ + wheelThreeRelativePos.z);

        wheelThreeModelMatrix.reset();
        wheelThreeModelMatrix.translate(wheelThreeRealPos.x, wheelThreeRealPos.y, wheelThreeRealPos.z);
        wheelThreeModelMatrix.rotateAroundPoint(new Point3D(-wheelThreeRealPos.x, -wheelThreeRealPos.y, -wheelThreeRealPos.z), xRot,
                0.0f, 1.0f, 0.0f);
        wheelThreeModelMatrix.rotateAroundPoint(new Point3D(-wheelThreeRealPos.x, -wheelThreeRealPos.y, -wheelThreeRealPos.z), yRot,
                1.0f, 0.0f, 0.0f);
        wheelThreeModelMatrix.rotate(-90.0f, 0.0f, 0.0f, 1.0f);
        wheelThreeModelMatrix.scale(wheelScaleAfter);

        Point3D wheelFourRelativePos = new Point3D(-wheelThreeRelativePos.x, wheelThreeRelativePos.y, wheelThreeRelativePos.z);
        Point3D wheelFourRealPos = new Point3D(truckTwoX + wheelFourRelativePos.x,
                truckTwoY + wheelFourRelativePos.y,
                truckTwoZ + wheelFourRelativePos.z);

        wheelFourModelMatrix.reset();
        wheelFourModelMatrix.translate(wheelFourRealPos.x, wheelFourRealPos.y, wheelFourRealPos.z);
        wheelFourModelMatrix.rotateAroundPoint(new Point3D(-wheelFourRealPos.x, -wheelFourRealPos.y, -wheelFourRealPos.z), xRot,
                0.0f, 1.0f, 0.0f);
        wheelFourModelMatrix.rotateAroundPoint(new Point3D(-wheelFourRealPos.x, -wheelFourRealPos.y, -wheelFourRealPos.z), yRot,
                1.0f, 0.0f, 0.0f);
        wheelFourModelMatrix.rotate(-270.0f, 0.0f, 0.0f, 1.0f);
        wheelFourModelMatrix.scale(wheelScaleAfter);

    /*    bearingOneModelMatrix.reset();
        bearingOneModelMatrix.translate(0.0f, truckPosY, truckPosZ);
        bearingOneModelMatrix.rotateAroundPoint(new Point3D(0.0f, -truckPosY, -truckPosZ), xRot,
                0.0f, 1.0f, 0.0f);
        bearingOneModelMatrix.rotateAroundPoint(new Point3D(0.0f, -truckPosY, -truckPosZ), yRot,
                1.0f, 0.0f, 0.0f);
        bearingOneModelMatrix.scale(1f);*/



        bearingTwoModelMatrix.reset();


        bearingThreeModelMatrix.reset();
        bearingFourModelMatrix.reset();
        bearingFiveModelMatrix.reset();
        bearingSixModelMatrix.reset();
        bearingSevenModelMatrix.reset();
        bearingEightModelMatrix.reset();



/*        float wheelPosY = truckPosY - 0.5f;
        wheelOneModelMatrix.reset();
        wheelOneModelMatrix.translate(0.0f, wheelPosY, truckPosZ);
        wheelOneModelMatrix.rotateAroundPoint(new Point3D(0.0f, -wheelPosY, -truckPosZ), xRot,
                0.0f, 1.0f, 0.0f);
        wheelOneModelMatrix.rotateAroundPoint(new Point3D(0.0f, -wheelPosY, -truckPosZ), yRot,
                1.0f, 0.0f, 0.0f);
        wheelOneModelMatrix.scale(0.1f);*/





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
       //     bearing.render(modelCamera, bearingOneModelMatrix);
        /*    bearing.render(modelCamera, bearingTwoModelMatrix);
            bearing.render(modelCamera, bearingThreeModelMatrix);
            bearing.render(modelCamera, bearingFourModelMatrix);*/
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

