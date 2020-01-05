package com.games.crispin.skateboardbuilderapp.SkateboardComponents;

import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera3D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Material;
import com.games.crispin.crispinmobile.Rendering.Utilities.Model;
import com.games.crispin.crispinmobile.Rendering.Utilities.ModelMatrix;
import com.games.crispin.crispinmobile.Rendering.Utilities.Texture;
import com.games.crispin.skateboardbuilderapp.TouchRotation;

public class ComponentModel
{
    private Model model;
    private Point3D position;
    private ModelMatrix modelMatrix;
    private float rotationY;
    private float rotationZ;
    private float scale;

    public ComponentModel(Model model,
                          Point3D position,
                          float rotationY,
                          float rotationZ,
                          float scale)
    {
        this.model = model;
        this.position = position;
        this.rotationY = rotationY;
        this.rotationZ = rotationZ;
        this.scale = scale;

        modelMatrix = new ModelMatrix();
    }

    public void setTexture(int resourceId)
    {
        model.setMaterial(new Material(resourceId));
    }

    public void update(TouchRotation touchRotation)
    {
        modelMatrix.reset();
        modelMatrix.translate(position);
        modelMatrix.rotateAroundPoint(-position.x, -position.y, -position.z,
                touchRotation.getRotationX(), 0.0f, 1.0f, 0.0f);
        modelMatrix.rotateAroundPoint(-position.x, -position.y, -position.z,
                touchRotation.getRotationY(), 1.0f, 0.0f, 0.0f);

        if(rotationY != 0.0f)
        {
            modelMatrix.rotate(rotationY, 0.0f, 1.0f, 0.0f);
        }

        if(rotationZ != 0.0f)
        {
            modelMatrix.rotate(rotationZ, 0.0f, 0.0f, 1.0f);
        }

        modelMatrix.scale(scale);
    }

    public void render(Camera3D camera3D)
    {
        model.render(camera3D, modelMatrix);
    }
}