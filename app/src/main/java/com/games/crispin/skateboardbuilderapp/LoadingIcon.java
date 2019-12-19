package com.games.crispin.skateboardbuilderapp;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera3D;
import com.games.crispin.crispinmobile.Rendering.Utilities.RotationMatrix;
import com.games.crispin.crispinmobile.Geometry.Scale2D;
import com.games.crispin.crispinmobile.Geometry.Vector3D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Rendering.Models.Square;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Material;
import com.games.crispin.crispinmobile.Rendering.Utilities.Texture;

public class LoadingIcon
{
    private Square square;
    private RotationMatrix rotationMatrix;
    private float rotationAngle;

    public LoadingIcon()
    {
        square = new Square(new Material(new Texture(R.drawable.load_icon)), true);
        rotationMatrix = new RotationMatrix();
        rotationAngle = 0.0f;
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
     //   rotationMatrix.reset();
       // rotationMatrix.applyRotation(new Vector3D(0.0f, 0.0f, 1.0f), rotationAngle);
    //   square.setRotation(rotationMatrix);
       // square.setRotationAroundPoint(new Point3D(120.0f / Crispin.getSurfaceWidth(), 120.0f / Crispin.getSurfaceHeight(), 0.0f), rotationAngle, 0.0f, 0.0f, 1.0f);


        rotationAngle += 5f;
    }

    public void draw(Camera2D camera2D)
    {
        // Disable depth
        square.render(camera2D);
        // Re-enable depth
    }
}
