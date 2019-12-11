package com.games.crispin.skateboardbuilderapp;

import android.view.MotionEvent;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Scale2D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Rendering.Models.Square;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Font;
import com.games.crispin.crispinmobile.Rendering.Utilities.Material;
import com.games.crispin.crispinmobile.Rendering.Utilities.Texture;
import com.games.crispin.crispinmobile.UserInterface.Button;
import com.games.crispin.crispinmobile.UserInterface.Image;
import com.games.crispin.crispinmobile.UserInterface.LinearLayout;
import com.games.crispin.crispinmobile.UserInterface.Text;
import com.games.crispin.crispinmobile.Utilities.Scene;

public class HomeScene extends Scene
{
    private Image logoImage;
    private Button newButton;
    private Button openButton;
    private Button buyButton;
    private Text newText;
    private Text openText;
    private Text buyText;

    private LinearLayout buttonLayout;
    private LinearLayout textLayout;

    private FadeTransition fadeTransition;

    private Camera2D uiCamera;

    private float newUIColour = 1.0f;
    private float openUIColour = 1.0f;
    private float buyUIColour = 1.0f;

    private static final Colour BACKGROUND_COLOR = new Colour(57, 142, 178);

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

        final float buttonSize = 200.0f;
        final float buttonPadding = 40.0f;
        final float buttonLogoSpacing = 150.0f;
        final float buttonTextSpacing = 10.0f;
        final float buttonPosY = logoImage.getPosition().y - buttonLogoSpacing - buttonSize;
        final float textPosY = buttonPosY + buttonSize + buttonTextSpacing;

        final Font buttonFont = new Font(R.raw.aileron_regular, 48);

        newText = new Text(buttonFont, "NEW", false, true,
                buttonSize);
        openText = new Text(buttonFont, "OPEN", false, true,
                buttonSize);
        buyText = new Text(buttonFont, "BUY", false, true,
                buttonSize);
        newButton = new Button(new Texture(R.drawable.pencil_icon));
        openButton = new Button(new Texture(R.drawable.folder_icon));
        buyButton = new Button(new Texture(R.drawable.bank_card_icon));

        newText.setColour(Colour.WHITE);
        openText.setColour(Colour.WHITE);
        buyText.setColour(Colour.WHITE);
        newButton.setSize(new Scale2D(buttonSize, buttonSize));
        openButton.setSize(new Scale2D(buttonSize, buttonSize));
        buyButton.setSize(new Scale2D(buttonSize, buttonSize));


        buttonLayout = new LinearLayout(new Point2D((Crispin.getSurfaceWidth() / 2.0f) - (buttonSize / 2.0f) - buttonPadding - buttonSize, buttonPosY));
        buttonLayout.setPadding(new Scale2D(buttonPadding, 0.0f));
        buttonLayout.add(newButton);
        buttonLayout.add(openButton);
        buttonLayout.add(buyButton);

        textLayout = new LinearLayout(new Point2D((Crispin.getSurfaceWidth() / 2.0f) - (buttonSize / 2.0f) - buttonPadding - buttonSize, textPosY));
        textLayout.setPadding(new Scale2D(buttonPadding, 0.0f));
        textLayout.add(newText);
        textLayout.add(openText);
        textLayout.add(buyText);

        newButton.addTouchListener(e -> {
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
        });

        openButton.addTouchListener(e -> {
            switch (e.getEvent())
            {
                case CLICK:
                    openText.enableWiggle(40.0f, Text.WiggleSpeed_E.FAST);
                    fadeTransition.fadeOutToScence(TestScene::new);
                    break;
                case RELEASE:
                    openText.disableWiggle();
                    break;
            }
        });

        buyButton.addTouchListener(e -> {
            switch (e.getEvent())
            {
                case CLICK:
                    buyText.enableWiggle(40.0f, Text.WiggleSpeed_E.FAST);
                    break;
                case RELEASE:
                    buyText.disableWiggle();
                    break;
            }
        });
    }

    void handleNewButtonColour()
    {
        if(newButton.isClicked())
        {
            if(newUIColour <= 0.0f)
            {
                newUIColour = 0.0f;
            }
            else
            {
                newUIColour -= 0.05f;
            }

            newButton.setColour(new Colour(1.0f, newUIColour, newUIColour));
            newText.setColour(new Colour(1.0f, newUIColour, newUIColour));
        }
        else
        {
            if(newUIColour >= 1.0f)
            {
                newUIColour = 1.0f;
            }
            else
            {
                newUIColour += 0.05f;
            }

            newButton.setColour(new Colour(1.0f, newUIColour, newUIColour));
            newText.setColour(new Colour(1.0f, newUIColour, newUIColour));
        }
    }

    void handleOpenButtonColour()
    {
        if(openButton.isClicked())
        {
            if(openUIColour <= 0.0f)
            {
                openUIColour = 0.0f;
            }
            else
            {
                openUIColour -= 0.05f;
            }

            openButton.setColour(new Colour(1.0f, openUIColour, openUIColour));
            openText.setColour(new Colour(1.0f, openUIColour, openUIColour));
        }
        else
        {
            if(openUIColour >= 1.0f)
            {
                openUIColour = 1.0f;
            }
            else
            {
                openUIColour += 0.05f;
            }

            openButton.setColour(new Colour(1.0f, openUIColour, openUIColour));
            openText.setColour(new Colour(1.0f, openUIColour, openUIColour));
        }
    }

    void handleBuyButtonColour()
    {
        if(buyButton.isClicked())
        {
            if(buyUIColour <= 0.0f)
            {
                buyUIColour = 0.0f;
            }
            else
            {
                buyUIColour -= 0.05f;
            }

            buyButton.setColour(new Colour(1.0f, buyUIColour, buyUIColour));
            buyText.setColour(new Colour(1.0f, buyUIColour, buyUIColour));
        }
        else
        {
            if(buyUIColour >= 1.0f)
            {
                buyUIColour = 1.0f;
            }
            else
            {
                buyUIColour += 0.05f;
            }

            buyButton.setColour(new Colour(1.0f, buyUIColour, buyUIColour));
            buyText.setColour(new Colour(1.0f, buyUIColour, buyUIColour));
        }
    }

    @Override
    public void update(float deltaTime)
    {
        handleNewButtonColour();
        handleOpenButtonColour();
        handleBuyButtonColour();

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

    }
}
