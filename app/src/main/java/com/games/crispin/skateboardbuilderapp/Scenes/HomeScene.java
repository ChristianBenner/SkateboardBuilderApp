package com.games.crispin.skateboardbuilderapp.Scenes;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Scale2D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Font;
import com.games.crispin.crispinmobile.Rendering.Utilities.Texture;
import com.games.crispin.crispinmobile.UserInterface.Image;
import com.games.crispin.crispinmobile.UserInterface.LinearLayout;
import com.games.crispin.crispinmobile.UserInterface.Text;
import com.games.crispin.crispinmobile.UserInterface.TouchListener;
import com.games.crispin.crispinmobile.Utilities.Scene;
import com.games.crispin.skateboardbuilderapp.CustomButton;
import com.games.crispin.skateboardbuilderapp.FadeTransition;
import com.games.crispin.skateboardbuilderapp.R;

public class HomeScene extends Scene
{
    public static final Colour BACKGROUND_COLOR = new Colour(57, 142, 178);

    private static final Scale2D BUTTON_SIZE = new Scale2D(200.0f, 200.0f);
    private static final Scale2D BUTTON_PADDING = new Scale2D(40.0f, 0.0f);
    private static final float TEXT_LINE_LENGTH = BUTTON_SIZE.x;
    private static final Scale2D TEXT_PADDING = new Scale2D(40.0f, 0.0f);

    private Image logoImage;
    private CustomButton newButton;
    private CustomButton openButton;
    private CustomButton buyButton;
    private Text newText;
    private Text openText;
    private Text buyText;

    private LinearLayout buttonLayout;
    private LinearLayout textLayout;

    private FadeTransition fadeTransition;

    private Camera2D uiCamera;

    public HomeScene()
    {
        // Set the background to a blue colour
        Crispin.setBackgroundColour(BACKGROUND_COLOR);

        // Create a fade transition for fading in
        fadeTransition = new FadeTransition();
        fadeTransition.setFadeColour(BACKGROUND_COLOR);
        fadeTransition.fadeIn();

        // Create a 2D camera for the UI rendering
        uiCamera = new Camera2D();

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

        final float buttonLogoSpacing = 150.0f;
        final float buttonTextSpacing = 10.0f;
        final float buttonPosY = logoImage.getPosition().y - buttonLogoSpacing - BUTTON_SIZE.y;
        final float textPosY = buttonPosY + BUTTON_SIZE.y + buttonTextSpacing;

        final Font TEXT_FONT = new Font(R.raw.aileron_regular, 48);

        // Create a linear layout for the text UI elements
        textLayout = new LinearLayout(new Point2D((Crispin.getSurfaceWidth() / 2.0f) -
                (TEXT_LINE_LENGTH / 2.0f) - TEXT_PADDING.x - TEXT_LINE_LENGTH, textPosY));
        textLayout.setPadding(TEXT_PADDING);
        newText = new Text(TEXT_FONT, "NEW", false, true,
                TEXT_LINE_LENGTH);
        openText = new Text(TEXT_FONT, "OPEN", false, true,
                TEXT_LINE_LENGTH);
        buyText = new Text(TEXT_FONT, "BUY", false, true,
                TEXT_LINE_LENGTH);
        newText.setColour(Colour.WHITE);
        openText.setColour(Colour.WHITE);
        buyText.setColour(Colour.WHITE);
        textLayout.add(newText);
        textLayout.add(openText);
        textLayout.add(buyText);

        // Create a linear layout for the button UI elements
        buttonLayout = new LinearLayout(new Point2D((Crispin.getSurfaceWidth() / 2.0f) -
                (BUTTON_SIZE.x / 2.0f) - BUTTON_PADDING.x - BUTTON_SIZE.x, buttonPosY));
        buttonLayout.setPadding(BUTTON_PADDING);
        newButton = new CustomButton(new Texture(R.drawable.pencil_icon));
        openButton = new CustomButton(new Texture(R.drawable.folder_icon));
        buyButton = new CustomButton(new Texture(R.drawable.bank_card_icon));
        newButton.setSize(BUTTON_SIZE);
        openButton.setSize(BUTTON_SIZE);
        buyButton.setSize(BUTTON_SIZE);
        buttonLayout.add(newButton);
        buttonLayout.add(openButton);
        buttonLayout.add(buyButton);

        // Add touch listeners to button
        newButton.addTouchListener(buttonNewTouchListener);
        openButton.addTouchListener(buttonOpenTouchListener);
        buyButton.addTouchListener(buyButtonTouchListener);
    }

    @Override
    public void update(float deltaTime)
    {
        newButton.update(deltaTime);
        openButton.update(deltaTime);
        buyButton.update(deltaTime);

        newText.setColour(newButton.getColour());
        openText.setColour(openButton.getColour());
        buyButton.setColour(buyButton.getColour());

        fadeTransition.update(deltaTime);
    }

    @Override
    public void render()
    {
        logoImage.draw(uiCamera);
        buttonLayout.draw(uiCamera);
        textLayout.draw(uiCamera);
        fadeTransition.draw(uiCamera);
    }

    @Override
    public void touch(int type, Point2D position)
    {
        // Nothing to do here (no touch controls on this scene)
    }

    // The 'new' button touch listener
    private final TouchListener buttonNewTouchListener = e ->
    {
        switch (e.getEvent())
        {
            case CLICK:
                newText.enableWiggle(40.0f, Text.WiggleSpeed_E.FAST);
                fadeTransition.fadeOutToScence(SelectDeckWidthScene::new);
                break;
            case RELEASE:
                newText.disableWiggle();
                break;
        }
    };

    // The 'open' button touch listener
    private final TouchListener buttonOpenTouchListener = e ->
    {
        switch (e.getEvent())
        {
            case CLICK:
                openText.enableWiggle(40.0f, Text.WiggleSpeed_E.FAST);
                break;
            case RELEASE:
                openText.disableWiggle();
                break;
        }
    };

    // The 'buy' button touch listener
    private final TouchListener buyButtonTouchListener = e -> {
        switch (e.getEvent())
        {
            case CLICK:
                buyText.enableWiggle(40.0f, Text.WiggleSpeed_E.FAST);
                break;
            case RELEASE:
                buyText.disableWiggle();
                break;
        }
    };
}
