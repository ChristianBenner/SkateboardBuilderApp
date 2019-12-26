package com.games.crispin.skateboardbuilderapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.opengl.Matrix;
import android.util.Xml;
import android.view.MotionEvent;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Model;
import com.games.crispin.crispinmobile.Rendering.Utilities.ModelMatrix;
import com.games.crispin.crispinmobile.Rendering.Utilities.RotationMatrix;
import com.games.crispin.crispinmobile.Geometry.Vector3D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.UserInterface.Border;
import com.games.crispin.crispinmobile.UserInterface.Button;
import com.games.crispin.crispinmobile.UserInterface.Dropdown;
import com.games.crispin.crispinmobile.UserInterface.Image;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera3D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Font;
import com.games.crispin.crispinmobile.Rendering.Utilities.Material;
import com.games.crispin.crispinmobile.Rendering.Utilities.RenderObject;
import com.games.crispin.crispinmobile.Rendering.Utilities.Texture;
import com.games.crispin.crispinmobile.UserInterface.TouchEvent;
import com.games.crispin.crispinmobile.UserInterface.TouchListener;
import com.games.crispin.crispinmobile.Utilities.OBJModelLoader;
import com.games.crispin.crispinmobile.Utilities.Scene;

import org.xmlpull.v1.XmlSerializer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TestScene extends Scene {
    private Camera3D camera3D;
    private Camera2D camera2D;

    private Model palaceDeck;
    private Model palaceGrip;

    private Image image;
    private Button button;
    private Button backButton;
    private Dropdown dropdown;

    private boolean renderGrip;

    private boolean spinning = false;
    private boolean increaseSpin = false;
    private float spinAdd = 0.0f;

    Material grip2;

    boolean gripOne = true;

    Model truck;
    ModelMatrix truckModelMatrix;

    int deckMaterialIndex = 0;
    int gripMaterialIndex = 0;
    ArrayList<Material> deckMaterials;
    ArrayList<Material> gripMaterials;

    private float alpha = 0.0f;
    private boolean fadeIn = true;

    private TouchRotation touchRotation;

    private Model truckOne;
    private Model truckTwo;
    private Model board;
    private Model griptape;

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
            skateboards = SaveReader.parse(Crispin.getApplicationContext().
                    openFileInput("saves.xml"));
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    //load currently

    boolean colourNormal = true;
    public TestScene()
    {
       // Crispin.setBackgroundColour(new Colour(0.4f, 0.78f, 0.843f));
      //  Crispin.setBackgroundColour(new Colour(0, 213, 213));

        // Mad blue
        Crispin.setBackgroundColour(new Colour(57, 142, 178));

        truckModelMatrix = new ModelMatrix();
        // Darker blue
        //Crispin.setBackgroundColour(new Colour(21, 61, 82));

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

        Border border = new Border(Colour.BLACK, 4);

        backButton = new Button(R.drawable.back_icon);
        backButton.setPosition(100.0f, Crispin.getSurfaceHeight() - 100.0f - 200.0f);
        backButton.addTouchListener(new TouchListener() {
            @Override
            public void touchEvent(TouchEvent e) {
                switch (e.getEvent())
                {
                    case CLICK:
                        fadeIn = false;
                        break;
                }
            }
        });

        button = new Button(R.drawable.pencil_icon);
       // button.setBorder(border);
        button.setPosition(100.0f, 100.0f);
        button.addTouchListener(new TouchListener() {
            @Override
            public void touchEvent(TouchEvent e) {
                switch (e.getEvent())
                {
                    case CLICK:
                        palaceDeck.setMaterial(nextDeckMaterial());
                        palaceGrip.setMaterial(nextGripMaterial());
                        break;
                }
            }
        });

        touchRotation = new TouchRotation();

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
        addDeckToList(R.drawable.palacedeck);
        addDeckToList(R.drawable.jart_new_wave);
        addDeckToList(R.drawable.primitive_rodriquez_thorns);
        addDeckToList(R.drawable.primitive_x_rick_and_morty);
        addDeckToList(R.drawable.real_wair_flooded);

        gripMaterials = new ArrayList<>();
        addGripToList(R.drawable.grip2);
        addGripToList(R.drawable.grip);

        renderGrip = true;

        camera3D = new Camera3D();
        camera3D.setPosition(new Point3D(0.0f, 0.0f, 7.0f));

        camera2D = new Camera2D(0.0f, 0.0f, Crispin.getSurfaceWidth(), Crispin.getSurfaceHeight());

        palaceDeck = OBJModelLoader.readObjFile(R.raw.deck8_125_uv_test);
        palaceDeck.setScale(0.2f, 0.2f, 0.2f);
        palaceDeck.setMaterial(deckMaterials.get(0));

        palaceGrip = OBJModelLoader.readObjFile(R.raw.grip8_125_4);
        palaceGrip.setScale(0.2f, 0.2f, 0.2f);
        palaceGrip.setMaterial(gripMaterials.get(0));

        backButton.setAlpha(0.0f);
        button.setAlpha(0.0f);
        palaceDeck.setAlpha(0.0f);
        palaceGrip.setAlpha(0.0f);
        palaceDeck.setRotation(0.0f, 90.0f, 90.0f);
        palaceGrip.setRotation(0.0f, 90.0f, 90.0f);
        Material grey = new Material(new Texture(R.drawable.grey));
        truck = OBJModelLoader.readObjFile(R.raw.trucktest);
      //  truck.setScale(0.6f, 0.6f, 0.6f);
        truck.setMaterial(grey);


        Material matTruckOne = new Material(new Texture(R.drawable.grey));
        Material matTruckTwo = new Material(new Texture(R.drawable.grey));

        truckOne = OBJModelLoader.readObjFile(R.raw.trucktest);
        truckTwo = OBJModelLoader.readObjFile(R.raw.trucktest);
        board = OBJModelLoader.readObjFile(R.raw.deck8_125_uv_test_2);
        griptape = OBJModelLoader.readObjFile(R.raw.grip8_125_4);
        truckOne.setMaterial(matTruckOne);
        truckTwo.setMaterial(matTruckTwo);
        board.setMaterial(new Material(new Texture(R.drawable.real_wair_flooded)));

        Material gripMat = new Material(new Texture(R.drawable.grip2));
      //  gripMat.setSpecularMap(new Texture(R.drawable.gripspecularmap));
        griptape.setMaterial(gripMat);
        truckOne.setColour(Colour.GREEN);
        truckTwo.setColour(Colour.ORANGE);

    }

    float angle = 0.0f;

    float time = 0.0f;

    float cx = 0.0f;
    @Override
    public void update(float deltaTime) {
        angle += 2.0f;

        if(fadeIn)
        {
            if(alpha >= 1.0f)
            {
                alpha = 1.0f;
            }
            else
            {
                alpha += 0.05f;
            }
        }
        else
        {
            if(alpha <= 0.0f)
            {
                Crispin.setScene(HomeScene::new);
            }
            else
            {
                alpha -= 0.05f;
            }
        }


        palaceDeck.setAlpha(alpha);
        palaceGrip.setAlpha(alpha);
        button.setAlpha(alpha);
        backButton.setAlpha(alpha);
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

        touchRotation.update(deltaTime);

        rm.reset();
        rm.applyRotation(new Vector3D(0.0f, 1.0f, 0.0f), touchRotation.getRotationX() + 45.0f);
        rm.applyRotation(new Vector3D(1.0f, 0.0f, 0.0f), touchRotation.getRotationY() + 270.0f);
     //   truck.setRotation(rm);

        truckModelMatrix.reset();
        float[] modelMatrix = truckModelMatrix.getModelMatrix();
        Matrix.rotateM(modelMatrix, 0, touchRotation.getRotationX(), modelMatrix[1], modelMatrix[5], modelMatrix[9]);
        Matrix.rotateM(modelMatrix, 0, touchRotation.getRotationY(), modelMatrix[0], modelMatrix[4], modelMatrix[8]);

     //   cx += 0.05f;
     //   camera3D.setPosition(new Point3D(5.0f * (float)Math.sin(cx), 0.0f, 5.0f * (float)Math.cos(cx)));
    //    camera3D.setDirection(new Vector3D(0.0f, 0.0f, -1.0f));

      //  truckModelMatrix.rotateAroundPoint(0.0f, 0.0f, 0.0f, rotationX + 45.0f,0.0f, 1.0f, 0.0f);
    //    truckModelMatrix.rotateAroundPoint(0.0f, 0.0f, 0.0f, rotationY + 270.0f, 1.0f, 0.0f, 0.0f);
        System.out.println("ooo rotx: " + touchRotation.getRotationX());
        System.out.println("ooo roty: " + touchRotation.getRotationY());


        float yAngle = 90.0f;

        // Update board pos
        truckOneModelMatrix.reset();
        truckOneModelMatrix.translate(0.0f, 0.0f, -1.5f);
        truckOneModelMatrix.rotateAroundPoint(0.0f, 0.0f, 1.5f, touchRotation.getRotationX(), 0.0f, 1.0f, 0.0f);
        truckOneModelMatrix.rotateAroundPoint(0.0f, 0.0f, 1.5f, touchRotation.getRotationY(), 1.0f, 0.0f, 0.0f);
        truckOneModelMatrix.scale(0.2f);

        truckTwoModelMatrix.reset();
        truckTwoModelMatrix.translate(0.0f, 0.0f, 1.5f);
        truckTwoModelMatrix.rotateAroundPoint(0.0f, 0.0f, -1.5f, touchRotation.getRotationX(), 0.0f, 1.0f, 0.0f);
        truckTwoModelMatrix.rotateAroundPoint(0.0f, 0.0f, -1.5f, touchRotation.getRotationY(), 1.0f, 0.0f, 0.0f);
        truckTwoModelMatrix.scale(0.2f);

        boardModelMatrix.reset();
        boardModelMatrix.rotate(touchRotation.getRotationX(), 0.0f, 1.0f, 0.0f);
        boardModelMatrix.rotate(touchRotation.getRotationY(), 1.0f, 0.0f, 0.0f);
        boardModelMatrix.scale(0.2f);

        griptapeModelMatrix.reset();
        griptapeModelMatrix.rotate(touchRotation.getRotationX(), 0.0f, 1.0f, 0.0f);
        griptapeModelMatrix.rotate(touchRotation.getRotationY(), 1.0f, 0.0f, 0.0f);
        griptapeModelMatrix.scale(0.2f);
    }

    private ModelMatrix truckOneModelMatrix = new ModelMatrix();
    private ModelMatrix truckTwoModelMatrix = new ModelMatrix();
    private ModelMatrix boardModelMatrix = new ModelMatrix();
    private ModelMatrix griptapeModelMatrix = new ModelMatrix();

    RotationMatrix rm = new RotationMatrix();

    @Override
    public void render()
    {
        float[] mm = new float[16];
        Matrix.setIdentityM(mm, 0);

        float[] rm = new float[16];
        Matrix.setIdentityM(rm, 0);
        Matrix.rotateM(rm, 0, touchRotation.getRotationX(), 0.0f, 1.0f, 0.0f);
        Matrix.rotateM(rm, 0, touchRotation.getRotationY(), 1.0f, 0.0f, 0.0f);

        float[] tempMatrix = new float[16];
        Matrix.multiplyMM(tempMatrix, 0, mm, 0, rm, 0);


   //     truck.render(camera3D, new ModelMatrix(tempMatrix));

        truckOne.render(camera3D, truckOneModelMatrix);
        truckTwo.render(camera3D, truckTwoModelMatrix);
        board.render(camera3D, boardModelMatrix);
        griptape.render(camera3D, griptapeModelMatrix);

        button.draw(camera2D);
        backButton.draw(camera2D);
     //  image.draw(camera2D);
       // dropdown.draw(camera2D);
    }



    @Override
    public void touch(int type, Point2D position)
    {
        touchRotation.touch(type, position);
    }
}
