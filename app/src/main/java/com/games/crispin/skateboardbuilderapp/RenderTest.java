package com.games.crispin.skateboardbuilderapp;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Geometry.Scale3D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Rendering.Models.Cube;
import com.games.crispin.crispinmobile.Rendering.Models.Square;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera3D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Material;
import com.games.crispin.crispinmobile.Rendering.Utilities.ModelMatrix;
import com.games.crispin.crispinmobile.Rendering.Utilities.RenderObject;
import com.games.crispin.crispinmobile.Rendering.Utilities.Texture;
import com.games.crispin.crispinmobile.Utilities.Scene;

public class RenderTest extends Scene
{
    private RenderObject renderObject;
    private ModelMatrix matrix;
    private Camera3D camera3D;

    private Camera2D camera2D;
    private RenderObject renderObject2d;

    private ModelMatrix matrix2d;

    public RenderTest()
    {
        Crispin.setBackgroundColour(Colour.DARK_GREY);

        camera3D = new Camera3D();
        camera3D.setPosition(new Point3D(1.0f, 0.0f, 6.0f));

        renderObject = new Cube();
        renderObject.setColour(Colour.RED);
        renderObject.setPosition(0.0f, 0.0f);

        matrix = new ModelMatrix();

        camera2D = new Camera2D(0.0f, 0.0f, Crispin.getSurfaceWidth(), Crispin.getSurfaceHeight());

        Material loadingIcon = new Material(new Texture(R.drawable.load_icon));
        renderObject2d = new Square(loadingIcon, true);

        matrix2d = new ModelMatrix();
    }

    float angle = 0.0f;
    float angle2 = 0.0f;
    @Override
    public void update(float deltaTime)
    {
        angle += 0.5f;
        angle2 += 7.5f;
       // renderObject.setRotationAroundPoint(new Point3D(1.0f, 0.0f, 1.0f), angle, 0.0f, 1.0f, 0.0f);
    }

    @Override
    public void render()
    {
        matrix.reset();

      //  matrix.translate(new Point3D(1.0f, 0.0f, 0.0f));
        matrix.rotateAroundPoint(new Point3D(1.0f, 1.0f, 1.0f), angle, 0.0f, 0.0f, 1.0f);

        matrix2d.reset();
        matrix2d.translate(new Point3D((Crispin.getSurfaceWidth() / 2.0f) - 100.0f, (Crispin.getSurfaceHeight() / 2.0f) - 100.0f, 0.0f));
        matrix2d.rotateAroundPoint(new Point3D(100.0f, 100.0f, 0.0f), angle2, 0.0f, 0.0f, 1.0f);
        matrix2d.scale(new Scale3D(200.0f, 200.0f, 1.0f));


        renderObject.render(camera3D, matrix);
        renderObject2d.render(camera2D, matrix2d);
    }



    @Override
    public void touch(int type, Point2D position)
    {

    }
}
