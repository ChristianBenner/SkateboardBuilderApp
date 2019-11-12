package com.games.crispin.skateboardbuilderapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.games.crispin.crispinmobile.Crispin;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        // The application context
        final Context context = super.getApplicationContext();

        // Frame layout that the views will be added to (CrispinMobile and Android UI)
        FrameLayout frameLayout = new FrameLayout(context);

        // Create layout inflater (inflate XML layout to a view)
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);

        // Inflate home layout
        View homeLayout = inflater.inflate(R.layout.home_activity, null);

        // Add graphical view to frame layout
        Crispin.init(this, frameLayout, () -> new TestScene(homeLayout));

        frameLayout.addView(homeLayout);

        setContentView(frameLayout);
    }
}