package com.games.crispin.skateboardbuilderapp.UserInterface;

import java.util.EventListener;

/**
 * A visibility listener interface for user interface objects that changes its visibility state
 *
 * @see         InfoPanel
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
public interface VisibilityListener extends EventListener
{
    /**
     * A method that gets called when a user interfaces visibility changes to shown
     *
     * @since   1.0
     */
    void onShow();

    /**
     * A method that gets called when a user interfaces visibility changes to hidden
     *
     * @since   1.0
     */
    void onHide();
}