package com.games.crispin.skateboardbuilderapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.skateboardbuilderapp.Scenes.HomeScene;

/**
 * The MainActivity class is what the runtime environment looks for when starting the program. It
 * is derived from the Android class AppCompatActivity.
 *
 * @see         AppCompatActivity
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
public class MainActivity extends AppCompatActivity
{
    /**
     * This function is called when the activity is created. It is part of the Android activity
     * lifecycle controlled by the Android system. It is a function that overrides from the base
     * class.
     *
     * @param savedInstanceState    The saved instance state bundle
     * @since 1.0
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Request that the application does not have a title
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Hide the action bar at the top of the application
        getSupportActionBar().hide();

        // Add graphical view to frame layout
        Crispin.init(this, () -> new HomeScene());
    }
}