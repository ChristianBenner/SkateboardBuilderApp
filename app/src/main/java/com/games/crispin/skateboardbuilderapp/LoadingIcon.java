package com.games.crispin.skateboardbuilderapp;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Geometry.Scale3D;
import com.games.crispin.crispinmobile.Rendering.Utilities.ModelMatrix;
import com.games.crispin.crispinmobile.Geometry.Scale2D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Rendering.Models.Square;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Material;
import com.games.crispin.crispinmobile.Rendering.Utilities.Texture;

public class LoadingIcon
{
    private Square square;
    private ModelMatrix modelMatrix;
    private float rotationAngle;
    private float rotationTime;
    private static final float DEFAULT_ROT_SPEED = 3.5f;

    public LoadingIcon()
    {
        square = new Square(new Material(new Texture(R.drawable.load_icon)), true);
        modelMatrix = new ModelMatrix();
        rotationAngle = 0.0f;
        rotationTime = 0.0f;
    }

    public void setColour(Colour colour)
    {
        square.getMaterial().setColour(colour);
    }

    public Colour getColour()
    {
        return square.getMaterial().getColour();
    }

    public void setPosition(Point2D position)
    {
        square.setPosition(position);
    }

    public Point2D getPosition()
    {
        return square.getPosition();
    }

    public void setSize(Scale2D size)
    {
        square.setScale(size);
    }

    public Scale2D getSize()
    {
        return square.getScale();
    }

    public void update(float deltaTime)
    {
        modelMatrix.reset();
        modelMatrix.translate(new Point3D((Crispin.getSurfaceWidth() / 2.0f) - 100.0f, (Crispin.getSurfaceHeight() / 2.0f) - 100.0f, 0.0f));
        modelMatrix.rotateAroundPoint(new Point3D(100.0f, 100.0f, 0.0f), rotationAngle, 0.0f, 0.0f, 1.0f);
        modelMatrix.scale(new Scale3D(200.0f, 200.0f, 1.0f));

        rotationTime += 0.07f * deltaTime;
        rotationAngle += DEFAULT_ROT_SPEED + (((Math.sin(rotationTime) + 1) / 2.0f) * 5.0f);
    }

    public void draw(Camera2D camera2D)
    {
        square.render(camera2D, modelMatrix);
    }
}
