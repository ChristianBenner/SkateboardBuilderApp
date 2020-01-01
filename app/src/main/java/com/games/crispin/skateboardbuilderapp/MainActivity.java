package com.games.crispin.skateboardbuilderapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.skateboardbuilderapp.Scenes.HomeScene;
import com.games.crispin.skateboardbuilderapp.Scenes.SelectDeckWidthScene;
import com.games.crispin.skateboardbuilderapp.Scenes.ViewBoardScene;

public class MainActivity extends AppCompatActivity
{
    private String m_Text = "";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        // Add graphical view to frame layout
        Crispin.init(this, () -> new HomeScene());
    }
}