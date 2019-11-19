package com.games.crispin.skateboardbuilderapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.opengl.Matrix;
import android.util.Xml;
import android.view.View;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Geometry.Geometry;
import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Geometry.Vector2D;
import com.games.crispin.crispinmobile.Geometry.Vector3D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Rendering.UserInterface.Border;
import com.games.crispin.crispinmobile.Rendering.UserInterface.Button;
import com.games.crispin.crispinmobile.Rendering.UserInterface.Dropdown;
import com.games.crispin.crispinmobile.Rendering.UserInterface.Image;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera3D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Font;
import com.games.crispin.crispinmobile.Rendering.Utilities.Material;
import com.games.crispin.crispinmobile.Rendering.Utilities.RenderObject;
import com.games.crispin.crispinmobile.Rendering.Utilities.Texture;
import com.games.crispin.crispinmobile.Rendering.Utilities.TextureOptions;
import com.games.crispin.crispinmobile.Utilities.OBJModelLoader;
import com.games.crispin.crispinmobile.Utilities.Scene;

import org.xmlpull.v1.XmlSerializer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_LINEAR_MIPMAP_LINEAR;
import static android.opengl.GLES20.GL_NEAREST;

public class TestScene extends Scene {
    private Camera3D camera3D;
    private Camera2D camera2D;

    private RenderObject palaceDeck;
    private RenderObject palaceGrip;

    private Image image;
    private Button button;
    private Dropdown dropdown;

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

        if(skateboards != null && skateboards.isEmpty() == false)
        {
            skateboards.get(0).setDeck(deckMaterialIndex);
        }

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

        if(skateboards != null && skateboards.isEmpty() == false)
        {
            skateboards.get(0).setGrip(gripMaterialIndex);
        }

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

    public TestScene()
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

        Font f = new Font(R.raw.aileron_regular, 64);

        Texture t = new Texture(R.drawable.logo);
        float ratio = t.getHeight()/t.getWidth();
        float targetWidth = Crispin.getSurfaceWidth() - (Crispin.getSurfaceWidth() / 10.0f);
        image = new Image(R.drawable.logo, (int)targetWidth, (int)(targetWidth * ratio));
        image.setPosition((Crispin.getSurfaceWidth()/2.0f) - (targetWidth/2.0f), Crispin.getSurfaceHeight() - (targetWidth * ratio) - 40.0f);

        Border border = new Border(Colour.RED, 50);

        button = new Button(R.drawable.bank_card_icon);
        button.setBorder(border);
        button.setPosition(100.0f, 100.0f);
        button.setOpacity(0.5f);

        dropdown = new Dropdown();
        dropdown.addItem("Test Item");
        dropdown.addItem("Another Item");
        dropdown.addItem("Third Item");
        dropdown.addItem("Last Item");
        //dropdown.expand();
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

        renderGrip = true;

        camera3D = new Camera3D();
        camera3D.setPosition(new Point3D(0.0f, 0.0f, 7.0f));

        camera2D = new Camera2D(0.0f, 0.0f, Crispin.getSurfaceWidth(), Crispin.getSurfaceHeight());

        palaceDeck = OBJModelLoader.readObjFile(R.raw.deck8_125_uv_test);
        palaceDeck.setScale(0.15f, 0.15f, 0.15f);
        palaceDeck.setMaterial(deckMaterials.get(0));
        palaceDeck.setPosition(new Point3D(0.0f, -1f, 0.0f));

        palaceGrip = OBJModelLoader.readObjFile(R.raw.grip8_125_4);
        palaceGrip.setScale(0.15f, 0.15f, 0.15f);
        palaceGrip.setMaterial(gripMaterials.get(0));
        palaceGrip.setPosition(new Point3D(0.0f, -1f, 0.0f));

     //   palaceDeck.setRotation(0.0f, 90.0f, 90.0f);
     //   palaceGrip.setRotation(0.0f, 90.0f, 90.0f);
    }

    float angle = 0.0f;

    float time = 0.0f;

    @Override
    public void update(float deltaTime) {
        angle += 2.0f;
       // palaceDeck.setRotation(angle, 90.0f, 90.0f);
       // palaceGrip.setRotation(angle, 90.0f, 90.0f);

        time += 1.0f;
        if(time > 60.0f * 2.0f)
        {
            if(dropdown.isExpanded())
            {
                dropdown.collapse();
            }
            else
            {
                dropdown.expand();
            }
            time = 0.0f;
        }
    }

    float[] modelMatrix = new float[16];
    float[] resultMatrix = new float[16];
    float[] rotationMatrix = new float[16];
    float rotationX = 0.0f;
    float rotationY = 0.0f;

    float[] invertedRotation = new float[16];

    void applyRotation(Vector3D rotationAxis, float angle)
    {
        float[] calcAxis = new float[4];
        float[] axisOfRotation = { rotationAxis.x, rotationAxis.y, rotationAxis.z, 1.0f };
        Matrix.invertM(invertedRotation, 0, modelMatrix, 0);
        Matrix.multiplyMV(calcAxis, 0, invertedRotation, 0, axisOfRotation, 0);
        Matrix.rotateM(modelMatrix, 0, angle, calcAxis[0], calcAxis[1], calcAxis[2]);
    }

    @Override
    public void render()
    {
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.scaleM(modelMatrix, 0, palaceDeck.getScale().x, palaceDeck.getScale().y, palaceDeck.getScale().z);

        applyRotation(new Vector3D(0.0f, 1.0f, 0.0f), rotationX + 45.0f);
        applyRotation(new Vector3D(1.0f, 0.0f, 0.0f), rotationY + 270.0f);

        Matrix.translateM(modelMatrix,
                0, palaceDeck.getPosition().x, palaceDeck.getPosition().y, palaceDeck.getPosition().z);


        palaceDeck.renderTestModelMatrix(camera3D, modelMatrix);
        palaceGrip.renderTestModelMatrix(camera3D, modelMatrix);

        button.draw(camera2D);
        image.draw(camera2D);
        dropdown.draw(camera2D);
    }

    float dragAngleY = 0.0f;
    float dragAngleX = 0.0f;
    Point2D lastPos;

    @Override
    public void touch(int type, Point2D position)
    {
        if(type == 1)
        {
            lastPos = position;
        }

        if(type == 3)
        {
            // Dragging
            System.out.println("DRAGGING");

            boolean inv;

            if(lastPos == null)
            {
                lastPos = position;
            }

            rotationX += (position.x - lastPos.x) * 0.2f;
            rotationY += (position.y - lastPos.y) * 0.2f;


            lastPos = position;

           // palaceDeck.setRotation(dragAngleY, 90.0f + dragAngleX, 0.0f);
           // palaceGrip.setRotation(dragAngleY, 90.0f + dragAngleX, 0.0f);
        }
    }
}
