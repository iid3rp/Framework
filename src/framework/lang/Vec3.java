package framework.lang;

import java.nio.FloatBuffer;

public final class Vec3 extends Vector
{
    public float x;
    public float y;
    public float z;

    public static final Vec3 xAxis = new Vec3(1, 0, 0);
    public static final Vec3 yAxis = new Vec3(0, 1, 0);
    public static final Vec3 zAxis = new Vec3(0, 0, 1);

    public Vec3(Vector vec)
    {
        x = vec.x();
        y = vec.y();
        z = vec.z();
    }

    public Vec3() {}

    public Vec3(float xyz)
    {
        x = y = z = xyz;
    }

    public Vec3(Vec3 src)
    {
        this.set(src);
    }

    public Vec3(float x, float y, float z)
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

    /**
     * Rotate this vector the specified radians around the X axis.
     *
     * @param angle
     *          the angle in radians
     * @return this
     */
    public Vec3 rotateX(float angle) {
        return rotateX(angle, this);
    }

    public Vec3 rotateX(float angle, Vec3 dest) {
        float sin = Math.sin(angle), cos = Math.cosFromSin(sin, angle);
        float y = this.y * cos - this.z * sin;
        float z = this.y * sin + this.z * cos;
        dest.x = this.x;
        dest.y = y;
        dest.z = z;
        return dest;
    }

    /**
     * Rotate this vector the specified radians around the Y axis.
     *
     * @param angle
     *          the angle in radians
     * @return this
     */
    public Vec3 rotateY(float angle) {
        return rotateY(angle, this);
    }

    public Vec3 rotateY(float angle, Vec3 dest) {
        float sin = Math.sin(angle);
        float cos = Math.cos(angle);
        float x =  this.x * cos + this.z * sin;
        float z = -this.x * sin + this.z * cos;
        dest.x = x;
        dest.y = this.y;
        dest.z = z;
        return dest;
    }

    /**
     * Rotate this vector the specified radians around the Z axis.
     *
     * @param angle
     *          the angle in radians
     * @return this
     */
    public Vec3 rotateZ(float angle) {
        return rotateZ(angle, this);
    }

    public Vec3 rotateZ(float angle, Vec3 dest) {
        float sin = Math.sin(angle);
        float cos = Math.cos(angle);
        float x = this.x * cos - this.y * sin;
        float y = this.x * sin + this.y * cos;
        dest.x = x;
        dest.y = y;
        dest.z = this.z;
        return dest;
    }

    public Vec3 set(Vec3 src)
    {
        this.x = src.x;
        this.y = src.y;
        this.z = src.z;
        return this;
    }

    @Override
    public float lengthSquared()
    {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    public float lengthFrom(Vec3 location)
    {
        float x = this.x - location.x;
        float y = this.y - location.y;
        float z = this.z - location.z;
        return (float) java.lang.Math.sqrt(x * x + y * y + z * z);
    }

    public Vec3 translate(float x, float y, float z)
    {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public Vec3 add(Vec3 right)
    {
        return add(this, right, this);
    }


    public static Vec3 add(Vec3 left, Vec3 right)
    {
        return add(left, right, null);
    }

    public static Vec3 add(Vec3 left, Vec3 right, Vec3 dest)
    {
        if (dest == null)
            return new Vec3(left.x + right.x, left.y + right.y, left.z + right.z);
        dest.set(left.x + right.x, left.y + right.y, left.z + right.z);
        return dest;
    }

    public static Vec3 sub(Vec3 left, Vec3 right, Vec3 dest)
    {
        if (dest == null) 
            return new Vec3(left.x - right.x, left.y - right.y, left.z - right.z);
        dest.set(left.x - right.x, left.y - right.y, left.z - right.z);
        return dest;
    }

    public static Vec3 cross(Vec3 left, Vec3 right, Vec3 dest) {
        if (dest == null) {
            dest = new Vec3();
        }

        dest.set(left.y * right.z - left.z * right.y, right.x * left.z - right.z * left.x, left.x * right.y - left.y * right.x);
        return dest;
    }

    public Vec3 negate()
    {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
        return this;
    }

    public Vec3 negate(Vec3 dest) {
        if (dest == null) {
            dest = new Vec3();
        }

        dest.x = -this.x;
        dest.y = -this.y;
        dest.z = -this.z;
        return dest;
    }

    public Vec3 normalize()
    {
        return normalize(this);
    }

    public Vec3 normalize(Vec3 dest) {
        float l = this.length();
        if (dest == null)
            dest = new Vec3(this.x / l, this.y / l, this.z / l);
        else
            dest.set(this.x / l, this.y / l, this.z / l);
        return dest;
    }

    public static float dot(Vec3 left, Vec3 right) {
        return left.x * right.x + left.y * right.y + left.z * right.z;
    }

    public static float angle(Vec3 a, Vec3 b) {
        float dls = dot(a, b) / (a.length() * b.length());
        if (dls < -1.0F) {
            dls = -1.0F;
        } else if (dls > 1.0F) {
            dls = 1.0F;
        }

        return (float) java.lang.Math.acos(dls);
    }

    public Vec3 load(FloatBuffer buf)
    {
        this.x = buf.get();
        this.y = buf.get();
        this.z = buf.get();
        return this;
    }

    public Vec3 mul(float scale)
    {
        return scale(scale);
    }

    public Vec3 scale(float scale)
    {
        this.x *= scale;
        this.y *= scale;
        this.z *= scale;
        return this;
    }

    public Vec3 store(FloatBuffer buf) {
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

    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        Vec3 other = (Vec3) obj;
        return this.x == other.x && this.y == other.y && this.z == other.z;
    }

    @Override
    public float x()
    {
        return x;
    }

    @Override
    public float y()
    {
        return y;
    }

    @Override
    public float z()
    {
        return z;
    }

    public boolean isWithin(Vec3 min, Vec3 max)
    {
        return x >= min.x && x <= max.x &&
                y >= min.y && y <= max.y &&
                z >= min.z && z <= max.z;
    }
}
