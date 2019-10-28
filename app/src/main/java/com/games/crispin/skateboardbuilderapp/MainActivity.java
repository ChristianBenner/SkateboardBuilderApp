package com.games.crispin.skateboardbuilderapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

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

        setContentView(frameLayout);
    }
}