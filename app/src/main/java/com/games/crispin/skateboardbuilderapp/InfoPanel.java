package com.games.crispin.skateboardbuilderapp;

import android.view.MotionEvent;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Scale2D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.games.crispin.crispinmobile.UserInterface.Font;
import com.games.crispin.crispinmobile.UserInterface.Button;
import com.games.crispin.crispinmobile.UserInterface.Plane;
import com.games.crispin.crispinmobile.UserInterface.Text;
import com.games.crispin.crispinmobile.UserInterface.TouchEvent;

import java.util.EventListener;

public class InfoPanel
{
    public interface VisibilityListener extends EventListener
    {
        void onShow();
        void onHide();
    }

    private VisibilityListener visibilityListener;

    private static final Point2D TEXT_PADDING = new Point2D(25.0f, 50.0f);
    private static final Point2D PANEL_PADDING = new Point2D(50.0f, 0.0f);

    private Plane backgroundHider;
    private Plane infoPanel;
    private Text infoPanelTitle;
    private Text infoText;
    private Button closeButton;
    private boolean visible;

    private Font infoFont;

    public InfoPanel()
    {
        visible = false;

        backgroundHider = new Plane(new Point2D(0.0f, 0.0f), new Scale2D(
                Crispin.getSurfaceWidth(), Crispin.getSurfaceHeight()));
        backgroundHider.setColour(new Colour(0.2f, 0.2f, 0.2f, 0.5f));

        Font titleFont = new Font(R.raw.aileron_regular, 76);
        infoFont = new Font(R.raw.aileron_regular, 48);

        infoPanelTitle = new Text(titleFont, "Information", false,
                true, Crispin.getSurfaceWidth() - (PANEL_PADDING.x * 2.0f) -
                (TEXT_PADDING.x * 2.0f));
        infoPanelTitle.setColour(Colour.WHITE);
        infoText = new Text(infoFont, "No info", true, true,
                Crispin.getSurfaceWidth() - (PANEL_PADDING.x * 2.0f) -
                        (TEXT_PADDING.x * 2.0f));
        infoText.setColour(Colour.WHITE);
        infoPanel = new Plane();
        closeButton = new Button(R.drawable.close_icon);
        closeButton.setColour(Colour.WHITE);
        closeButton.setEnabled(visible);
        closeButton.addTouchListener(e ->
        {
            // If clicked then released, hide
            if(e.getEvent() == TouchEvent.Event.RELEASE)
            {
                hide();
            }
        });

        positionUI();
    }

    public void setVisibilityListener(VisibilityListener visibilityListener)
    {
        this.visibilityListener = visibilityListener;
    }

    private void positionUI()
    {
        float panelHeight = TEXT_PADDING.y + infoPanelTitle.getHeight() +
                TEXT_PADDING.y + infoText.getHeight() + TEXT_PADDING.y;

        Scale2D panelSize = new Scale2D(Crispin.getSurfaceWidth() - PANEL_PADDING.x * 2.0f,
                panelHeight);
        Point2D panelPosition = new Point2D(PANEL_PADDING.x, (Crispin.getSurfaceHeight() / 2.0f) -
                (panelSize.y / 2.0f));
        infoPanel.setPosition(panelPosition);
        infoPanel.setSize(panelSize);
        infoPanel.setColour(Constants.BACKGROUND_COLOR);

        infoPanelTitle.setPosition(panelPosition.x + TEXT_PADDING.x, panelPosition.y + panelSize.y -
                TEXT_PADDING.y - infoPanelTitle.getHeight());
        infoText.setPosition(panelPosition.x + TEXT_PADDING.x, panelPosition.y + panelSize.y -
                TEXT_PADDING.y - infoPanelTitle.getHeight() - TEXT_PADDING.y - infoText.getHeight());

        Point2D closeButtonPadding = new Point2D(25.0f, 25.0f);
        closeButton.setSize(120.0f, 120.0f);
        closeButton.setPosition(panelPosition.x + panelSize.x - closeButton.getSize().x -
                closeButtonPadding.x, panelPosition.y + panelSize.y - closeButton.getSize().y -
                closeButtonPadding.y);
    }

    public void setText(String text)
    {
        infoText.setText(text);
        positionUI();
    }

    public void draw(Camera2D uiCamera)
    {
        if(visible)
        {
            backgroundHider.draw(uiCamera);
            infoPanel.draw(uiCamera);
            infoPanelTitle.draw(uiCamera);
            infoText.draw(uiCamera);
            closeButton.draw(uiCamera);
        }
    }

    public void touch(int type, Point2D position)
    {
        if(visible && type == MotionEvent.ACTION_UP)
        {
            // Check if the pointer intercepts the information box, if it doesn't then hide the info
            if(!(position.x > infoPanel.getPosition().x && position.x < infoPanel.getPosition().x +
                    infoPanel.getSize().x && position.y > infoPanel.getPosition().y && position.y <
                    infoPanel.getPosition().y + infoPanel.getSize().y))
            {
                // Not clicked on so hide
                hide();
            }
        }
    }

    public void hide()
    {
        visible = false;
        closeButton.setEnabled(false);

        if(visibilityListener != null)
        {
            visibilityListener.onHide();
        }
    }

    public void show()
    {
        visible = true;
        closeButton.setEnabled(true);

        if(visibilityListener != null)
        {
            visibilityListener.onShow();
        }
    }

    public boolean isVisible()
    {
        return visible;
    }
}
