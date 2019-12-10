package com.games.crispin.skateboardbuilderapp;

import android.view.MotionEvent;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Scale2D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Font;
import com.games.crispin.crispinmobile.Rendering.Utilities.Texture;
import com.games.crispin.crispinmobile.UserInterface.Button;
import com.games.crispin.crispinmobile.UserInterface.Image;
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

    private Camera2D uiCamera;

    private boolean fadeIn = true;
    private float uiAlpha = 0.0f;
    private float newUIColour = 1.0f;
    private float openUIColour = 1.0f;
    private float buyUIColour = 1.0f;
    private boolean holdingNewButton = false;
    private boolean holdingOpenButton = false;
    private boolean holdingBuyButton = false;

    public HomeScene()
    {
        // Set the background to a blue colour
        Crispin.setBackgroundColour(new Colour(57, 142, 178));

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
        newText.setPosition((Crispin.getSurfaceWidth() / 2.0f) - (buttonSize / 2.0f) -
                buttonPadding - buttonSize, textPosY);
        newText.setColour(Colour.WHITE);

        openText = new Text(buttonFont, "OPEN", false, true,
                buttonSize);
        openText.setPosition((Crispin.getSurfaceWidth() / 2.0f) - (buttonSize / 2.0f),
                textPosY);
        openText.setColour(Colour.WHITE);

        buyText = new Text(buttonFont, "BUY", false, true,
                buttonSize);
        buyText.setPosition((Crispin.getSurfaceWidth() / 2.0f) +
                (buttonSize / 2.0f) + buttonPadding, textPosY);
        buyText.setColour(Colour.WHITE);

        newButton = new Button(new Texture(R.drawable.pencil_icon));
        newButton.setSize(new Scale2D(buttonSize, buttonSize));
        newButton.setPosition((Crispin.getSurfaceWidth() / 2.0f) - (buttonSize / 2.0f) -
                buttonPadding - buttonSize, buttonPosY);

        openButton = new Button(new Texture(R.drawable.folder_icon));
        openButton.setSize(new Scale2D(buttonSize, buttonSize));
        openButton.setPosition((Crispin.getSurfaceWidth() / 2.0f) - (buttonSize / 2.0f),
                buttonPosY);

        buyButton = new Button(new Texture(R.drawable.bank_card_icon));
        buyButton.setSize(new Scale2D(buttonSize, buttonSize));
        buyButton.setPosition((Crispin.getSurfaceWidth() / 2.0f) + (buttonSize / 2.0f) +
                buttonPadding, buttonPosY);

        logoImage.setOpacity(uiAlpha);
        newButton.setOpacity(uiAlpha);
        openButton.setOpacity(uiAlpha);
        buyButton.setOpacity(uiAlpha);
        newText.setOpacity(uiAlpha);
        openText.setOpacity(uiAlpha);
        buyText.setOpacity(uiAlpha);

        newButton.addTouchListener(e -> {
            switch (e.getEvent())
            {
                case CLICK:
                    newText.enableWiggle(40.0f, Text.WiggleSpeed_E.FAST);
                    holdingNewButton = true;
                    break;
                case RELEASE:
                    newText.disableWiggle();
                    holdingNewButton = false;
                    break;
            }
        });

        openButton.addTouchListener(e -> {
            switch (e.getEvent())
            {
                case CLICK:
                    openText.enableWiggle(40.0f, Text.WiggleSpeed_E.FAST);
                    holdingOpenButton = true;
                    fadeIn = false;
                    break;
                case RELEASE:
                    openText.disableWiggle();
                    holdingOpenButton = false;
                    break;
            }
        });

        buyButton.addTouchListener(e -> {
            switch (e.getEvent())
            {
                case CLICK:
                    buyText.enableWiggle(40.0f, Text.WiggleSpeed_E.FAST);
                    holdingBuyButton = true;
                    break;
                case RELEASE:
                    buyText.disableWiggle();
                    holdingBuyButton = false;
                    break;
            }
        });
    }

    void handleNewButtonColour()
    {
        if(holdingNewButton)
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
        if(holdingOpenButton)
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
        if(holdingBuyButton)
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
    public void update(float deltaTime) {
        handleNewButtonColour();
        handleOpenButtonColour();
        handleBuyButtonColour();

        if(fadeIn)
        {
            if(uiAlpha >= 1.0f)
            {
                uiAlpha = 1.0f;
            }
            else
            {
                uiAlpha += 0.05f;
            }
        }
        else
        {
            if(uiAlpha <= 0.0f)
            {
                uiAlpha = 0.0f;

                Crispin.setScene(TestScene::new);
            }
            else
            {
                uiAlpha -= 0.05f;
            }
        }


        logoImage.setOpacity(uiAlpha);
        newButton.setOpacity(uiAlpha);
        openButton.setOpacity(uiAlpha);
        buyButton.setOpacity(uiAlpha);
        newText.setOpacity(uiAlpha);
        openText.setOpacity(uiAlpha);
        buyText.setOpacity(uiAlpha);
    }

    @Override
    public void render() {
        logoImage.draw(uiCamera);
        newButton.draw(uiCamera);
        openButton.draw(uiCamera);
        buyButton.draw(uiCamera);
        newText.draw(uiCamera);
        openText.draw(uiCamera);
        buyText.draw(uiCamera);
    }

    @Override
    public void touch(int type, Point2D position) {

    }
}
