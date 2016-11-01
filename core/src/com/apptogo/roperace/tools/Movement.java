package com.apptogo.roperace.tools;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;

public class Movement
{
    private Vector3 target = new Vector3();
    private Vector3 start = new Vector3();

    private float STEP_SIZE;
    private float step = 0;

    private boolean run = false;

    private Interpolation interpolation;

    public Movement()
    {
        this(Interpolation.sine);
    }

    public Movement(Interpolation interpolation)
    {
        this(interpolation, 0.05f);
    }

    public Movement(Interpolation interpolation, float stepSize)
    {
        this.interpolation = interpolation;
        this.STEP_SIZE = stepSize;
    }

    public boolean isRun()
    {
        return this.run;
    }

    public void set(Vector3 start, Vector3 target)
    {
        this.start.set(start);
        this.target.set(target);
        this.step = 0;
        this.run = true;
    }

    public void nextStep()
    {
        this.step += this.STEP_SIZE;
    }

    public Vector3 getCurrent()
    {
        if (this.complete())
        {
            Vector3 temp = new Vector3(this.target);
            this.reset();

            return temp;
        }
        else
        {
            this.nextStep();

            return new Vector3(this.interpolation.apply(this.start.x, this.target.x, this.step),
                    this.interpolation.apply(this.start.y, this.target.y, this.step),
                    this.interpolation.apply(this.start.z, this.target.z, this.step));
        }
    }

    public boolean complete()
    {
        return this.step >= 1f;
    }

    public void reset()
    {
        this.target = new Vector3();
        this.target = new Vector3();
        this.step = 0;
        this.run = false;
    }
}