package com.games.crispin.skateboardbuilderapp.Scenes;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Scale2D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.games.crispin.crispinmobile.UserInterface.Border;
import com.games.crispin.crispinmobile.UserInterface.Button;
import com.games.crispin.crispinmobile.UserInterface.Dropdown;
import com.games.crispin.crispinmobile.UserInterface.Font;
import com.games.crispin.crispinmobile.UserInterface.Text;
import com.games.crispin.crispinmobile.Utilities.Scene;
import com.games.crispin.skateboardbuilderapp.ConfigReaders.BearingConfigReader;
import com.games.crispin.skateboardbuilderapp.ConfigReaders.DesignConfigReader;
import com.games.crispin.skateboardbuilderapp.ConfigReaders.GripConfigReader;
import com.games.crispin.skateboardbuilderapp.ConfigReaders.SaveManager;
import com.games.crispin.skateboardbuilderapp.ConfigReaders.TruckConfigReader;
import com.games.crispin.skateboardbuilderapp.ConfigReaders.WheelConfigReader;
import com.games.crispin.skateboardbuilderapp.Constants;
import com.games.crispin.skateboardbuilderapp.UserInterface.CustomButton;
import com.games.crispin.skateboardbuilderapp.UserInterface.FadeTransition;
import com.games.crispin.skateboardbuilderapp.R;
import com.games.crispin.skateboardbuilderapp.SkateboardComponents.Bearing;
import com.games.crispin.skateboardbuilderapp.SkateboardComponents.Design;
import com.games.crispin.skateboardbuilderapp.SkateboardComponents.Grip;
import com.games.crispin.skateboardbuilderapp.SkateboardComponents.Skateboard;
import com.games.crispin.skateboardbuilderapp.SkateboardComponents.Truck;
import com.games.crispin.skateboardbuilderapp.SkateboardComponents.Wheel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BuyScene extends Scene
{
    // Padding for the select deck width dropdown
    private static final Point2D SELECT_SAVE_DROPDOWN_PADDING = new Point2D(50.0f,
            50.0f);

    // Size for the select deck width dropdown
    private static final Scale2D SELECT_SAVE_DROPDOWN_SIZE = new Scale2D(
            Crispin.getSurfaceWidth() - (SELECT_SAVE_DROPDOWN_PADDING.x * 2.0f),
            100.0f);

    // Camera for 2D/user interface rendering
    private Camera2D uiCamera;

    private HashMap<Integer, Skateboard> dropdownComponents;

    private FadeTransition fadeTransition;

    private Dropdown saveDropdown;

    // Back button UI (return to home screen)
    private CustomButton backButton;

    // Buy button
    private Button buyButton;

    private Text titleText;

    private Font partInfoFont;

    private Text partListTitle;
    private Text deckText;
    private Text deckPrice;

    private Text gripText;
    private Text gripPrice;

    private Text truckText;
    private Text truckPrice;

    private Text bearingText;
    private Text bearingPrice;

    private Text wheelText;
    private Text wheelPrice;

    private Text totalText;

    private boolean showText;

    public BuyScene()
    {
        // Set the background to a blue colour
        Crispin.setBackgroundColour(Constants.BACKGROUND_COLOR);

        // To show the text or not
        showText = false;

        // Create the user interface camera
        uiCamera = new Camera2D(0, 0, Crispin.getSurfaceWidth(), Crispin.getSurfaceHeight());

        // Create the fade transition object and set it to fade in
        fadeTransition = new FadeTransition();
        fadeTransition.setFadeColour(Constants.BACKGROUND_COLOR);
        fadeTransition.fadeIn();

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

        // The font for text on the scene
        Font aileronRegularFont = new Font(R.raw.aileron_regular, 76);

        buyButton = new Button(aileronRegularFont, "Buy");
        buyButton.setSize(Constants.NEXT_BUTTON_SIZE);
        buyButton.setPosition(Constants.getNextButtonPosition());
        buyButton.setColour(Constants.BACKGROUND_COLOR);
        buyButton.setBorder(new Border(Colour.WHITE, 8));
        buyButton.setTextColour(Colour.WHITE);
        buyButton.addTouchListener(e ->
        {
            switch (e.getEvent())
            {
                case RELEASE:
                    fadeTransition.fadeOutToScence(HomeScene::new);
                    break;
            }
        });

        titleText = new Text(aileronRegularFont, "Select a design to buy", false,
                true, Crispin.getSurfaceWidth());

        final Point2D TITLE_TEXT_POSITION = new Point2D(0.0f, Constants.getBackButtonPosition().y -
                Constants.BACK_BUTTON_PADDING.y - titleText.getHeight());

        titleText.setColour(Colour.WHITE);
        titleText.setPosition(TITLE_TEXT_POSITION);

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

                    Skateboard board = dropdownComponents.get(selectedId);

                    // DESIGN TEXT AND PRICE
                    Design design = DesignConfigReader.getInstance().getDesign(board.getDesign());
                    deckText.setText("Deck: " + design.name);
                    deckText.setPosition(50.0f, partListTitle.getPosition().y - 25.0f -
                            deckText.getHeight());

                    deckPrice.setText(priceFormat(design.price));
                    deckPrice.setPosition(Crispin.getSurfaceWidth() - 50.0f - deckPrice.getWidth(),
                            partListTitle.getPosition().y - 25.0f -
                                    deckPrice.getHeight());


                    // GRIP TEXT AND PRICE
                    Grip grip = GripConfigReader.getInstance().getGrip(board.getGrip());
                    gripText.setText("Grip: " + grip.name);
                    gripText.setPosition(50.0f, deckText.getPosition().y - 25.0f - gripText.getHeight());

                    gripPrice.setText(priceFormat(grip.price));
                    gripPrice.setPosition(Crispin.getSurfaceWidth() - 50.0f - gripPrice.getWidth(),
                            deckText.getPosition().y - 25.0f - gripPrice.getHeight());


                    // TRUCK TEXT AND PRICE
                    Truck truck = TruckConfigReader.getInstance().getTruck(board.getTrucks());
                    truckText.setText("Truck: " + truck.name);
                    truckText.setPosition(50.0f, gripText.getPosition().y - 25.0f -
                            truckText.getHeight());

                    truckPrice.setText(priceFormat(truck.price));
                    truckPrice.setPosition(Crispin.getSurfaceWidth() - 50.0f - truckPrice.getWidth(),
                            gripText.getPosition().y - 25.0f - truckPrice.getHeight());


                    // BEARING TEXT AND PRICE
                    Bearing bearing = BearingConfigReader.getInstance().getBearing(board.getBearings());
                    bearingText.setText("Bearing: " + bearing.name);
                    bearingText.setPosition(50.0f, truckPrice.getPosition().y - 25.0f -
                            bearingText.getHeight());

                    bearingPrice.setText(priceFormat(bearing.price));
                    bearingPrice.setPosition(Crispin.getSurfaceWidth() - 50.0f - bearingPrice.getWidth(),
                            truckText.getPosition().y - 25.0f - bearingPrice.getHeight());


                    // WHEEL TEXT AND PRICE
                    Wheel wheel  = WheelConfigReader.getInstance().getWheel(board.getWheels());
                    wheelText.setText("Wheel: " + wheel.name);
                    wheelText.setPosition(50.0f, bearingText.getPosition().y - 25.0f -
                            wheelText.getHeight());

                    wheelPrice.setText(priceFormat(wheel.price));
                    wheelPrice.setPosition(Crispin.getSurfaceWidth() - 50.0f - wheelPrice.getWidth(),
                            bearingText.getPosition().y - 25.0f - wheelPrice.getHeight());

                    float total = design.price + grip.price + truck.price + bearing.price +
                            wheel.price;
                    totalText.setText("Total: " + priceFormat(total));
                    totalText.setPosition(50.0f, wheelText.getPosition().y - 100.0f);

                    showText = true;
                    break;
            }
        });

        ArrayList<Character> specialCharacters = new ArrayList<>();
        specialCharacters.add('£');

        partInfoFont = new Font(R.raw.aileron_regular, 36, specialCharacters);
        partListTitle = new Text(partInfoFont, "Selected parts:", true,
                false, Crispin.getSurfaceWidth());
        partListTitle.setPosition(50.0f, saveDropdown.getPosition().y - 50.0f -
                partListTitle.getHeight());
        partListTitle.setColour(Colour.WHITE);

        // Open some info on the save
        deckText = new Text(partInfoFont, "Deck: None selected", true,
                false, Crispin.getSurfaceWidth());
        deckText.setPosition(50.0f, partListTitle.getPosition().y - 25.0f -
                deckText.getHeight());
        deckText.setColour(Colour.WHITE);

        deckPrice = new Text(partInfoFont, "£0.00", true,
                false, Crispin.getSurfaceWidth());
        deckPrice.setPosition(Crispin.getSurfaceWidth() - 50.0f - deckPrice.getWidth(),
                partListTitle.getPosition().y - 25.0f -
                deckPrice.getHeight());
        deckPrice.setColour(Colour.WHITE);

        gripText = new Text(partInfoFont, "Grip: None selected", true,
                false, Crispin.getSurfaceWidth());
        gripText.setPosition(50.0f, deckText.getPosition().y - 25.0f - gripText.getHeight());
        gripText.setColour(Colour.WHITE);

        gripPrice = new Text(partInfoFont, "£0.00", true,
                false, Crispin.getSurfaceWidth());
        gripPrice.setPosition(Crispin.getSurfaceWidth() - 50.0f - gripPrice.getWidth(),
                deckText.getPosition().y - 25.0f - gripPrice.getHeight());
        gripPrice.setColour(Colour.WHITE);

        truckText = new Text(partInfoFont, "Truck: None selected", true,
                false, Crispin.getSurfaceWidth());
        truckText.setPosition(50.0f, gripText.getPosition().y - 25.0f - truckText.getHeight());
        truckText.setColour(Colour.WHITE);

        truckPrice = new Text(partInfoFont, "£0.00", true,
                false, Crispin.getSurfaceWidth());
        truckPrice.setPosition(Crispin.getSurfaceWidth() - 50.0f - truckPrice.getWidth(),
                gripText.getPosition().y - 25.0f - gripPrice.getHeight());
        truckPrice.setColour(Colour.WHITE);

        bearingText = new Text(partInfoFont, "Bearing: None selected", true,
                false, Crispin.getSurfaceWidth());
        bearingText.setPosition(50.0f, truckText.getPosition().y - 25.0f -
                bearingText.getHeight());
        bearingText.setColour(Colour.WHITE);

        bearingPrice = new Text(partInfoFont, "£0.00", true,
                false, Crispin.getSurfaceWidth());
        bearingPrice.setPosition(Crispin.getSurfaceWidth() - 50.0f - bearingPrice.getWidth(),
                truckText.getPosition().y - 25.0f - bearingPrice.getHeight());
        bearingPrice.setColour(Colour.WHITE);

        // Wheel text and price
        wheelText = new Text(partInfoFont, "Wheel: None selected", true,
                false, Crispin.getSurfaceWidth());
        wheelText.setPosition(50.0f, bearingText.getPosition().y - 25.0f -
                wheelText.getHeight());
        wheelText.setColour(Colour.WHITE);

        wheelPrice = new Text(partInfoFont, "£0.00", true,
                false, Crispin.getSurfaceWidth());
        wheelPrice.setPosition(Crispin.getSurfaceWidth() - 50.0f - wheelPrice.getWidth(),
                bearingText.getPosition().y - 25.0f - wheelPrice.getHeight());
        wheelPrice.setColour(Colour.WHITE);

        totalText = new Text(partInfoFont, "Total: £0.00", true, false,
                Crispin.getSurfaceWidth());
        totalText.setPosition(50.0f, wheelText.getPosition().y - 100.0f);
        totalText.setColour(Colour.WHITE);


/*        private Text truckText;
        private Text truckPrice;

        private Text bearingText;
        private Text bearingPrice;

        private Text wheelText;
        private Text wheelPrice;

        private Text totalText;*/
    }

    @Override
    public void update(float deltaTime)
    {
        backButton.update(deltaTime);
        fadeTransition.update(deltaTime);
    }

    @Override
    public void render()
    {
        backButton.draw(uiCamera);
        buyButton.draw(uiCamera);
        titleText.draw(uiCamera);

        if(showText)
        {
            partListTitle.draw(uiCamera);
            deckText.draw(uiCamera);
            deckPrice.draw(uiCamera);
            gripText.draw(uiCamera);
            gripPrice.draw(uiCamera);
            truckText.draw(uiCamera);
            truckPrice.draw(uiCamera);
            bearingText.draw(uiCamera);
            bearingPrice.draw(uiCamera);
            wheelText.draw(uiCamera);
            wheelPrice.draw(uiCamera);
            totalText.draw(uiCamera);
        }

        saveDropdown.draw(uiCamera);

        fadeTransition.draw(uiCamera);
    }

    @Override
    public void touch(int type, Point2D position)
    {

    }

    private String priceFormat(float price)
    {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(price);
    }
}
