package framework.lang;

import java.lang.Math;
import java.nio.FloatBuffer;

public sealed abstract class Vector
        permits Vec2, Vec3, Vec4 {

    protected Vector() {
    }

    public final float length() {
        return (float) Math.sqrt(lengthSquared());
    }

    public Vector normalize()
    {
        float len = this.length();
        if (len != 0.0F) {
            float l = 1.0F / len;
            return this.scale(l);
        } else {
            throw new IllegalStateException("Zero length vector");
        }
    }

    public abstract float lengthSquared();

    public abstract Vector load(FloatBuffer var1);

    public abstract Vector negate();

    public abstract Vector store(FloatBuffer var1);

    public abstract Vector scale(float var1);
    public abstract float x();

    public abstract float y();
    protected float z()
    {
        return 1;
    }

    protected float w()
    {
        return 1;
    }
}
