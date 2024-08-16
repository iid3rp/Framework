package framework.util;

import framework.h.Display;

public class SmoothFloat
{

    private float delta;
    private float target;
    private float actual;

    public SmoothFloat()
    {
        delta = 1;
        actual = 0;
    }

    public SmoothFloat(float initialValue, float delta)
    {
        this.target = initialValue;
        this.actual = initialValue;
        this.delta = delta;
    }

    public void update()
    {
        float offset = target - actual;
        float change = offset * delta;
        actual += change;
    }

    public void increaseTarget(float dT)
    {
        this.target += dT;
    }

    public void setTarget(float target)
    {
        this.target = target;
    }

    public void instantIncrease(float increase)
    {
        this.actual += increase;
    }

    public float get()
    {
        return actual;
    }

    public void force()
    {
    }

    public float getTarget()
    {
        return target;
    }

}
