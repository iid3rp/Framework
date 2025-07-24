package framework.lang;

import java.nio.FloatBuffer;

public class Mat4
{
    public float m00;
    public float m01;
    public float m02;
    public float m03;
    public float m10;
    public float m11;
    public float m12;
    public float m13;
    public float m20;
    public float m21;
    public float m22;
    public float m23;
    public float m30;
    public float m31;
    public float m32;
    public float m33;
    
    // overhead 

    public Mat4() {
        identity();
    }

    public Mat4(Mat4 src)
    {
        m00 = src.m00;
        m01 = src.m01;
        m02 = src.m02;
        m03 = src.m03;

        m10 = src.m10;
        m11 = src.m11;
        m12 = src.m12;
        m13 = src.m13;

        m20 = src.m20;
        m21 = src.m21;
        m22 = src.m22;
        m23 = src.m23;

        m30 = src.m30;
        m31 = src.m31;
        m32 = src.m32;
        m33 = src.m33;
    }

    public static Mat4 translate(float x, float y, float z, Mat4 src, Mat4 dest)
    {
        if (dest == null) {
            dest = new Mat4();
        }

        dest.m30 += src.m00 * x + src.m10 * y + src.m20 * z;
        dest.m31 += src.m01 * x + src.m11 * y + src.m21 * z;
        dest.m32 += src.m02 * x + src.m12 * y + src.m22 * z;
        dest.m33 += src.m03 * x + src.m13 * y + src.m23 * z;
        return dest;
    }

    public static Mat4 rotate(double angle, float x, float y, float z, Mat4 src, Mat4 dest)
    {
        if (dest == null)
            dest = new Mat4();

        float c = (float) Math.cos(angle);
        float s = (float) Math.sin(angle);
        float omc = 1f - c; // one minus c

        float xy = x * y;
        float yz = y * z;
        float xz = x * z;
        float xs = x * s;
        float ys = y * s;
        float zs = z * s;

        float f00 = x * x * omc + c;
        float f01 = xy * omc + zs;
        float f02 = xz * omc - ys;
        float f10 = xy * omc - zs;
        float f11 = y * y * omc + c;
        float f12 = yz * omc + xs;
        float f20 = xz * omc + ys;
        float f21 = yz * omc - xs;
        float f22 = z * z * omc + c;


        float t00 = src.m00 * f00 + src.m10 * f01 + src.m20 * f02;
        float t01 = src.m01 * f00 + src.m11 * f01 + src.m21 * f02;
        float t02 = src.m02 * f00 + src.m12 * f01 + src.m22 * f02;
        float t03 = src.m03 * f00 + src.m13 * f01 + src.m23 * f02;

        float t10 = src.m00 * f10 + src.m10 * f11 + src.m20 * f12;
        float t11 = src.m01 * f10 + src.m11 * f11 + src.m21 * f12;
        float t12 = src.m02 * f10 + src.m12 * f11 + src.m22 * f12;
        float t13 = src.m03 * f10 + src.m13 * f11 + src.m23 * f12;

        float t20 = src.m00 * f20 + src.m10 * f21 + src.m20 * f22;
        float t21 = src.m01 * f20 + src.m11 * f21 + src.m21 * f22;
        float t22 = src.m02 * f20 + src.m12 * f21 + src.m22 * f22;
        float t23 = src.m03 * f20 + src.m13 * f21 + src.m23 * f22;

        dest.m00 = t00;
        dest.m01 = t01;
        dest.m02 = t02;
        dest.m03 = t03;

        dest.m10 = t10;
        dest.m11 = t11;
        dest.m12 = t12;
        dest.m13 = t13;

        dest.m20 = t20;
        dest.m21 = t21;
        dest.m22 = t22;
        dest.m23 = t23;

        return dest;
    }

    public static Mat4 scale(float x, float y, float z, Mat4 src, Mat4 dest)
    {
        if (dest == null)
            dest = new Mat4();

        dest.m00 = src.m00 * x;
        dest.m01 = src.m01 * x;
        dest.m02 = src.m02 * x;
        dest.m03 = src.m03 * x;
        dest.m10 = src.m10 * y;
        dest.m11 = src.m11 * y;
        dest.m12 = src.m12 * y;
        dest.m13 = src.m13 * y;
        dest.m20 = src.m20 * z;
        dest.m21 = src.m21 * z;
        dest.m22 = src.m22 * z;
        dest.m23 = src.m23 * z;
        return dest;
    }

    public String toString() {
        return String.valueOf(m00) + ' ' + m10 + ' ' + m20 + ' ' + m30 + '\n' +
                m01 + ' ' + m11 + ' ' + m21 + ' ' + m31 + '\n' +
                m02 + ' ' + m12 + ' ' + m22 + ' ' + m32 + '\n' +
                m03 + ' ' + m13 + ' ' + m23 + ' ' + m33 + '\n';
    }

    public Mat4 identity() {
        return identity(this);
    }

    public static Mat4 identity(Mat4 m) {
        m.m00 = 1f;
        m.m01 = 0f;
        m.m02 = 0f;
        m.m03 = 0f;
        m.m10 = 0f;
        m.m11 = 1f;
        m.m12 = 0f;
        m.m13 = 0f;
        m.m20 = 0f;
        m.m21 = 0f;
        m.m22 = 1f;
        m.m23 = 0f;
        m.m30 = 0f;
        m.m31 = 0f;
        m.m32 = 0f;
        m.m33 = 1f;
        return m;
    }

    public Mat4 setZero() {
        return setZero(this);
    }

    public static Mat4 setZero(Mat4 m) {
        m.m00 = 0f;
        m.m01 = 0f;
        m.m02 = 0f;
        m.m03 = 0f;
        m.m10 = 0f;
        m.m11 = 0f;
        m.m12 = 0f;
        m.m13 = 0f;
        m.m20 = 0f;
        m.m21 = 0f;
        m.m22 = 0f;
        m.m23 = 0f;
        m.m30 = 0f;
        m.m31 = 0f;
        m.m32 = 0f;
        m.m33 = 0f;
        return m;
    }

    public Mat4 load(Mat4 src) {
        return load(src, this);
    }

    public static Mat4 load(Mat4 src, Mat4 dest) {
        if (dest == null) {
            dest = new Mat4();
        }

        dest.m00 = src.m00;
        dest.m01 = src.m01;
        dest.m02 = src.m02;
        dest.m03 = src.m03;
        dest.m10 = src.m10;
        dest.m11 = src.m11;
        dest.m12 = src.m12;
        dest.m13 = src.m13;
        dest.m20 = src.m20;
        dest.m21 = src.m21;
        dest.m22 = src.m22;
        dest.m23 = src.m23;
        dest.m30 = src.m30;
        dest.m31 = src.m31;
        dest.m32 = src.m32;
        dest.m33 = src.m33;
        return dest;
    }

    public Mat4 load(FloatBuffer buf) {
        m00 = buf.get();
        m01 = buf.get();
        m02 = buf.get();
        m03 = buf.get();
        m10 = buf.get();
        m11 = buf.get();
        m12 = buf.get();
        m13 = buf.get();
        m20 = buf.get();
        m21 = buf.get();
        m22 = buf.get();
        m23 = buf.get();
        m30 = buf.get();
        m31 = buf.get();
        m32 = buf.get();
        m33 = buf.get();
        return this;
    }

    public Mat4 loadTranspose(FloatBuffer buf) {
        m00 = buf.get();
        m10 = buf.get();
        m20 = buf.get();
        m30 = buf.get();
        m01 = buf.get();
        m11 = buf.get();
        m21 = buf.get();
        m31 = buf.get();
        m02 = buf.get();
        m12 = buf.get();
        m22 = buf.get();
        m32 = buf.get();
        m03 = buf.get();
        m13 = buf.get();
        m23 = buf.get();
        m33 = buf.get();
        return this;
    }

    public Mat4 store(FloatBuffer buf) {
        buf.put(m00);
        buf.put(m01);
        buf.put(m02);
        buf.put(m03);
        buf.put(m10);
        buf.put(m11);
        buf.put(m12);
        buf.put(m13);
        buf.put(m20);
        buf.put(m21);
        buf.put(m22);
        buf.put(m23);
        buf.put(m30);
        buf.put(m31);
        buf.put(m32);
        buf.put(m33);
        return this;
    }

    public Mat4 storeTranspose(FloatBuffer buf) {
        buf.put(m00);
        buf.put(m10);
        buf.put(m20);
        buf.put(m30);
        buf.put(m01);
        buf.put(m11);
        buf.put(m21);
        buf.put(m31);
        buf.put(m02);
        buf.put(m12);
        buf.put(m22);
        buf.put(m32);
        buf.put(m03);
        buf.put(m13);
        buf.put(m23);
        buf.put(m33);
        return this;
    }

    public Mat4 store3f(FloatBuffer buf) {
        buf.put(m00);
        buf.put(m01);
        buf.put(m02);
        buf.put(m10);
        buf.put(m11);
        buf.put(m12);
        buf.put(m20);
        buf.put(m21);
        buf.put(m22);
        return this;
    }

    public static Mat4 add(Mat4 left, Mat4 right, Mat4 dest) {
        if (dest == null) {
            dest = new Mat4();
        }

        dest.m00 = left.m00 + right.m00;
        dest.m01 = left.m01 + right.m01;
        dest.m02 = left.m02 + right.m02;
        dest.m03 = left.m03 + right.m03;
        dest.m10 = left.m10 + right.m10;
        dest.m11 = left.m11 + right.m11;
        dest.m12 = left.m12 + right.m12;
        dest.m13 = left.m13 + right.m13;
        dest.m20 = left.m20 + right.m20;
        dest.m21 = left.m21 + right.m21;
        dest.m22 = left.m22 + right.m22;
        dest.m23 = left.m23 + right.m23;
        dest.m30 = left.m30 + right.m30;
        dest.m31 = left.m31 + right.m31;
        dest.m32 = left.m32 + right.m32;
        dest.m33 = left.m33 + right.m33;
        return dest;
    }

    public static Mat4 sub(Mat4 left, Mat4 right, Mat4 dest) {
        if (dest == null) {
            dest = new Mat4();
        }

        dest.m00 = left.m00 - right.m00;
        dest.m01 = left.m01 - right.m01;
        dest.m02 = left.m02 - right.m02;
        dest.m03 = left.m03 - right.m03;
        dest.m10 = left.m10 - right.m10;
        dest.m11 = left.m11 - right.m11;
        dest.m12 = left.m12 - right.m12;
        dest.m13 = left.m13 - right.m13;
        dest.m20 = left.m20 - right.m20;
        dest.m21 = left.m21 - right.m21;
        dest.m22 = left.m22 - right.m22;
        dest.m23 = left.m23 - right.m23;
        dest.m30 = left.m30 - right.m30;
        dest.m31 = left.m31 - right.m31;
        dest.m32 = left.m32 - right.m32;
        dest.m33 = left.m33 - right.m33;
        return dest;
    }

    public static Mat4 mul(Mat4 left, Mat4 right, Mat4 dest) {
        if (dest == null) {
            dest = new Mat4();
        }

        dest.m00 = left.m00 * right.m00 + left.m10 * right.m01 + left.m20 * right.m02 + left.m30 * right.m03;
        dest.m01 = left.m01 * right.m00 + left.m11 * right.m01 + left.m21 * right.m02 + left.m31 * right.m03;
        dest.m02 = left.m02 * right.m00 + left.m12 * right.m01 + left.m22 * right.m02 + left.m32 * right.m03;
        dest.m03 = left.m03 * right.m00 + left.m13 * right.m01 + left.m23 * right.m02 + left.m33 * right.m03;
        dest.m10 = left.m00 * right.m10 + left.m10 * right.m11 + left.m20 * right.m12 + left.m30 * right.m13;
        dest.m11 = left.m01 * right.m10 + left.m11 * right.m11 + left.m21 * right.m12 + left.m31 * right.m13;
        dest.m12 = left.m02 * right.m10 + left.m12 * right.m11 + left.m22 * right.m12 + left.m32 * right.m13;
        dest.m13 = left.m03 * right.m10 + left.m13 * right.m11 + left.m23 * right.m12 + left.m33 * right.m13;
        dest.m20 = left.m00 * right.m20 + left.m10 * right.m21 + left.m20 * right.m22 + left.m30 * right.m23;
        dest.m21 = left.m01 * right.m20 + left.m11 * right.m21 + left.m21 * right.m22 + left.m31 * right.m23;
        dest.m22 = left.m02 * right.m20 + left.m12 * right.m21 + left.m22 * right.m22 + left.m32 * right.m23;
        dest.m23 = left.m03 * right.m20 + left.m13 * right.m21 + left.m23 * right.m22 + left.m33 * right.m23;
        dest.m30 = left.m00 * right.m30 + left.m10 * right.m31 + left.m20 * right.m32 + left.m30 * right.m33;
        dest.m31 = left.m01 * right.m30 + left.m11 * right.m31 + left.m21 * right.m32 + left.m31 * right.m33;
        dest.m32 = left.m02 * right.m30 + left.m12 * right.m31 + left.m22 * right.m32 + left.m32 * right.m33;
        dest.m33 = left.m03 * right.m30 + left.m13 * right.m31 + left.m23 * right.m32 + left.m33 * right.m33;
        return dest;
    }

    public static Vec4 transform(Mat4 left, Vec4 right, Vec4 dest)
    {
        if (dest == null)
            dest = new Vec4();

        dest.x = left.m00 * right.x + left.m10 * right.y + left.m20 * right.z + left.m30 * right.w;
        dest.y = left.m01 * right.x + left.m11 * right.y + left.m21 * right.z + left.m31 * right.w;
        dest.z = left.m02 * right.x + left.m12 * right.y + left.m22 * right.z + left.m32 * right.w;
        dest.w = left.m03 * right.x + left.m13 * right.y + left.m23 * right.z + left.m33 * right.w;

        return dest;
    }

    public Mat4 transpose() {
        return transpose(this);
    }

    public Mat4 translate(Vec2 vec) {
        return translate(vec, this);
    }

    public Mat4 translate(Vec3 vec) {
        return translate(vec, this);
    }

    public Mat4 scale(Vec3 vec) {
        return scale(vec, this, this);
    }

    public static Mat4 scale(Vec3 vec, Mat4 src, Mat4 dest) {
        if (dest == null)
            dest = new Mat4();


        dest.m00 = src.m00 * vec.x;
        dest.m01 = src.m01 * vec.x;
        dest.m02 = src.m02 * vec.x;
        dest.m03 = src.m03 * vec.x;
        dest.m10 = src.m10 * vec.y;
        dest.m11 = src.m11 * vec.y;
        dest.m12 = src.m12 * vec.y;
        dest.m13 = src.m13 * vec.y;
        dest.m20 = src.m20 * vec.z;
        dest.m21 = src.m21 * vec.z;
        dest.m22 = src.m22 * vec.z;
        dest.m23 = src.m23 * vec.z;
        return dest;
    }

    public Mat4 rotate(float angle, Vec3 axis) {
        return rotate(angle, axis, this);
    }

    public Mat4 rotate(float angle, Vec3 axis, Mat4 dest) {
        return rotate(angle, axis, this, dest);
    }

    public static Mat4 rotate(float angle, Vec3 axis, Mat4 src, Mat4 dest) {
        if (dest == null) 
            dest = new Mat4();

        float c = GeomMath.cos(angle);
        float s = GeomMath.sin(angle);
        float omc = 1f - c; // omc means one minus c

        float xy = axis.x * axis.y;
        float yz = axis.y * axis.z;
        float xz = axis.x * axis.z;
        float xs = axis.x * s;
        float ys = axis.y * s;
        float zs = axis.z * s;

        float f00 = axis.x * axis.x * omc + c;
        float f01 = xy * omc + zs;
        float f02 = xz * omc - ys;
        float f10 = xy * omc - zs;
        float f11 = axis.y * axis.y * omc + c;
        float f12 = yz * omc + xs;
        float f20 = xz * omc + ys;
        float f21 = yz * omc - xs;
        float f22 = axis.z * axis.z * omc + c;


        float t00 = src.m00 * f00 + src.m10 * f01 + src.m20 * f02;
        float t01 = src.m01 * f00 + src.m11 * f01 + src.m21 * f02;
        float t02 = src.m02 * f00 + src.m12 * f01 + src.m22 * f02;
        float t03 = src.m03 * f00 + src.m13 * f01 + src.m23 * f02;
        
        float t10 = src.m00 * f10 + src.m10 * f11 + src.m20 * f12;
        float t11 = src.m01 * f10 + src.m11 * f11 + src.m21 * f12;
        float t12 = src.m02 * f10 + src.m12 * f11 + src.m22 * f12;
        float t13 = src.m03 * f10 + src.m13 * f11 + src.m23 * f12;

        float t20 = src.m00 * f20 + src.m10 * f21 + src.m20 * f22;
        float t21 = src.m01 * f20 + src.m11 * f21 + src.m21 * f22;
        float t22 = src.m02 * f20 + src.m12 * f21 + src.m22 * f22;
        float t23 = src.m03 * f20 + src.m13 * f21 + src.m23 * f22;

        dest.m00 = t00;
        dest.m01 = t01;
        dest.m02 = t02;
        dest.m03 = t03;

        dest.m10 = t10;
        dest.m11 = t11;
        dest.m12 = t12;
        dest.m13 = t13;

        dest.m20 = t20;
        dest.m21 = t21;
        dest.m22 = t22;
        dest.m23 = t23;

        return dest;
    }

    public Mat4 translate(Vec3 vec, Mat4 dest) {
        return translate(vec, this, dest);
    }

    public static Mat4 translate(Vec3 vec, Mat4 src, Mat4 dest) {
        if (dest == null) {
            dest = new Mat4();
        }

        dest.m30 += src.m00 * vec.x + src.m10 * vec.y + src.m20 * vec.z;
        dest.m31 += src.m01 * vec.x + src.m11 * vec.y + src.m21 * vec.z;
        dest.m32 += src.m02 * vec.x + src.m12 * vec.y + src.m22 * vec.z;
        dest.m33 += src.m03 * vec.x + src.m13 * vec.y + src.m23 * vec.z;
        return dest;
    }

    public Mat4 translate(Vec2 vec, Mat4 dest) {
        return translate(vec, this, dest);
    }

    public static Mat4 translate(Vec2 vec, Mat4 src, Mat4 dest) {
        if (dest == null) {
            dest = new Mat4();
        }

        dest.m30 += src.m00 * vec.x + src.m10 * vec.y;
        dest.m31 += src.m01 * vec.x + src.m11 * vec.y;
        dest.m32 += src.m02 * vec.x + src.m12 * vec.y;
        dest.m33 += src.m03 * vec.x + src.m13 * vec.y;
        return dest;
    }

    public static Mat4 translate(float x, float y, Mat4 src, Mat4 dest) {
        if (dest == null) {
            dest = new Mat4();
        }

        dest.m30 += src.m00 * x + src.m10 * y;
        dest.m31 += src.m01 * x + src.m11 * y;
        dest.m32 += src.m02 * x + src.m12 * y;
        dest.m33 += src.m03 * x + src.m13 * y;
        return dest;
    }

    public Mat4 transpose(Mat4 dest) {
        return transpose(this, dest);
    }

    public static Mat4 transpose(Mat4 src, Mat4 dest) {
        if (dest == null) {
            dest = new Mat4();
        }

        dest.m00 = src.m00;
        dest.m01 = src.m10;
        dest.m02 = src.m20;
        dest.m03 = src.m30;
        dest.m10 = src.m01;
        dest.m11 = src.m11;
        dest.m12 = src.m21;
        dest.m13 = src.m31;
        dest.m20 = src.m02;
        dest.m21 = src.m12;
        dest.m22 = src.m22;
        dest.m23 = src.m32;
        dest.m30 = src.m03;
        dest.m31 = src.m13;
        dest.m32 = src.m23;
        dest.m33 = src.m33;
        
        return dest;
    }

    public float det()
    {
        return (m00 * (m11 * m22 * m33 + m12 * m23 * m31 + m13 * m21 * m32 - m13 * m22 * m31 - m11 * m23 * m32 - m12 * m21 * m33)) 
                - (m01 * (m10 * m22 * m33 + m12 * m23 * m30 + m13 * m20 * m32 - m13 * m22 * m30 - m10 * m23 * m32 - m12 * m20 * m33))
                + (m02 * (m10 * m21 * m33 + m11 * m23 * m30 + m13 * m20 * m31 - m13 * m21 * m30 - m10 * m23 * m31 - m11 * m20 * m33))
                - (m03 * (m10 * m21 * m32 + m11 * m22 * m30 + m12 * m20 * m31 - m12 * m21 * m30 - m10 * m22 * m31 - m11 * m20 * m32));
    }

    private static float det3(float t00, float t01, float t02, float t10, float t11, float t12, float t20, float t21, float t22) {
        return t00 * (t11 * t22 - t12 * t21)
                + t01 * (t12 * t20 - t10 * t22)
                + t02 * (t10 * t21 - t11 * t20);
    }

    public Mat4 invert() {
        return invert(this, this);
    }

    public static Mat4 invert(Mat4 src, Mat4 dest)
    {
        float det = src.det(); // determinant
        if (det == 0f)
            return null;

        if (dest == null)
            dest = new Mat4();

        float odd = 1f / det; // odd means one divided by determinant
        float t00 = det3(src.m11, src.m12, src.m13, src.m21, src.m22, src.m23, src.m31, src.m32, src.m33);
        float t01 = -det3(src.m10, src.m12, src.m13, src.m20, src.m22, src.m23, src.m30, src.m32, src.m33);
        float t02 = det3(src.m10, src.m11, src.m13, src.m20, src.m21, src.m23, src.m30, src.m31, src.m33);
        float t03 = -det3(src.m10, src.m11, src.m12, src.m20, src.m21, src.m22, src.m30, src.m31, src.m32);
        float t10 = -det3(src.m01, src.m02, src.m03, src.m21, src.m22, src.m23, src.m31, src.m32, src.m33);
        float t11 = det3(src.m00, src.m02, src.m03, src.m20, src.m22, src.m23, src.m30, src.m32, src.m33);
        float t12 = -det3(src.m00, src.m01, src.m03, src.m20, src.m21, src.m23, src.m30, src.m31, src.m33);
        float t13 = det3(src.m00, src.m01, src.m02, src.m20, src.m21, src.m22, src.m30, src.m31, src.m32);
        float t20 = det3(src.m01, src.m02, src.m03, src.m11, src.m12, src.m13, src.m31, src.m32, src.m33);
        float t21 = -det3(src.m00, src.m02, src.m03, src.m10, src.m12, src.m13, src.m30, src.m32, src.m33);
        float t22 = det3(src.m00, src.m01, src.m03, src.m10, src.m11, src.m13, src.m30, src.m31, src.m33);
        float t23 = -det3(src.m00, src.m01, src.m02, src.m10, src.m11, src.m12, src.m30, src.m31, src.m32);
        float t30 = -det3(src.m01, src.m02, src.m03, src.m11, src.m12, src.m13, src.m21, src.m22, src.m23);
        float t31 = det3(src.m00, src.m02, src.m03, src.m10, src.m12, src.m13, src.m20, src.m22, src.m23);
        float t32 = -det3(src.m00, src.m01, src.m03, src.m10, src.m11, src.m13, src.m20, src.m21, src.m23);
        float t33 = det3(src.m00, src.m01, src.m02, src.m10, src.m11, src.m12, src.m20, src.m21, src.m22);
        dest.m00 = t00 * odd;
        dest.m11 = t11 * odd;
        dest.m22 = t22 * odd;
        dest.m33 = t33 * odd;
        dest.m01 = t10 * odd;
        dest.m10 = t01 * odd;
        dest.m20 = t02 * odd;
        dest.m02 = t20 * odd;
        dest.m12 = t21 * odd;
        dest.m21 = t12 * odd;
        dest.m03 = t30 * odd;
        dest.m30 = t03 * odd;
        dest.m13 = t31 * odd;
        dest.m31 = t13 * odd;
        dest.m32 = t23 * odd;
        dest.m23 = t32 * odd;
        return dest;

    }

    public Mat4 negate() {
        return negate(this);
    }

    public Mat4 negate(Mat4 dest) {
        return negate(this, dest);
    }

    public static Mat4 negate(Mat4 src, Mat4 dest) {
        if (dest == null) {
            dest = new Mat4();
        }

        dest.m00 = -src.m00;
        dest.m01 = -src.m01;
        dest.m02 = -src.m02;
        dest.m03 = -src.m03;
        dest.m10 = -src.m10;
        dest.m11 = -src.m11;
        dest.m12 = -src.m12;
        dest.m13 = -src.m13;
        dest.m20 = -src.m20;
        dest.m21 = -src.m21;
        dest.m22 = -src.m22;
        dest.m23 = -src.m23;
        dest.m30 = -src.m30;
        dest.m31 = -src.m31;
        dest.m32 = -src.m32;
        dest.m33 = -src.m33;
        return dest;
    }
}
