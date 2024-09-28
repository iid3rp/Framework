package framework.util;

import org.joml.Vector3f;

public class Vec3f
{
    public static Vector3f add(Vector3f left, Vector3f right, Vector3f dest)
    {
        if(dest == null) {
            return new Vector3f(left.x + right.x, left.y + right.y, left.z + right.z);
        }
        else {
            dest.set(left.x + right.x, left.y + right.y, left.z + right.z);
            return dest;
        }
    }

    public static Vector3f sub(Vector3f left, Vector3f right, Vector3f dest)
    {
        if(dest == null) {
            return new Vector3f(left.x - right.x, left.y - right.y, left.z - right.z);
        }
        else {
            dest.set(left.x - right.x, left.y - right.y, left.z - right.z);
            return dest;
        }
    }

    public static Vector3f cross(Vector3f left, Vector3f right, Vector3f dest)
    {
        if(dest == null) {
            dest = new Vector3f();
        }

        dest.set(left.y * right.z - left.z * right.y, right.x * left.z - right.z * left.x,
                left.x * right.y - left.y * right.x);
        return dest;
    }

    public static float dot(Vector3f left, Vector3f right)
    {
        return left.x * right.x + left.y * right.y + left.z * right.z;
    }

    public static float angle(Vector3f a, Vector3f b)
    {
        float dls = dot(a, b) / (a.length() * b.length());
        if(dls < -1.0F) {
            dls = -1.0F;
        }
        else if(dls > 1.0F) {
            dls = 1.0F;
        }

        return (float) Math.acos(dls);
    }
}
