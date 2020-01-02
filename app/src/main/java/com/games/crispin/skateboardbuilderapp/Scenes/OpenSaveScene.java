package com.games.crispin.skateboardbuilderapp.Scenes;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Scale2D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.games.crispin.crispinmobile.UserInterface.Font;
import com.games.crispin.crispinmobile.UserInterface.Dropdown;
import com.games.crispin.crispinmobile.UserInterface.Text;
import com.games.crispin.crispinmobile.Utilities.Scene;
import com.games.crispin.skateboardbuilderapp.ConfigReaders.SaveManager;
import com.games.crispin.skateboardbuilderapp.Constants;
import com.games.crispin.skateboardbuilderapp.CustomButton;
import com.games.crispin.skateboardbuilderapp.FadeTransition;
import com.games.crispin.skateboardbuilderapp.R;
import com.games.crispin.skateboardbuilderapp.SkateboardComponents.Skateboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OpenSaveScene extends Scene
{
    // Position of the back button
    private static final Point2D BACK_BUTTON_POSITION = new Point2D(Constants.BACK_BUTTON_PADDING.x,
            Crispin.getSurfaceHeight() - Constants.BACK_BUTTON_PADDING.y -
                    Constants.BACK_BUTTON_SIZE.y);

    // Padding for the select deck width dropdown
    private static final Point2D SELECT_SAVE_DROPDOWN_PADDING = new Point2D(50.0f,
            50.0f);

    // Size for the select deck width dropdown
    private static final Scale2D SELECT_SAVE_DROPDOWN_SIZE = new Scale2D(
            Crispin.getSurfaceWidth() - (SELECT_SAVE_DROPDOWN_PADDING.x * 2.0f),
            100.0f);

    private FadeTransition fadeTransition;

    private Camera2D uiCamera;

    private Text title;

    private CustomButton backButton;

    private Dropdown saveDropdown;

    private HashMap<Integer, Skateboard> dropdownComponents;

    public OpenSaveScene()
    {
        // Set the background to a blue colour
        Crispin.setBackgroundColour(Constants.BACKGROUND_COLOR);

        // Create a fade transition for fading in
        fadeTransition = new FadeTransition();
        fadeTransition.setFadeColour(Constants.BACKGROUND_COLOR);
        fadeTransition.fadeIn();

        // Create a 2D camera for the UI rendering
        uiCamera = new Camera2D();

        Font titleFont = new Font(R.raw.aileron_regular, 76);
        title = new Text(titleFont, "Open Save", true, true,
                Crispin.getSurfaceWidth());
        final Point2D TITLE_TEXT_POSITION = new Point2D(0.0f, BACK_BUTTON_POSITION.y -
                Constants.BACK_BUTTON_PADDING.y - title.getHeight());
        title.setPosition(TITLE_TEXT_POSITION);
        title.setColour(Colour.WHITE);

        backButton = new CustomButton(R.drawable.back_icon);
        backButton.setPosition(BACK_BUTTON_POSITION);
        backButton.setSize(Constants.BACK_BUTTON_SIZE);

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

        saveDropdown = new Dropdown("Select save");

        final Point2D SELECT_DECK_WIDTH_DROPDOWN_POSITION = new Point2D(
                SELECT_SAVE_DROPDOWN_PADDING.x, TITLE_TEXT_POSITION.y -
                SELECT_SAVE_DROPDOWN_SIZE.y - SELECT_SAVE_DROPDOWN_PADDING.y);

        saveDropdown.setPosition(SELECT_DECK_WIDTH_DROPDOWN_POSITION);
        saveDropdown.setSize(SELECT_SAVE_DROPDOWN_SIZE);
        saveDropdown.setDisabledBorders(Dropdown.INNER_BORDERS);
        saveDropdown.setColour(Constants.BACKGROUND_COLOR);
        saveDropdown.setTextColour(Colour.WHITE);
        saveDropdown.setBorderColour(Colour.WHITE);
        saveDropdown.setStateIcons(R.drawable.expand_icon, R.drawable.collapse_icon);


        // Reset save file
        List<Skateboard> temp = new ArrayList<>();
        temp.add(new Skateboard("Example save", 1, 1, 1, 1, 1, 1));
        temp.add(new Skateboard("Test board", 2, 1, 2, 1, 3, 5));
       // SaveManager.writeSave(temp);
/*
        <?xml version='1.0' encoding='UTF-8' standalone='yes' ?>
            <saves>
                <skateboard deck="1" grip="1" trucks="1" bearings="1" wheels="1" design="1" name="Example save" />
                <skateboard deck="2" grip="1" trucks="2" bearings="1" wheels="3" design="5" name="Test board" />
            </saves>*/

        // Add contents to save dropdown
        List<Skateboard> saves = SaveManager.loadSaves();
        dropdownComponents = new HashMap<>();
        if(saves != null && saves.isEmpty() == false)
        {
            for(Skateboard save : saves)
            {
                System.out.println("Adding save: " + save.getName());
                dropdownComponents.put(saveDropdown.addItem(save.getName()), save);
            }
        }

        // Add touch listener to dropdown
        saveDropdown.addTouchListener(e ->
        {
            switch (e.getEvent())
            {
                case CLICK:

                    break;
                case RELEASE:
                    int selectedId = saveDropdown.getSelectedId();

                    break;
            }
        });
    }

    @Override
    public void update(float deltaTime)
    {
        fadeTransition.update(deltaTime);
    }

    @Override
    public void render()
    {
        backButton.draw(uiCamera);
        title.draw(uiCamera);
        saveDropdown.draw(uiCamera);
        fadeTransition.draw(uiCamera);
    }

    @Override
    public void touch(int type, Point2D position)
    {

    }
}
