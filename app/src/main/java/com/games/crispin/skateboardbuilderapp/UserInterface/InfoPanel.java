package com.games.crispin.skateboardbuilderapp.UserInterface;

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
import com.games.crispin.skateboardbuilderapp.Constants;
import com.games.crispin.skateboardbuilderapp.R;

/**
 * An information panel user interface. The object draws a panel with some specified information
 * text and also darkens the background slightly.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
public class InfoPanel
{
    // Padding for the text objects. The x-padding is the offset from the sides of the panels. The
    // y-padding is the offset from the top of the panels and the other text components within the
    // panel.
    private static final Point2D TEXT_PADDING = new Point2D(25.0f, 50.0f);

    // The padding of the panel UI. The x-padding is the distance from the sides of the view.
    private static final Point2D PANEL_PADDING = new Point2D(50.0f, 0.0f);

    // The font for the information on the panel
    private Font infoFont;

    // The plane used to darken what is behind the information panel
    private Plane backgroundHider;

    // The information panel plane
    private Plane infoPanel;

    // The title of the information panel
    private Text infoPanelTitle;

    // The text of the information panel that holds the information contents
    private Text infoText;

    // The close button for the panel
    private Button closeButton;

    // The visibility state of the panel
    private boolean visible;

    // A visibility listener that listens to the change in state of the panels visibility
    private VisibilityListener visibilityListener;

    /**
     * Constructor the information panel and all of its internal user interface objects
     *
     * @since 1.0
     */
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

    /**
     * Set the visibility listener to be called when the visibility state of the panel changes.
     *
     * @param visibilityListener    The visibility listener to be set
     * @since 1.0
     */
    public void setVisibilityListener(VisibilityListener visibilityListener)
    {
        this.visibilityListener = visibilityListener;
    }

    /**
     * Set the information text for the panel. This causes the text to be re-generated
     *
     * @param text  The new text string that contains the information
     * @since 1.0
     */
    public void setText(String text)
    {
        infoText.setText(text);
        positionUI();
    }

    /**
     * Draw all of the internal user interface components
     *
     * @param uiCamera  The 2D camera to draw the internal user interface components
     *                  with
     * @since 1.0
     */
    public void draw(Camera2D uiCamera)
    {
        // If the visibility state is set to true then draw the components, otherwise do not
        if(visible)
        {
            backgroundHider.draw(uiCamera);
            infoPanel.draw(uiCamera);
            infoPanelTitle.draw(uiCamera);
            infoText.draw(uiCamera);
            closeButton.draw(uiCamera);
        }
    }

    /**
     * Touch method used to control the event of touching the background instead of the information
     * panel itself. This uses the touch event so that when the background is clicked the panel will
     * be hidden.
     *
     * @param type      The type of touch event
     * @param position  The position of the touch event
     * @since 1.0
     */
    public void touch(int type, Point2D position)
    {
        // If the panel is visible and the motion event represents the end of a user click, check
        // if the panel should be hidden
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

    /**
     * Hide the panel. This causes the visibility listeners <code>onHide</code> method to be called
     *
     * @see VisibilityListener
     * @since 1.0
     */
    public void hide()
    {
        visible = false;
        closeButton.setEnabled(false);

        // If the visibility listener exists, call the on hide method on it
        if(visibilityListener != null)
        {
            visibilityListener.onHide();
        }
    }

    /**
     * Show the panel. This causes the visibility listeners <code>onShow</code> method to be called
     *
     * @see VisibilityListener
     * @since 1.0
     */
    public void show()
    {
        visible = true;
        closeButton.setEnabled(true);

        // If the visibility listener exists, call the on show method on it
        if(visibilityListener != null)
        {
            visibilityListener.onShow();
        }
    }

    /**
     * Get the visibility state of the panel
     *
     * @since 1.0
     */
    public boolean isVisible()
    {
        return visible;
    }

    /**
     * Position the user interface on the information panel. This must happen when the text changes.
     *
     * @since 1.0
     */
    private void positionUI()
    {
        // Calculate the new height of the panel
        float panelHeight = TEXT_PADDING.y + infoPanelTitle.getHeight() +
                TEXT_PADDING.y + infoText.getHeight() + TEXT_PADDING.y;

        // Reposition and re-size the information panel
        Scale2D panelSize = new Scale2D(Crispin.getSurfaceWidth() - PANEL_PADDING.x * 2.0f,
                panelHeight);
        Point2D panelPosition = new Point2D(PANEL_PADDING.x,
                (Crispin.getSurfaceHeight() / 2.0f) - (panelSize.y / 2.0f));
        infoPanel.setPosition(panelPosition);
        infoPanel.setSize(panelSize);
        infoPanel.setColour(Constants.BACKGROUND_COLOR);

        // Reposition the title text
        infoPanelTitle.setPosition(panelPosition.x + TEXT_PADDING.x, panelPosition.y +
                panelSize.y - TEXT_PADDING.y - infoPanelTitle.getHeight());

        // Reposition the information text
        infoText.setPosition(panelPosition.x + TEXT_PADDING.x, panelPosition.y + panelSize.y -
                TEXT_PADDING.y - infoPanelTitle.getHeight() - TEXT_PADDING.y -
                infoText.getHeight());

        // Reposition and re-size the close button
        Point2D closeButtonPadding = new Point2D(25.0f, 25.0f);
        closeButton.setSize(120.0f, 120.0f);
        closeButton.setPosition(panelPosition.x + panelSize.x - closeButton.getSize().x -
                closeButtonPadding.x, panelPosition.y + panelSize.y - closeButton.getSize().y -
                closeButtonPadding.y);
    }
}
