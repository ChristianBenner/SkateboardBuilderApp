package com.games.crispin.skateboardbuilderapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.games.crispin.crispinmobile.Crispin;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // The application context
        final Context context = super.getApplicationContext();

        // Frame layout that the views will be added to (CrispinMobile and Android UI)
        FrameLayout frameLayout = new FrameLayout(context);

        // Add graphical view to frame layout
        Crispin.init(this, frameLayout, () -> new TestScene());

        // Linear Layout for Android UI components
        LinearLayout linearLayout = new LinearLayout(context);

        // Add button to linear layout
        Button button = new Button(context);
        button.setText("Test Button");
        linearLayout.addView(button);

        // Add linear layout to application
        frameLayout.addView(linearLayout);

        setContentView(frameLayout);
    }
}