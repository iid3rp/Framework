package framework.lang;

import java.nio.FloatBuffer;

public class Vector3f
{
    public float x;
    public float y;
    public float z;

    public static Vector3f xAxis = new Vector3f(1, 0, 0);
    public static Vector3f yAxis = new Vector3f(0, 1, 0);
    public static Vector3f zAxis = new Vector3f(0, 0, 1);

    public Vector3f()
    {
        x = y = z = 0;
    }

    public Vector3f(float xyz)
    {
        x = y = z = xyz;
    }

    public Vector3f(Vector3f src)
    {
        this.set(src);
    }

    public Vector3f(float x, float y, float z)
    {
        this.set(x, y, z);
    }

    public void set(float x, float y) 
    {
        this.x = x;
        this.y = y;
    }

    public void set(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f set(Vector3f src)
    {
        this.x = src.x;
        this.y = src.y;
        this.z = src.z;
        return this;
    }
    
    public float length()
    {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public float lengthSquared() 
    {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    public Vector3f translate(float x, float y, float z)
    {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public static Vector3f add(Vector3f left, Vector3f right)
    {
        return add(left, right, null);
    }

    public static Vector3f add(Vector3f left, Vector3f right, Vector3f dest)
    {
        if (dest == null)
            return new Vector3f(left.x + right.x, left.y + right.y, left.z + right.z);
        dest.set(left.x + right.x, left.y + right.y, left.z + right.z);
        return dest;
    }

    public static Vector3f sub(Vector3f left, Vector3f right, Vector3f dest)
    {
        if (dest == null) 
            return new Vector3f(left.x - right.x, left.y - right.y, left.z - right.z);
        dest.set(left.x - right.x, left.y - right.y, left.z - right.z);
        return dest;
    }

    public static Vector3f cross(Vector3f left, Vector3f right, Vector3f dest) {
        if (dest == null) {
            dest = new Vector3f();
        }

        dest.set(left.y * right.z - left.z * right.y, right.x * left.z - right.z * left.x, left.x * right.y - left.y * right.x);
        return dest;
    }

    public Vector3f negate()
    {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
        return this;
    }

    public Vector3f negate(Vector3f dest) {
        if (dest == null) {
            dest = new Vector3f();
        }

        dest.x = -this.x;
        dest.y = -this.y;
        dest.z = -this.z;
        return dest;
    }

    public Vector3f normalize()
    {
        return normalize(null);
    }

    public Vector3f normalize(Vector3f dest) {
        float l = this.length();
        if (dest == null) {
            dest = new Vector3f(this.x / l, this.y / l, this.z / l);
        } else {
            dest.set(this.x / l, this.y / l, this.z / l);
        }

        return dest;
    }

    public static float dot(Vector3f left, Vector3f right) {
        return left.x * right.x + left.y * right.y + left.z * right.z;
    }

    public static float angle(Vector3f a, Vector3f b) {
        float dls = dot(a, b) / (a.length() * b.length());
        if (dls < -1.0F) {
            dls = -1.0F;
        } else if (dls > 1.0F) {
            dls = 1.0F;
        }

        return (float)Math.acos(dls);
    }

    public Vector3f load(FloatBuffer buf)
    {
        this.x = buf.get();
        this.y = buf.get();
        this.z = buf.get();
        return this;
    }

    public Vector3f mul(float scale)
    {
        return scale(scale);
    }

    public Vector3f scale(float scale)
    {
        this.x *= scale;
        this.y *= scale;
        this.z *= scale;
        return this;
    }

    public Vector3f store(FloatBuffer buf) {
        buf.put(this.x);
        buf.put(this.y);
        buf.put(this.z);
        return this;
    }

    @Override
    public String toString() {
        String sb = "Vec3[" +
                this.x +
                ", " +
                this.y +
                ", " +
                this.z +
                ']';
        return sb;
    }

    public final float getX() {
        return this.x;
    }

    public final float getY() {
        return this.y;
    }

    public final void setX(float x) {
        this.x = x;
    }

    public final void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getZ() {
        return this.z;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            Vector3f other = (Vector3f)obj;
            return this.x == other.x && this.y == other.y && this.z == other.z;
        }
    }
}
