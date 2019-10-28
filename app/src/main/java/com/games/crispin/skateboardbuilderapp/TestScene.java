package com.games.crispin.skateboardbuilderapp;

import android.view.View;
import android.widget.Button;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera3D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Material;
import com.games.crispin.crispinmobile.Rendering.Utilities.RenderObject;
import com.games.crispin.crispinmobile.Utilities.OBJModelLoader;
import com.games.crispin.crispinmobile.Utilities.Scene;

public class TestScene extends Scene {
    Camera3D camera3D;
    RenderObject obj;

    public TestScene(View view)
    {
        Crispin.setBackgroundColour(Colour.ORANGE);

        camera3D = new Camera3D();
        camera3D.setPosition(new Point3D(0.0f, 0.0f, 7.0f));

        Material blue = new Material();
        blue.setColour(Colour.BLUE);

        obj = OBJModelLoader.readObjFile(R.raw.skateboard);
        obj.setScale(0.06f, 0.06f, 0.06f);
        obj.setMaterial(blue);

        Button b1 = view.findViewById(R.id.button);
        b1.setOnClickListener(v -> obj.setScale(0.1f, 0.1f, 0.1f));
    }

    float angle = 0.0f;

    @Override
    public void update(float deltaTime) {
        angle += 1.0f;
        obj.setRotation(-90.0f + angle, 0.0f, 90.0f);
    }

    @Override
    public void render() {
        obj.render(camera3D);
    }
}
