package com.games.crispin.skateboardbuilderapp;

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

public class TestScene extends Scene {
    Camera3D camera3D;
    RenderObject obj;
    RenderObject obj2;
    RenderObject obj3;
    RenderObject obj4;
    public TestScene(View view)
    {
        Crispin.setBackgroundColour(new Colour(0.4f, 0.78f, 0.843f));

        camera3D = new Camera3D();
        camera3D.setPosition(new Point3D(0.0f, 0.0f, 7.0f));

        Material blue = new Material();
      //  blue.setColour(Colour.BLUE);
        blue.setTexture(new Texture(R.drawable.deck));
        blue.setSpecularMap(new Texture(R.drawable.specularmap));

        Material grip = new Material();
        //  blue.setColour(Colour.BLUE);
        grip.setTexture(new Texture(R.drawable.grip));
        grip.setSpecularMap(new Texture(R.drawable.specularmap));

        obj = OBJModelLoader.readObjFile(R.raw.deck8_125_4);
        obj.setScale(0.15f, 0.15f, 0.15f);
        obj.setMaterial(blue);
        obj.setPosition(new Point3D(0.0f, -3f, 0.0f));

        obj2 = OBJModelLoader.readObjFile(R.raw.grip8_125_4);
        obj2.setScale(0.15f, 0.15f, 0.15f);
        obj2.setMaterial(grip);
        obj2.setPosition(new Point3D(0.0f, -3f, 0.0f));


        Material blue2 = new Material();
        //  blue.setColour(Colour.BLUE);
        blue2.setTexture(new Texture(R.drawable.deck2));
        blue2.setSpecularMap(new Texture(R.drawable.specularmap));

        Material grip2 = new Material();
        //  blue.setColour(Colour.BLUE);
        grip2.setTexture(new Texture(R.drawable.grip2));
        grip2.setSpecularMap(new Texture(R.drawable.specularmap));

        obj3 = OBJModelLoader.readObjFile(R.raw.deck8_125_4);
        obj3.setScale(0.15f, 0.15f, 0.15f);
        obj3.setMaterial(blue2);
        obj3.setPosition(new Point3D(0.0f, -4.5f, 0.0f));

        obj4 = OBJModelLoader.readObjFile(R.raw.grip8_125_4);
        obj4.setScale(0.15f, 0.15f, 0.15f);
        obj4.setMaterial(grip2);
        obj4.setPosition(new Point3D(0.0f, -4.5f, 0.0f));

        Button b1 = view.findViewById(R.id.button);
        b1.setOnClickListener(v -> obj.setScale(0.1f, 0.1f, 0.1f));
    }

    float angle = 0.0f;

    @Override
    public void update(float deltaTime) {
        angle += 2.0f;
        obj.setRotation(angle, 90.0f, 90.0f);
        obj2.setRotation(angle, 90.0f, 90.0f);
        obj3.setRotation(angle, 90.0f, 90.0f);
        obj4.setRotation(angle, 90.0f, 90.0f);
    }

    @Override
    public void render() {
        obj.render(camera3D);
        obj2.render(camera3D);
        obj3.render(camera3D);
        obj4.render(camera3D);
    }
}
