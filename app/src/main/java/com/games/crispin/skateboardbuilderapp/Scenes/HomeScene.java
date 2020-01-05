package com.games.crispin.skateboardbuilderapp.Scenes;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Scale2D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.games.crispin.crispinmobile.UserInterface.Font;
import com.games.crispin.crispinmobile.Rendering.Utilities.Texture;
import com.games.crispin.crispinmobile.UserInterface.Image;
import com.games.crispin.crispinmobile.UserInterface.LinearLayout;
import com.games.crispin.crispinmobile.UserInterface.Text;
import com.games.crispin.crispinmobile.UserInterface.TouchListener;
import com.games.crispin.crispinmobile.Utilities.Scene;
import com.games.crispin.skateboardbuilderapp.ConfigReaders.SaveManager;
import com.games.crispin.skateboardbuilderapp.Constants;
import com.games.crispin.skateboardbuilderapp.UserInterface.CustomButton;
import com.games.crispin.skateboardbuilderapp.FadeTransition;
import com.games.crispin.skateboardbuilderapp.R;
import com.games.crispin.skateboardbuilderapp.SkateboardComponents.Skateboard;

/**
 * The HomeScene class manages and displays the Home page in the application. It is the page that
 * users are first greeted with and allows them to navigate to different sections of the app. From
 * the home page users can either create a new design, open an existing design or purchase an
 * existing design. The class extends the CrispinEngine class 'Scene' which allows the engine to
 * handle it.
 *
 * @see         Scene
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
public class HomeScene extends Scene
{
    // Button size for the navigation buttons
    private static final Scale2D BUTTON_SIZE = new Scale2D(200.0f, 200.0f);

    // Padding between the navigation buttons
    private static final Scale2D BUTTON_PADDING = new Scale2D(40.0f, 0.0f);

    // The length of the text above each navigation button
    private static final float TEXT_LINE_LENGTH = BUTTON_SIZE.x;

    // Padding between each of the pieces of text above the navigation buttons
    private static final Scale2D TEXT_PADDING = BUTTON_PADDING;

    // User interface 2D camera
    private Camera2D uiCamera;

    // Fade transition user interface
    private FadeTransition fadeTransition;

    // The image that stores and displays the Skateboard Builder logo
    private Image logoImage;

    // Linear layout for the navigation buttons
    private LinearLayout buttonLayout;

    // Linear layout for the text above the navigation buttons
    private LinearLayout textLayout;

    // New button user interface
    private CustomButton newButton;

    // New text user interface
    private Text newText;

    // Open button user interface
    private CustomButton openButton;

    // Open text user interface
    private Text openText;

    // Buy button user interface
    private CustomButton buyButton;

    // Buy text user interface
    private Text buyText;

    /**
     * Constructor that creates the home scene
     *
     * @since 1.0
     */
    public HomeScene()
    {
        // Set the background to a blue colour
        Crispin.setBackgroundColour(Constants.BACKGROUND_COLOR);

        // Create a 2D camera for the UI rendering
        uiCamera = new Camera2D();

        // Create a fade transition for fading in
        fadeTransition = new FadeTransition();
        fadeTransition.setFadeColour(Constants.BACKGROUND_COLOR);
        fadeTransition.fadeIn();

        // Load the logo texture
        final Texture logoTexture = new Texture(R.drawable.logo);

        // The height-width ratio of the logo texture (so that we can scale it correctly)
        final float heightWidthRatio = logoTexture.getHeight() / logoTexture.getWidth();

        // We want the logo to take most of the width of the surface
        final float logoTargetWidth = Crispin.getSurfaceWidth() * 0.9f;

        // Padding for the logo and the top of the scene
        final float paddingLogo = 40.0f;

        logoImage = new Image(new Texture(R.drawable.logo), (int)logoTargetWidth,
                (int)(logoTargetWidth * heightWidthRatio));
        logoImage.setPosition((Crispin.getSurfaceWidth()/2.0f) - (logoTargetWidth/2.0f),
                Crispin.getSurfaceHeight() - (logoTargetWidth * heightWidthRatio) - paddingLogo);

        // Spacing between the logo and navigation buttons (y axis)
        final float buttonLogoSpacing = 150.0f;

        // Spacing between the text and the navigation buttons (y axis)
        final float buttonTextSpacing = 10.0f;

        // The Y position of all of the navigation buttons
        final float buttonPosY = logoImage.getPosition().y - buttonLogoSpacing - BUTTON_SIZE.y;

        // The y position of all of the navigation button text
        final float textPosY = buttonPosY + BUTTON_SIZE.y + buttonTextSpacing;

        // Text font
        final Font aileronRegularFont = new Font(R.raw.aileron_regular, 48);

        // Create a linear layout for the button UI elements
        buttonLayout = new LinearLayout(new Point2D((Crispin.getSurfaceWidth() / 2.0f) -
                (BUTTON_SIZE.x / 2.0f) - BUTTON_PADDING.x - BUTTON_SIZE.x, buttonPosY));
        buttonLayout.setPadding(BUTTON_PADDING);

        // Create a linear layout for the text UI elements
        textLayout = new LinearLayout(new Point2D((Crispin.getSurfaceWidth() / 2.0f) -
                (TEXT_LINE_LENGTH / 2.0f) - TEXT_PADDING.x - TEXT_LINE_LENGTH, textPosY));
        textLayout.setPadding(TEXT_PADDING);

        // Create the new button and add it to the button layout
        newButton = new CustomButton(new Texture(R.drawable.pencil_icon));
        newButton.addTouchListener(buttonNewTouchListener);
        newButton.setSize(BUTTON_SIZE);
        buttonLayout.add(newButton);

        // Create the new text and add it to the text layout
        newText = new Text(aileronRegularFont, "NEW", false, true,
                TEXT_LINE_LENGTH);
        newText.setColour(Colour.WHITE);
        textLayout.add(newText);

        // Create the open button and add it to the button layout
        openButton = new CustomButton(new Texture(R.drawable.folder_icon));
        openButton.addTouchListener(buttonOpenTouchListener);
        openButton.setSize(BUTTON_SIZE);
        buttonLayout.add(openButton);

        // Create the open text and add it to the text layout
        openText = new Text(aileronRegularFont, "OPEN", false, true,
                TEXT_LINE_LENGTH);
        openText.setColour(Colour.WHITE);
        textLayout.add(openText);

        // Create the buy button and add it to the button layout
        buyButton = new CustomButton(new Texture(R.drawable.bank_card_icon));
        buyButton.addTouchListener(buyButtonTouchListener);
        buyButton.setSize(BUTTON_SIZE);
        buttonLayout.add(buyButton);

        // Create the buy text and add it to the text layout
        buyText = new Text(aileronRegularFont, "BUY", false, true,
                TEXT_LINE_LENGTH);
        buyText.setColour(Colour.WHITE);
        textLayout.add(buyText);
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
        // Update all of the buttons
        newButton.update(deltaTime);
        openButton.update(deltaTime);
        buyButton.update(deltaTime);

        // Update all of the text
        newText.setColour(newButton.getColour());
        openText.setColour(openButton.getColour());
        buyButton.setColour(buyButton.getColour());

        // Update the fade transition
        fadeTransition.update(deltaTime);
    }

    /**
     * Render function overridden from the Scene parent class. The render function should contain
     * the draw. The function is where the user interface is drawn so it is processed by the engine.
     *
     * @see     Scene
     * @since   1.0
     */
    @Override
    public void render()
    {
        // Draw all of the user interface elements
        logoImage.draw(uiCamera);
        buttonLayout.draw(uiCamera);
        textLayout.draw(uiCamera);
        fadeTransition.draw(uiCamera);
    }

    /**
     * Touch function overridden from the Scene parent class. The touch function allows you to
     * intercept user touch input and process it. There is no touch controls on the home page except
     * for the user interface which is handled by the engine.
     *
     * @param type      The type of touch event (e.g. click, release or drag)
     * @param position  The position of the touch event (x, y)
     * @see             Scene
     * @since           1.0
     */
    @Override
    public void touch(int type, Point2D position)
    {
        // Nothing to do here (no touch controls on this scene)
    }

    /**
     * Touch listener for the 'new' navigation button. This assigns some logic to the action of the
     * user interacting with the 'new' button.
     *
     * @param event     The touch event
     * @see             TouchListener
     * @since           1.0
     */
    private final TouchListener buttonNewTouchListener = event ->
    {
        // Switch through the type of the event
        switch (event.getEvent())
        {
            case CLICK:
                // Start wiggling the text above the button
                newText.enableWiggle(40.0f, Text.WiggleSpeed_E.FAST);
                break;
            case RELEASE:
                // Stop wiggling the text above the button
                newText.disableWiggle();

                // Clear the current save because we do not want the select component scenes to
                // open on any existing save
                SaveManager.writeCurrentSave(new Skateboard());

                // Transition to the first scene in board creation (the Select Deck Width page)
                fadeTransition.fadeOutToScence(SelectDeckWidthScene::new);
                break;
        }
    };

    /**
     * Touch listener for the 'open' navigation button. This assigns some logic to the action of the
     * user interacting with the 'open' button.
     *
     * @param event     The touch event
     * @see             TouchListener
     * @since           1.0
     */
    private final TouchListener buttonOpenTouchListener = e ->
    {
        // Switch through the type of the event
        switch (e.getEvent())
        {
            case CLICK:
                // Start wiggling the text above the button
                openText.enableWiggle(40.0f, Text.WiggleSpeed_E.FAST);
                break;
            case RELEASE:
                // Stop wiggling the text above the button
                openText.disableWiggle();

                // Transition to the open save page
                fadeTransition.fadeOutToScence(OpenSaveScene::new);
                break;
        }
    };

    /**
     * Touch listener for the 'buy' navigation button. This assigns some logic to the action of the
     * user interacting with the 'buy' button.
     *
     * @param event     The touch event
     * @see             TouchListener
     * @since           1.0
     */
    private final TouchListener buyButtonTouchListener = e -> {
        switch (e.getEvent())
        {
            case CLICK:
                // Start wiggling the text above the button
                buyText.enableWiggle(40.0f, Text.WiggleSpeed_E.FAST);
                break;
            case RELEASE:
                // Stop wiggling the text above the button
                buyText.disableWiggle();

                // Transition to the buy page
                fadeTransition.fadeOutToScence(BuyScene::new);
                break;
        }
    };
}
