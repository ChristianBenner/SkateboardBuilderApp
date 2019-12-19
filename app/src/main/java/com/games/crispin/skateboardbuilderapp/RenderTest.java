package com.games.crispin.skateboardbuilderapp;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Geometry.Scale3D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Rendering.Models.Cube;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera3D;
import com.games.crispin.crispinmobile.Rendering.Utilities.ModelMatrix;
import com.games.crispin.crispinmobile.Rendering.Utilities.RenderObject;
import com.games.crispin.crispinmobile.Utilities.Scene;

public class RenderTest extends Scene
{
    private RenderObject renderObject;
    private ModelMatrix matrix;
    private Camera3D camera3D;

    public RenderTest()
    {
        Crispin.setBackgroundColour(Colour.YELLOW);

        camera3D = new Camera3D();
        camera3D.setPosition(new Point3D(1.0f, 0.0f, 6.0f));

        renderObject = new Cube();
        renderObject.setColour(Colour.RED);
        renderObject.setPosition(0.0f, 0.0f);

        matrix = new ModelMatrix();
    }

    float angle = 0.0f;
    @Override
    public void update(float deltaTime)
    {
        angle += 0.5f;
       // renderObject.setRotationAroundPoint(new Point3D(1.0f, 0.0f, 1.0f), angle, 0.0f, 1.0f, 0.0f);
    }

    @Override
    public void render()
    {
        matrix.reset();

      //  matrix.translate(new Point3D(1.0f, 0.0f, 0.0f));
        matrix.rotateAroundPoint(new Point3D(1.0f, 1.0f, 1.0f), angle, 0.0f, 0.0f, 1.0f);

        renderObject.render(camera3D, matrix);
    }



    @Override
    public void touch(int type, Point2D position)
    {

    }
}
