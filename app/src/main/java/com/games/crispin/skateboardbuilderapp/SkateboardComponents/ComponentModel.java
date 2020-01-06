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

    // Model matrix for the transformations of the model
    private ModelMatrix modelMatrix;

    /**
     * Create the component model object. This will apply an initial position, rotation and scale to
     * the model.
     *
     * @param model     The model to assign to the component
     * @param position  The position of the component
     * @param rotationY The Y-axis rotation of the object (not the rotation around the center point)
     * @param rotationZ The Z-axis rotation of the object (not the rotation around the center point)
     * @param scale     The scale of the model
     * @since           1.0
     */
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

    /**
     * Set the texture of the model by supplying the textures resource ID
     *
     * @param resourceId    The resource ID of the texture
     * @since               1.0
     */
    public void setTexture(int resourceId)
    {
        model.setMaterial(new Material(resourceId));
    }

    /**
     * Update the transformations of model matrix by re-applying the new rotations from the touch
     * rotation object.
     *
     * @param touchRotation Touch rotation object that contains the rotations to apply to the model
     * @since               1.0
     */
    public void update(TouchRotation touchRotation)
    {
        // Reset the model matrix with a new identity
        modelMatrix.reset();

        // Translate the model away from the origin
        modelMatrix.translate(position);

        // Rotate the model around the center of the 3D space on the Y axis
        modelMatrix.rotateAroundPoint(-position.x, -position.y, -position.z,
                touchRotation.getRotationX(), 0.0f, 1.0f, 0.0f);

        // Rotate the model around the center of the 3D space on teh Z axis
        modelMatrix.rotateAroundPoint(-position.x, -position.y, -position.z,
                touchRotation.getRotationY(), 1.0f, 0.0f, 0.0f);

        // If the Y rotation is not 0, rotate the matrix. This is because there is a bug with the
        // Android matrix code where if you rotate by 0 degrees the matrix is broken and no model
        // appears.
        if(rotationY != 0.0f)
        {
            modelMatrix.rotate(rotationY, 0.0f, 1.0f, 0.0f);
        }

        // If the Z rotation is not 0, rotate the matrix.
        if(rotationZ != 0.0f)
        {
            modelMatrix.rotate(rotationZ, 0.0f, 0.0f, 1.0f);
        }

        // Scale the model to the correct size
        modelMatrix.scale(scale);
    }

    /**
     * Render the skateboard component using an existing camera
     *
     * @param camera3D  The 3D camera object used to render models in a 3-dimensional space
     * @since           1.0
     */
    public void render(Camera3D camera3D)
    {
        model.render(camera3D, modelMatrix);
    }
}