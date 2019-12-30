package com.games.crispin.skateboardbuilderapp;

import android.view.MotionEvent;

import com.games.crispin.crispinmobile.Geometry.Point2D;

import java.lang.reflect.Array;

public class TouchRotation
{
    private static final int MAX_SAMPLES = 5;

    private float rotationX;
    private float rotationY;
    private Point2D lastPos;
    private boolean dragging;
    private Point2D difference;
    private float[] velocitySamplesX;
    private float[] velocitySamplesY;
    private Point2D averageVelocity;
    private int velocitySampleIndex;

    public TouchRotation(float startRotationX, float startRotationY)
    {
        rotationX = startRotationX;
        rotationY = startRotationY;
        lastPos = new Point2D();
        dragging = false;
        difference = new Point2D();
        velocitySamplesX = new float[MAX_SAMPLES];
        velocitySamplesY = new float[MAX_SAMPLES];
        averageVelocity = new Point2D();
        velocitySampleIndex = 0;
    }

    public TouchRotation()
    {
        this(0.0f, 0.0f);
    }

    public float getRotationX()
    {
        return rotationX;
    }

    public float getRotationY()
    {
        return rotationY;
    }

    public void update(float deltaTime)
    {
        if(dragging == false)
        {
            if(averageVelocity.x != 0 && averageVelocity.y != 0)
            {
                if(Math.abs(averageVelocity.x) <= 0.01f)
                {
                    averageVelocity.x = 0.0f;
                }

                if(Math.abs(averageVelocity.y) <= 0.01f)
                {
                    averageVelocity.y = 0.0f;
                }

                averageVelocity.x -= averageVelocity.x * 0.05f;
                averageVelocity.y -= averageVelocity.y * 0.05f;
                rotationX += averageVelocity.x;
                rotationY += averageVelocity.y;
            }
        }
    }

    public void touch(int type, Point2D position)
    {
        switch (type)
        {
            case MotionEvent.ACTION_DOWN:
                lastPos = position;
                dragging = true;

                // Set the samples back to 0
                for(int i = 0; i < MAX_SAMPLES; i++)
                {
                    velocitySamplesX[i] = 0.0f;
                    velocitySamplesY[i] = 0.0f;
                }
                velocitySampleIndex = 0;
            break;
            case MotionEvent.ACTION_UP:
                dragging = false;

                // Calculate mean velocity
                averageVelocity.x = 0.0f;
                averageVelocity.y = 0.0f;
                for(int i = 0; i < MAX_SAMPLES; i++)
                {
                    averageVelocity.x += velocitySamplesX[i];
                    averageVelocity.y += velocitySamplesY[i];
                }
                averageVelocity.x /= MAX_SAMPLES;
                averageVelocity.y /= MAX_SAMPLES;
                break;
            case MotionEvent.ACTION_MOVE:
                if(dragging)
                {
                    if(lastPos == null)
                    {
                        lastPos = position;
                    }

                    // Calculate the velocity vector
                    difference.x = (position.x - lastPos.x) * 0.2f;
                    difference.y = (position.y - lastPos.y) * 0.2f;

                    if(velocitySampleIndex >= MAX_SAMPLES)
                    {
                        velocitySampleIndex = 0;
                    }
                    velocitySamplesX[velocitySampleIndex] = difference.x;
                    velocitySamplesY[velocitySampleIndex] = difference.y;
                    velocitySampleIndex++;

                    rotationX += (position.x - lastPos.x) * 0.2f;
                    rotationY += (position.y - lastPos.y) * 0.2f;

                    lastPos = position;
                }
                break;
        }
    }
}
