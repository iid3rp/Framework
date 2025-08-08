package framework.lang;


import framework.shader.GLShader;
import framework.shader.GLShader.ShaderProgram.Struct;
import org.jetbrains.annotations.NotNull;

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


    // Calculations
    // Translation
    // =======================================================================================================================
    // =======================================================================================================================
    // =======================================================================================================================
    // =======================================================================================================================
    public Mat4 translate(float x, float y)
    {
        return translate(x, y, this);
    }

    public Mat4 translate(float x, float y, Mat4 dest)
    {
        return translate(x, y, this, dest);
    }

    public static Mat4 translate(float x, float y, Mat4 src, Mat4 dest)
    {
        return Translate.translate(x, y, src, dest);
    }

    public Mat4 translate(float x, float y, float z)
    {
        return translate(x, y, z,this);
    }

    public Mat4 translate(float x, float y, float z, Mat4 dest)
    {
        return translate(x, y, z, this, dest);
    }

    public static Mat4 translate(float x, float y, float z, Mat4 src, Mat4 dest)
    {
        return Translate.translate(x, y, z, src, dest);
    }

    public Mat4 translate(Vec2 vec)
    {
        return translate(vec.x, vec.y);
    }

    public Mat4 translate(Vec2 vec, Mat4 dest)
    {
        return translate(vec.x, vec.y, this, dest);
    }

    public static Mat4 translate(Vec2 vec, Mat4 src, Mat4 dest)
    {
        return translate(vec.x, vec.y, src, dest);
    }

    public Mat4 translate(Vec3 vec, Mat4 dest)
    {
        return translate(vec.x, vec.y, vec.z, this, dest);
    }

    public static Mat4 translate(Vec3 vec, Mat4 src, Mat4 dest)
    {
        return translate(vec.x, vec.y, vec.z, src, dest);
    }

    private static class Translate
    {
        public static Mat4 translate(float x, float y, Mat4 src, @NotNull Mat4 dest) {

            dest.m30 += src.m00 * x + src.m10 * y;
            dest.m31 += src.m01 * x + src.m11 * y;
            dest.m32 += src.m02 * x + src.m12 * y;
            dest.m33 += src.m03 * x + src.m13 * y;
            return dest;
        }

        private static Mat4 translate(float x, float y, float z, Mat4 src, @NotNull Mat4 dest)
        {
            dest.m30 += src.m00 * x + src.m10 * y + src.m20 * z;
            dest.m31 += src.m01 * x + src.m11 * y + src.m21 * z;
            dest.m32 += src.m02 * x + src.m12 * y + src.m22 * z;
            dest.m33 += src.m03 * x + src.m13 * y + src.m23 * z;
            return dest;
        }
    }

    // =======================================================================================================================
    // =======================================================================================================================
    // =======================================================================================================================
    // =======================================================================================================================

    public Mat4 rotate(double angle, float x,  float y, float z)
    {
        return rotate(angle, x, y, z, this);
    }

    public Mat4 rotate(double angle, float x, float y, float z, Mat4 dest) {
        return rotate(angle, x, y, z, this, dest);
    }

    public static Mat4 rotate(double angle, float x, float y, float z, Mat4 src, Mat4 dest)
    {
        return Rotate.rotate(angle, x, y, z, src, dest);
    }

    public Mat4 rotate(double angle, Vec3 axis)
    {
        return rotate(angle, axis, this);
    }

    public Mat4 rotate(double angle, Vec3 axis, Mat4 dest) {
        return rotate(angle, axis, this, dest);
    }

    public static Mat4 rotate(double angle, Vec3 axis, Mat4 src, Mat4 dest)
    {
        return Rotate.rotate(angle, axis.x, axis.y, axis.z, src, dest);
    }

    private static class Rotate
    {
        public static Mat4 rotate(double angle, float x, float y, float z, Mat4 src, @NotNull Mat4 dest)
        {
            // angles
            float c; // cos
            float s; // sin
            float omc; // one minus c

            // multiplication of axes
            float xy;
            float yz;
            float xz;

            // sine axes
            float xs;
            float ys;
            float zs;

            // 3x3 matrix
            // x
            float f00;
            float f01;
            float f02;

            // y
            float f10;
            float f11;
            float f12;

            // z
            float f20;
            float f21;
            float f22;

            // pointers
            float p00;
            float p01;
            float p02;
            float p03;
            float p10;
            float p11;
            float p12;
            float p13;
            float p20;
            float p21;
            float p22;
            float p23;


            c = (float) Math.cos(angle);
            s = (float) Math.sin(angle);
            omc = 1f - c;

            xy = x * y;
            yz = y * z;
            xz = x * z;

            xs = x * s;
            ys = y * s;
            zs = z * s;

            f00 = x * x * omc + c;
            f01 = xy * omc + zs;
            f02 = xz * omc - ys;
            f10 = xy * omc - zs;
            f11 = y * y * omc + c;
            f12 = yz * omc + xs;
            f20 = xz * omc + ys;
            f21 = yz * omc - xs;
            f22 = z * z * omc + c;


            p00 = src.m00 * f00 + src.m10 * f01 + src.m20 * f02;
            p01 = src.m01 * f00 + src.m11 * f01 + src.m21 * f02;
            p02 = src.m02 * f00 + src.m12 * f01 + src.m22 * f02;
            p03 = src.m03 * f00 + src.m13 * f01 + src.m23 * f02;

            p10 = src.m00 * f10 + src.m10 * f11 + src.m20 * f12;
            p11 = src.m01 * f10 + src.m11 * f11 + src.m21 * f12;
            p12 = src.m02 * f10 + src.m12 * f11 + src.m22 * f12;
            p13 = src.m03 * f10 + src.m13 * f11 + src.m23 * f12;

            p20 = src.m00 * f20 + src.m10 * f21 + src.m20 * f22;
            p21 = src.m01 * f20 + src.m11 * f21 + src.m21 * f22;
            p22 = src.m02 * f20 + src.m12 * f21 + src.m22 * f22;
            p23 = src.m03 * f20 + src.m13 * f21 + src.m23 * f22;

            dest.m00 = p00;
            dest.m01 = p01;
            dest.m02 = p02;
            dest.m03 = p03;

            dest.m10 = p10;
            dest.m11 = p11;
            dest.m12 = p12;
            dest.m13 = p13;

            dest.m20 = p20;
            dest.m21 = p21;
            dest.m22 = p22;
            dest.m23 = p23;

            return dest;
        }
    }

    // =======================================================================================================================
    // =======================================================================================================================
    // =======================================================================================================================
    // =======================================================================================================================

    public Mat4 scale(float x, float y)
    {
        return scale(x, y, 1, this);
    }

    public Mat4 scale(float x, float y, Mat4 src)
    {
        return scale(x, y, 1, src);
    }

    public static Mat4 scale(float x, float y, Mat4 src, Mat4 dest)
    {
        return Scale.scale(x, y, 1, src, dest);
    }

    public Mat4 scale(float x, float y, float z)
    {
        return scale(x, y, z, this);
    }

    public static Mat4 scale(float x, float y, float z, Mat4 src)
    {
        return Scale.scale(x, y, z, src);
    }

    public static Mat4 scale(float x, float y, float z, Mat4 src, Mat4 dest)
    {
        return Scale.scale(x, y, z, src, dest);
    }

    public Mat4 scale(Vec2 vec)
    {
        return scale(vec.x, vec.y);
    }

    public Mat4 scale(Vec2 vec2,  Mat4 src)
    {
        return scale(vec2.x, vec2.y, src);
    }

    public static Mat4 scale(Vec2 vec2,  Mat4 src, Mat4 dest)
    {
        return scale(vec2.x, vec2.y, src, dest);
    }

    public Mat4 scale(Vec3 vec)
    {
        return scale(vec.x, vec.y, vec.z);
    }

    public Mat4 scale(Vec3 vec, Mat4 src)
    {
        return scale(vec.x, vec.y, vec.z, src);
    }

    public static Mat4 scale(Vec3 vec, Mat4 src, Mat4 dest)
    {
        return scale(vec.x, vec.y, vec.z, src, dest);
    }

    public static class Scale
    {

        public static Mat4 scale(float x, float y, float z, @NotNull Mat4 src)
        {
            src.m00 *= x;
            src.m01 *= x;
            src.m02 *= x;
            src.m03 *= x;

            src.m10 *= y;
            src.m11 *= y;
            src.m12 *= y;
            src.m13 *= y;

            src.m20 *= z;
            src.m21 *= z;
            src.m22 *= z;
            src.m23 *= z;
            return src;
        }

        public static Mat4 scale(float x, float y, float z, @NotNull Mat4 src, @NotNull Mat4 dest)
        {
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
    }

    // =======================================================================================================================
    // =======================================================================================================================
    // =======================================================================================================================
    // =======================================================================================================================

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


    // =======================================================================================================================
    // =======================================================================================================================
    // =======================================================================================================================
    // =======================================================================================================================

    public static class Add
    {
        public static Mat4 add(Mat4 left, Mat4 right) {

            left.m00 += right.m00;
            left.m01 += right.m01;
            left.m02 += right.m02;
            left.m03 += right.m03;
            left.m10 += right.m10;
            left.m11 += right.m11;
            left.m12 += right.m12;
            left.m13 += right.m13;
            left.m20 += right.m20;
            left.m21 += right.m21;
            left.m22 += right.m22;
            left.m23 += right.m23;
            left.m30 += right.m30;
            left.m31 += right.m31;
            left.m32 += right.m32;
            left.m33 += right.m33;
            return left;
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
    }

    public static Mat4 add(Mat4 left, Mat4 right, Mat4 dest) {
        return Add.add(left, right, dest);
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

    public Mat4 translate(Vec3 vec) {
        return translate(vec, this);
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

    public static class Matrix {
        private static final float ONE = 1.0f;
        private static final float ZERO = 0.0f;
        private static final Struct str;

        static
        {
            str = new Struct("matrix", "transform", "projection", "view");
        }


        public static Mat4 createTransformation(float posX, float posY, float scaleX, float scaleY, Mat4 dest)
        {
            dest.identity();

            dest.m30 += dest.m00 * posX + dest.m10 * posY;
            dest.m31 += dest.m01 * posX + dest.m11 * posY;
            dest.m32 += dest.m02 * posX + dest.m12 * posY;
            dest.m33 += dest.m03 * posX + dest.m13 * posY;

            dest.m00 *= scaleX;
            dest.m01 *= scaleX;
            dest.m02 *= scaleX;
            dest.m03 *= scaleX;

            dest.m10 *= scaleY;
            dest.m11 *= scaleY;
            dest.m12 *= scaleY;
            dest.m13 *= scaleY;
            return dest;
        }

        /**
         * Creates a transformation matrix from a set of translation, rotation, and scaling values.
         * This method is thread-safe as it uses only local variables for all calculations.
         *
         * @param posX   The x-component of the translation.
         * @param posY   The y-component of the translation.
         * @param posZ   The z-component of the translation.
         * @param rotX   The rotation angle around the x-axis in degrees.
         * @param rotY   The rotation angle around the y-axis in degrees.
         * @param rotZ   The rotation angle around the z-axis in degrees.
         * @param scaleX The scaling factor along the x-axis.
         * @param scaleY The scaling factor along the y-axis.
         * @param scaleZ The scaling factor along the z-axis.
         * @param dest   The destination matrix to store the result. Must not be null.
         * @return The destination matrix with the applied transformation.
         */
        public static Mat4 createTransformation(float posX, float posY, float posZ,
                                                float rotX, float rotY, float rotZ,
                                                float scaleX, float scaleY, float scaleZ,
                                                Mat4 dest) {
            dest.identity();

            dest.m30 += dest.m00 * posX + dest.m10 * posY + dest.m20 * posZ;
            dest.m31 += dest.m01 * posX + dest.m11 * posY + dest.m21 * posZ;
            dest.m32 += dest.m02 * posX + dest.m12 * posY + dest.m22 * posZ;
            dest.m33 += dest.m03 * posX + dest.m13 * posY + dest.m23 * posZ;

            double rx = Math.toRadians(rotX);
            double ry = Math.toRadians(rotY);
            double rz = Math.toRadians(rotZ);

            float crx = (float) Math.cos(rx);
            float cry = (float) Math.cos(ry);
            float crz = (float) Math.cos(rz);

            float srx = (float) Math.sin(rx);
            float sry = (float) Math.sin(ry);
            float srz = (float) Math.sin(rz);

            float f00, f01, f02;
            float f10, f11, f12;
            float f20, f21, f22;

            float p00, p01, p02, p03;
            float p10, p11, p12, p13;
            float p20, p21, p22, p23;

            // rotX matrix
            f00 = ONE; f01 = ZERO; f02 = ZERO;
            f10 = ZERO; f11 = crx; f12 = srx;
            f20 = ZERO; f21 = -srx; f22 = crx;

            // Apply rotation X to the destination matrix
            p00 = dest.m00 * f00 + dest.m10 * f01 + dest.m20 * f02;
            p01 = dest.m01 * f00 + dest.m11 * f01 + dest.m21 * f02;
            p02 = dest.m02 * f00 + dest.m12 * f01 + dest.m22 * f02;
            p03 = dest.m03 * f00 + dest.m13 * f01 + dest.m23 * f02;

            p10 = dest.m00 * f10 + dest.m10 * f11 + dest.m20 * f12;
            p11 = dest.m01 * f10 + dest.m11 * f11 + dest.m21 * f12;
            p12 = dest.m02 * f10 + dest.m12 * f11 + dest.m22 * f12;
            p13 = dest.m03 * f10 + dest.m13 * f11 + dest.m23 * f12;

            p20 = dest.m00 * f20 + dest.m10 * f21 + dest.m20 * f22;
            p21 = dest.m01 * f20 + dest.m11 * f21 + dest.m21 * f22;
            p22 = dest.m02 * f20 + dest.m12 * f21 + dest.m22 * f22;
            p23 = dest.m03 * f20 + dest.m13 * f21 + dest.m23 * f22;

            dest.m00 = p00; dest.m01 = p01; dest.m02 = p02; dest.m03 = p03;
            dest.m10 = p10; dest.m11 = p11; dest.m12 = p12; dest.m13 = p13;
            dest.m20 = p20; dest.m21 = p21; dest.m22 = p22; dest.m23 = p23;

            // rotY matrix
            f00 = cry; f01 = ZERO; f02 = -sry;
            f10 = ZERO; f11 = ONE; f12 = ZERO;
            f20 = sry; f21 = ZERO; f22 = cry;

            // Apply rotation Y to the destination matrix
            p00 = dest.m00 * f00 + dest.m10 * f01 + dest.m20 * f02;
            p01 = dest.m01 * f00 + dest.m11 * f01 + dest.m21 * f02;
            p02 = dest.m02 * f00 + dest.m12 * f01 + dest.m22 * f02;
            p03 = dest.m03 * f00 + dest.m13 * f01 + dest.m23 * f02;

            p10 = dest.m00 * f10 + dest.m10 * f11 + dest.m20 * f12;
            p11 = dest.m01 * f10 + dest.m11 * f11 + dest.m21 * f12;
            p12 = dest.m02 * f10 + dest.m12 * f11 + dest.m22 * f12;
            p13 = dest.m03 * f10 + dest.m13 * f11 + dest.m23 * f12;

            p20 = dest.m00 * f20 + dest.m10 * f21 + dest.m20 * f22;
            p21 = dest.m01 * f20 + dest.m11 * f21 + dest.m21 * f22;
            p22 = dest.m02 * f20 + dest.m12 * f21 + dest.m22 * f22;
            p23 = dest.m03 * f20 + dest.m13 * f21 + dest.m23 * f22;

            dest.m00 = p00; dest.m01 = p01; dest.m02 = p02; dest.m03 = p03;
            dest.m10 = p10; dest.m11 = p11; dest.m12 = p12; dest.m13 = p13;
            dest.m20 = p20; dest.m21 = p21; dest.m22 = p22; dest.m23 = p23;

            // rotZ matrix
            f00 = crz; f01 = -srz; f02 = ZERO;
            f10 = srz; f11 = crz; f12 = ZERO;
            f20 = ZERO; f21 = ZERO; f22 = ONE;

            // Apply rotation Z to the destination matrix
            p00 = dest.m00 * f00 + dest.m10 * f01 + dest.m20 * f02;
            p01 = dest.m01 * f00 + dest.m11 * f01 + dest.m21 * f02;
            p02 = dest.m02 * f00 + dest.m12 * f01 + dest.m22 * f02;
            p03 = dest.m03 * f00 + dest.m13 * f01 + dest.m23 * f02;

            p10 = dest.m00 * f10 + dest.m10 * f11 + dest.m20 * f12;
            p11 = dest.m01 * f10 + dest.m11 * f11 + dest.m21 * f12;
            p12 = dest.m02 * f10 + dest.m12 * f11 + dest.m22 * f12;
            p13 = dest.m03 * f10 + dest.m13 * f11 + dest.m23 * f12;

            p20 = dest.m00 * f20 + dest.m10 * f21 + dest.m20 * f22;
            p21 = dest.m01 * f20 + dest.m11 * f21 + dest.m21 * f22;
            p22 = dest.m02 * f20 + dest.m12 * f21 + dest.m22 * f22;
            p23 = dest.m03 * f20 + dest.m13 * f21 + dest.m23 * f22;

            dest.m00 = p00; dest.m01 = p01; dest.m02 = p02; dest.m03 = p03;
            dest.m10 = p10; dest.m11 = p11; dest.m12 = p12; dest.m13 = p13;
            dest.m20 = p20; dest.m21 = p21; dest.m22 = p22; dest.m23 = p23;

            return Scale.scale(scaleX, scaleY, scaleZ, dest);
        }

        public static Mat4 createView(float posX, float posY, float posZ,
                                      float rotX, float rotY, float rotZ,
                                      Mat4 dest) {
            dest.identity();

            double rx = Math.toRadians(rotX);
            double ry = Math.toRadians(rotY);
            double rz = Math.toRadians(rotZ);

            float crx = (float) Math.cos(rx);
            float cry = (float) Math.cos(ry);
            float crz = (float) Math.cos(rz);

            float srx = (float) Math.sin(rx);
            float sry = (float) Math.sin(ry);
            float srz = (float) Math.sin(rz);

            float f00, f01, f02;
            float f10, f11, f12;
            float f20, f21, f22;

            float p00, p01, p02, p03;
            float p10, p11, p12, p13;
            float p20, p21, p22, p23;

            // rotX matrix
            f00 = ONE; f01 = ZERO; f02 = ZERO;
            f10 = ZERO; f11 = crx; f12 = srx;
            f20 = ZERO; f21 = -srx; f22 = crx;

            // Apply rotation X to the destination matrix
            p00 = dest.m00 * f00 + dest.m10 * f01 + dest.m20 * f02;
            p01 = dest.m01 * f00 + dest.m11 * f01 + dest.m21 * f02;
            p02 = dest.m02 * f00 + dest.m12 * f01 + dest.m22 * f02;
            p03 = dest.m03 * f00 + dest.m13 * f01 + dest.m23 * f02;

            p10 = dest.m00 * f10 + dest.m10 * f11 + dest.m20 * f12;
            p11 = dest.m01 * f10 + dest.m11 * f11 + dest.m21 * f12;
            p12 = dest.m02 * f10 + dest.m12 * f11 + dest.m22 * f12;
            p13 = dest.m03 * f10 + dest.m13 * f11 + dest.m23 * f12;

            p20 = dest.m00 * f20 + dest.m10 * f21 + dest.m20 * f22;
            p21 = dest.m01 * f20 + dest.m11 * f21 + dest.m21 * f22;
            p22 = dest.m02 * f20 + dest.m12 * f21 + dest.m22 * f22;
            p23 = dest.m03 * f20 + dest.m13 * f21 + dest.m23 * f22;

            dest.m00 = p00; dest.m01 = p01; dest.m02 = p02; dest.m03 = p03;
            dest.m10 = p10; dest.m11 = p11; dest.m12 = p12; dest.m13 = p13;
            dest.m20 = p20; dest.m21 = p21; dest.m22 = p22; dest.m23 = p23;

            // rotY matrix
            f00 = cry; f01 = ZERO; f02 = -sry;
            f10 = ZERO; f11 = ONE; f12 = ZERO;
            f20 = sry; f21 = ZERO; f22 = cry;

            // Apply rotation Y to the destination matrix
            p00 = dest.m00 * f00 + dest.m10 * f01 + dest.m20 * f02;
            p01 = dest.m01 * f00 + dest.m11 * f01 + dest.m21 * f02;
            p02 = dest.m02 * f00 + dest.m12 * f01 + dest.m22 * f02;
            p03 = dest.m03 * f00 + dest.m13 * f01 + dest.m23 * f02;

            p10 = dest.m00 * f10 + dest.m10 * f11 + dest.m20 * f12;
            p11 = dest.m01 * f10 + dest.m11 * f11 + dest.m21 * f12;
            p12 = dest.m02 * f10 + dest.m12 * f11 + dest.m22 * f12;
            p13 = dest.m03 * f10 + dest.m13 * f11 + dest.m23 * f12;

            p20 = dest.m00 * f20 + dest.m10 * f21 + dest.m20 * f22;
            p21 = dest.m01 * f20 + dest.m11 * f21 + dest.m21 * f22;
            p22 = dest.m02 * f20 + dest.m12 * f21 + dest.m22 * f22;
            p23 = dest.m03 * f20 + dest.m13 * f21 + dest.m23 * f22;

            dest.m00 = p00; dest.m01 = p01; dest.m02 = p02; dest.m03 = p03;
            dest.m10 = p10; dest.m11 = p11; dest.m12 = p12; dest.m13 = p13;
            dest.m20 = p20; dest.m21 = p21; dest.m22 = p22; dest.m23 = p23;

            // rotZ matrix
            f00 = crz; f01 = -srz; f02 = ZERO;
            f10 = srz; f11 = crz; f12 = ZERO;
            f20 = ZERO; f21 = ZERO; f22 = ONE;

            // Apply rotation Z to the destination matrix
            p00 = dest.m00 * f00 + dest.m10 * f01 + dest.m20 * f02;
            p01 = dest.m01 * f00 + dest.m11 * f01 + dest.m21 * f02;
            p02 = dest.m02 * f00 + dest.m12 * f01 + dest.m22 * f02;
            p03 = dest.m03 * f00 + dest.m13 * f01 + dest.m23 * f02;

            p10 = dest.m00 * f10 + dest.m10 * f11 + dest.m20 * f12;
            p11 = dest.m01 * f10 + dest.m11 * f11 + dest.m21 * f12;
            p12 = dest.m02 * f10 + dest.m12 * f11 + dest.m22 * f12;
            p13 = dest.m03 * f10 + dest.m13 * f11 + dest.m23 * f12;

            p20 = dest.m00 * f20 + dest.m10 * f21 + dest.m20 * f22;
            p21 = dest.m01 * f20 + dest.m11 * f21 + dest.m21 * f22;
            p22 = dest.m02 * f20 + dest.m12 * f21 + dest.m22 * f22;
            p23 = dest.m03 * f20 + dest.m13 * f21 + dest.m23 * f22;

            dest.m00 = p00; dest.m01 = p01; dest.m02 = p02; dest.m03 = p03;
            dest.m10 = p10; dest.m11 = p11; dest.m12 = p12; dest.m13 = p13;
            dest.m20 = p20; dest.m21 = p21; dest.m22 = p22; dest.m23 = p23;

            // translation section

            dest.m30 += dest.m00 * posX + dest.m10 * posY + dest.m20 * posZ;
            dest.m31 += dest.m01 * posX + dest.m11 * posY + dest.m21 * posZ;
            dest.m32 += dest.m02 * posX + dest.m12 * posY + dest.m22 * posZ;
            dest.m33 += dest.m03 * posX + dest.m13 * posY + dest.m23 * posZ;

            return dest;
        }

//    public static Mat4 createm(Camera camera, float staticPositionY)
//    {
//        Mat4 m = new Mat4();
//        Mat4.rotate((float) java.lang.Math.toRadians(camera.getPitch()), Vec3.xAxis, m, m);
//        Mat4.rotate((float) java.lang.Math.toRadians(camera.getYaw()), Vec3.yAxis, m, m);
//        Mat4.rotate((float) java.lang.Math.toRadians(camera.getRoll()), Vec3.zAxis, m, m);
//        Vec3 positiveCameraPosition = new Vec3(camera.getPosition().x, staticPositionY, camera.getPosition().z);
//        Vec3 negativeCameraPosition = new Vec3(-camera.getPosition().x, -staticPositionY, -camera.getPosition().z);
//        Mat4.translate(negativeCameraPosition, m, m);
//        return m;
//    }
//
//    public static Mat4 createm(Camera camera) {
//        Mat4 m = new Mat4();
//        m.identity();
//        m.rotate((float) java.lang.Math.toRadians(camera.getPitch()), Vec3.xAxis);
//        m.rotate((float) java.lang.Math.toRadians(camera.getYaw()), Vec3.yAxis);
//        m.rotate((float) java.lang.Math.toRadians(camera.getRoll()), Vec3.zAxis);
//
//        Vec3 cameraPos = camera.getPosition();
//        Vec3 negativeCameraPos = new Vec3(-cameraPos.x, -cameraPos.y, -cameraPos.z);
//
//        m.translate(negativeCameraPos);
//
//        return m;
//    }

        /**
         * Calculates the interpolation of a point within a triangle using barycentric coordinates.
         * This method takes three 3D points vec3_1, vec3_2, and vec3_3 representing the vertices of a triangle,
         * and a 2D point pos within the triangle. It then computes the barycentric coordinates l1, l2, and l3
         * which determine the weight of each vertex in the interpolation.
         * The barycentric coordinates are calculated based on the determinant of the triangle formed by the three points.
         * The interpolation is then computed using the barycentric coordinates to blend the values of vec3_1.y, vec3_2.y, and vec3_3.y.
         * @param vec3_1 The first vertex of the triangle.
         * @param vec3_2 The second vertex of the triangle.
         * @param vec3_3 The third vertex of the triangle.
         * @param pos The 2D point within the triangle for interpolation.
         * @return The interpolated value at the given 2D point within the triangle.
         */
        public static float barryCentric(Vec3 vec3_1, Vec3 vec3_2, Vec3 vec3_3, Vec2 pos)
        {
            float det = (vec3_2.z - vec3_3.z) * (vec3_1.x - vec3_3.x) + (vec3_3.x - vec3_2.x) * (vec3_1.z - vec3_3.z);
            float l1 = ((vec3_2.z - vec3_3.z) * (pos.x - vec3_3.x) + (vec3_3.x - vec3_2.x) * (pos.y - vec3_3.z)) / det;
            float l2 = ((vec3_3.z - vec3_1.z) * (pos.x - vec3_3.x) + (vec3_1.x - vec3_3.x) * (pos.y - vec3_3.z)) / det;
            float l3 = 1.0f - l1 - l2;
            return l1 * vec3_1.y + l2 * vec3_2.y + l3 * vec3_3.y;
        }
    }
}
