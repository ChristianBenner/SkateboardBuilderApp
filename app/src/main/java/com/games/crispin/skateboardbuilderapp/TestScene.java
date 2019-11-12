package com.games.crispin.skateboardbuilderapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Xml;
import android.view.View;
import android.widget.Button;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera3D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Material;
import com.games.crispin.crispinmobile.Rendering.Utilities.RenderObject;
import com.games.crispin.crispinmobile.Rendering.Utilities.Texture;
import com.games.crispin.crispinmobile.Utilities.OBJModelLoader;
import com.games.crispin.crispinmobile.Utilities.Scene;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class TestScene extends Scene {
    private Camera3D camera3D;
    private RenderObject palaceDeck;
    private RenderObject palaceGrip;

    private boolean renderGrip;

    private boolean spinning = false;
    private boolean increaseSpin = false;
    private float spinAdd = 0.0f;

    Material grip2;

    boolean gripOne = true;

    int deckMaterialIndex = 0;
    int gripMaterialIndex = 0;
    ArrayList<Material> deckMaterials;
    ArrayList<Material> gripMaterials;

    void addDeckToList(int resourceId)
    {
        Material m = new Material(new Texture(resourceId));
        m.setSpecularMap(new Texture(R.drawable.no_spec_map));
        deckMaterials.add(m);
    }

    void addGripToList(int resourceId)
    {
        Material m = new Material(new Texture(resourceId));
        m.setSpecularMap(new Texture(R.drawable.no_spec_map));
        gripMaterials.add(m);
    }

    Material nextDeckMaterial()
    {
        if(deckMaterialIndex >= deckMaterials.size())
        {
            deckMaterialIndex = 0;
        }

        Material m = deckMaterials.get(deckMaterialIndex);

        skateboards.get(0).setDeck(deckMaterialIndex);
        writeSave();

        deckMaterialIndex++;
        return m;
    }

    Material nextGripMaterial()
    {
        if(gripMaterialIndex >= gripMaterials.size())
        {
            gripMaterialIndex = 0;
        }

        Material m = gripMaterials.get(gripMaterialIndex);

        skateboards.get(0).setGrip(gripMaterialIndex);
        writeSave();

        gripMaterialIndex++;
        return m;
    }

    void writeBoardToXML(Skateboard skateboard)
    {
        try
        {
            FileOutputStream fileOutputStream = Crispin.getApplicationContext().openFileOutput("saves.xml", Context.MODE_PRIVATE);
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fileOutputStream, "UTF-8");
            serializer.startDocument("UTF-8", true);
            serializer.startTag("", "saves");
            serializer.startTag("", "skateboard");
            serializer.attribute("", "deck", Integer.toString(skateboard.getDeck()));
            serializer.attribute("", "grip", Integer.toString(skateboard.getGrip()));
            serializer.attribute("", "trucks", Integer.toString(skateboard.getTrucks()));
            serializer.attribute("", "bearings", Integer.toString(skateboard.getBearings()));
            serializer.attribute("", "wheels", Integer.toString(skateboard.getWheels()));
            serializer.attribute("", "name", skateboard.getName());
            serializer.endTag("", "skateboard");
            serializer.endTag("", "saves");
            serializer.endDocument();
            serializer.flush();
            fileOutputStream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    void writeSave()
    {
        try
        {
            FileOutputStream fileOutputStream = Crispin.getApplicationContext().openFileOutput("saves.xml", Context.MODE_PRIVATE);
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fileOutputStream, "UTF-8");
            serializer.startDocument("UTF-8", true);
            serializer.startTag("", "saves");

            // Write skateboard XML
            for(Skateboard s : skateboards)
            {
                serializer.startTag("", "skateboard");
                serializer.attribute("", "deck", Integer.toString(s.getDeck()));
                serializer.attribute("", "grip", Integer.toString(s.getGrip()));
                serializer.attribute("", "trucks", Integer.toString(s.getTrucks()));
                serializer.attribute("", "bearings", Integer.toString(s.getBearings()));
                serializer.attribute("", "wheels", Integer.toString(s.getWheels()));
                serializer.attribute("", "name", s.getName());
                serializer.endTag("", "skateboard");
            }

            serializer.endTag("", "saves");
            serializer.endDocument();
            serializer.flush();
            fileOutputStream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    void readBoardFromXML(String filename)
    {
        try
        {
            FileInputStream fileInputStream = Crispin.getApplicationContext().openFileInput(filename);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            char[] inputBuffer = new char[fileInputStream.available()];
            inputStreamReader.read(inputBuffer);

            String data = new String(inputBuffer);

            inputStreamReader.close();
            fileInputStream.close();

            System.out.println("FILE: " + data);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    List<Skateboard> skateboards = null;
    private boolean loadSaves()
    {
        try
        {
            skateboards = SaveReader.parse(Crispin.getApplicationContext().openFileInput("saves.xml"));
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public TestScene(View view)
    {
        Crispin.setBackgroundColour(new Colour(0.4f, 0.78f, 0.843f));

        // Get application preferences (contains settings)
        SharedPreferences preferences = Crispin.getApplicationContext().
                getSharedPreferences("app_pref",0);
        SharedPreferences.Editor preferencesEditor = preferences.edit();

        float fps = preferences.getFloat("fps", 60.0f);
        System.out.println("FPS: " + fps);
        preferencesEditor.putFloat("fps", 60.0f);
        preferencesEditor.commit();


/*
     //   writeBoardToXML(new Skateboard());
        readBoardFromXML("saves.xml");

        if(loadSaves())
        {
            for(Skateboard s : skateboards)
            {
                System.out.println(s.getName() + " {");
                System.out.println("\tDeck: " + s.getDeck());
                System.out.println("\tGrip: " + s.getGrip());
                System.out.println("\tTrucks: " + s.getTrucks());
                System.out.println("\tBearings: " + s.getBearings());
                System.out.println("\tWheels: " + s.getWheels());
                System.out.println("}");
            }
        }

        // Add some example boards
        skateboards.clear();
        skateboards.add(new Skateboard("Cool", 2, 5, 1, 6, 9));
        skateboards.add(new Skateboard("Wow", 6, 4, 2, 4, 2));
        skateboards.add(new Skateboard("Epic", 3, 8, 4, 1, 8));
        skateboards.add(new Skateboard("Mean", 1, 3, 3, 7, 4));

        writeSave();
*/
      /*  skateboards = new ArrayList<>();
        skateboards.clear();
        skateboards.add(new Skateboard("Cool", 0, 0, 1, 6, 9));
        writeSave();*/
        if(loadSaves())
        {
            deckMaterialIndex = skateboards.get(0).getDeck();
            gripMaterialIndex = skateboards.get(0).getGrip();
        }

        deckMaterials = new ArrayList<>();
        addDeckToList(R.drawable.jart_new_wave);
        addDeckToList(R.drawable.palacedeck);
        addDeckToList(R.drawable.primitive_rodriquez_thorns);
        addDeckToList(R.drawable.primitive_x_rick_and_morty);
        addDeckToList(R.drawable.real_wair_flooded);

        gripMaterials = new ArrayList<>();
        addGripToList(R.drawable.grip);
        addGripToList(R.drawable.grip2);
        addGripToList(R.drawable.grip3);
        addGripToList(R.drawable.grip4);
        addGripToList(R.drawable.grip5);

        renderGrip = true;

        camera3D = new Camera3D();
        camera3D.setPosition(new Point3D(0.0f, 0.0f, 7.0f));

        palaceDeck = OBJModelLoader.readObjFile(R.raw.deck8_125_uv_test);
        palaceDeck.setScale(0.15f, 0.15f, 0.15f);
        palaceDeck.setMaterial(nextDeckMaterial());
        palaceDeck.setPosition(new Point3D(0.0f, -5f, 0.0f));

        palaceGrip = OBJModelLoader.readObjFile(R.raw.grip8_125_4);
        palaceGrip.setScale(0.15f, 0.15f, 0.15f);
        palaceGrip.setMaterial(nextGripMaterial());
        palaceGrip.setPosition(new Point3D(0.0f, -5f, 0.0f));

        Button b1 = view.findViewById(R.id.button);
        b1.setOnClickListener(v ->
        {
            if(!spinning)
            {
                // Toggle grip tape
                spinning = true;
                spinAdd = 0.0f;
                increaseSpin = true;

                if(!renderGrip)
                {
                    renderGrip = true;
                }
            }
        });

        Button b2 = view.findViewById(R.id.button2);
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                palaceGrip.setMaterial(nextGripMaterial());
            }
        });

        Button b3 = view.findViewById(R.id.button3);
        b3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                palaceDeck.setMaterial(nextDeckMaterial());
            }
        });
    }

    float angle = 0.0f;

    float maxSpinAdd = 10.0f;
    boolean fadingIn = false;

    float changeMatTime = 0.0f;

    float alphaGrip = 1.0f;

    @Override
    public void update(float deltaTime) {
        angle += 2.0f;

/*        changeMatTime += 1f;
        if(changeMatTime > 10.0f)
        {
            changeMatTime = 0.0f;
            palaceDeck.setMaterial(nextMaterial());
        }*/


        if(spinning)
        {
            if(spinAdd >= maxSpinAdd)
            {
                increaseSpin = false;

                if(!fadingIn)
                {
                    alphaGrip = 0.0f;
                    renderGrip = false;
                }
                else
                {
                    alphaGrip = 1.0f;
                }

                fadingIn = !fadingIn;
            }

            if(increaseSpin)
            {
                spinAdd += 0.1f;

                float val = ((spinAdd+1.0f) / 2.0f)/maxSpinAdd;

                if(!fadingIn)
                {
                    alphaGrip = 1.0f - val;
                }
                else
                {
                    alphaGrip = val;
                }
            }
            else
            {
                spinAdd -= 0.1f;
            }


            if(spinAdd <= 0.0f)
            {
                spinAdd = 0.0f;
                spinning = false;
            }

            angle += spinAdd;
        }

        palaceDeck.setRotation(angle, 90.0f, 90.0f);
        palaceGrip.setRotation(angle, 90.0f, 90.0f);
        palaceGrip.getMaterial().getColour().setAlpha(alphaGrip);
    }

    @Override
    public void render()
    {
        palaceDeck.render(camera3D);

        if(renderGrip)
        {
            palaceGrip.render(camera3D);
        }
    }
}
