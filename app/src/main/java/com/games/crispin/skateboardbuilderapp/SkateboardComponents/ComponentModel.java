package com.games.crispin.skateboardbuilderapp.SkateboardComponents;

import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera3D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Material;
import com.games.crispin.crispinmobile.Rendering.Utilities.Model;
import com.games.crispin.crispinmobile.Rendering.Utilities.ModelMatrix;
import com.games.crispin.skateboardbuilderapp.UserInterface.TouchRotation;

/**
 * The component model class is designed to help render and handle models of skateboard components.
 * This involves the positioning and rotating of them. The primary use of the class currently is in
 * the <code>ViewBoardScene</code> and <code>OpenSaveScene</code> because there is multiple 3D
 * component models and they all need to be handled in one place. It helps reduce the amount of
 * code in the application.
 *
 * @see         com.games.crispin.skateboardbuilderapp.Scenes.ViewBoardScene
 * @see         com.games.crispin.skateboardbuilderapp.Scenes.OpenSaveScene
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
public class ComponentModel
{
    // The model of the skateboard component
    private Model model;

    // The position of the model
    private Point3D position;

    // The Y-axis rotation amount
    private float rotationY;

    // The X-axis rotation amount
    private float rotationZ;

    // The scale of the model
    private float scale;

    private ModelMatrix modelMatrix;

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