package framework.lang;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.nio.FloatBuffer;

public class Mat4
{
    public float[][] m;

    public Mat4() {
        identity();
    }

    public Mat4(Mat4 src) {
        m = src.m;
    }

    public String toString() {
        String buf = String.valueOf(
                m[0][0]) + ' ' + m[1][0] + ' ' + m[2][0] + ' ' + m[3][0] + '\n' +
                m[0][1] + ' ' + m[1][1] + ' ' + m[2][1] + ' ' + m[3][1] + '\n' +
                m[0][2] + ' ' + m[1][2] + ' ' + m[2][2] + ' ' + m[3][2] + '\n' +
                m[0][3] + ' ' + m[1][3] + ' ' + m[2][3] + ' ' + m[3][3] + '\n';
        return buf;
    }

    public Mat4 identity() {
        if(m == null)
            m = new float[4][4];
        m[0][0] = 1f;
        m[0][1] = 0f;
        m[0][2] = 0f;
        m[0][3] = 0f;

        m[1][0] = 0f;
        m[1][1] = 1f;
        m[1][2] = 0f;
        m[1][3] = 0f;

        m[2][0] = 0f;
        m[2][1] = 0f;
        m[2][2] = 1f;
        m[2][3] = 0f;

        m[3][0] = 0f;
        m[3][1] = 0f;
        m[3][2] = 0f;
        m[3][3] = 1f;
        return this;
    }

    public static Mat4 identity(Mat4 m) {

        m.m[0][0] = 1.0F;
        m.m[0][1] = 0.0F;
        m.m[0][2] = 0.0F;
        m.m[0][3] = 0.0F;

        m.m[1][0] = 0.0F;
        m.m[1][1] = 1.0F;
        m.m[1][2] = 0.0F;
        m.m[1][3] = 0.0F;

        m.m[2][0] = 0.0F;
        m.m[2][1] = 0.0F;
        m.m[2][2] = 1.0F;
        m.m[2][3] = 0.0F;

        m.m[3][0] = 0.0F;
        m.m[3][1] = 0.0F;
        m.m[3][2] = 0.0F;
        m.m[3][3] = 1.0F;
        return m;
    }

    public Mat4 setZero()
    {
        return setZero(this);
    }

    public static Mat4 setZero(Mat4 m)
    {
        m.m = new float[4][4];
        return m;
    }

    public Mat4 load(Mat4 src)
    {
        return load(src, this);
    }

    public static Mat4 load(Mat4 src, Mat4 dest)
    {
        dest.m = src.m;
        return dest;
    }

    public Mat4 load(FloatBuffer buf) {
        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 4; j++)
                m[i][j] = buf.get();
        return this;
    }

    public Mat4 loadTranspose(FloatBuffer buf) {
        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 4; j++)
                m[j][i] = buf.get();
        return this;
    }

    public Mat4 store(FloatBuffer buf) {
        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 4; j++)
                buf.put(m[i][j]);
        return this;
    }

    public Mat4 storeTranspose(FloatBuffer buf) {
        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 4; j++)
                buf.put(m[j][i]);
        return this;
    }

    public Mat4 store3f(FloatBuffer buf) {
        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 3; j++)
                buf.put(m[i][j]);
        return this;
    }

    public static Mat4 add(Mat4 left, Mat4 right, Mat4 dest) {
        if (dest == null)
            dest = new Mat4();

        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 4; j++)
                dest.m[i][j] = left.m[i][j] + right.m[i][j];

        return dest;
    }

    public static Mat4 sub(Mat4 left, Mat4 right, Mat4 dest) {
        if (dest == null)
            dest = new Mat4();

        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 4; j++)
                dest.m[i][j] = left.m[i][j] - right.m[i][j];

        return dest;
    }

    public static Mat4 mul(Mat4 left, Mat4 right, Mat4 dest)
    {
        if (dest == null)
            dest = new Mat4();

        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 4; j++)
                dest.m[i][j] = left.m[0][j] * right.m[i][0] +
                                left.m[1][j] * right.m[i][1] +
                                left.m[2][j] * right.m[i][2] +
                                left.m[3][j] * right.m[i][3];

        return dest;
    }

    public static Vector4f transform(Mat4 left, Vector4f right, Vector4f dest) {
        if (dest == null) {
            dest = new Vector4f();
        }

        float x = left.m[0][0] * right.x + left.m[1][0] * right.y + left.m[2][0] * right.z + left.m[3][0] * right.w;
        float y = left.m[0][1] * right.x + left.m[1][1] * right.y + left.m[2][1] * right.z + left.m[3][1] * right.w;
        float z = left.m[0][2] * right.x + left.m[1][2] * right.y + left.m[2][2] * right.z + left.m[3][2] * right.w;
        float w = left.m[0][3] * right.x + left.m[1][3] * right.y + left.m[2][3] * right.z + left.m[3][3] * right.w;
        dest.x = x;
        dest.y = y;
        dest.z = z;
        dest.w = w;
        return dest;
    }

    public Mat4 transpose() {
        return transpose(this);
    }

    public Mat4 translate(Vector2f vec) {
        return translate(vec, this);
    }

    public Mat4 translate(Vector3f vec) {
        return translate(vec, this);
    }

    public Mat4 scale(Vector3f vec) {
        return scale(vec, this, this);
    }

    public static Mat4 scale(Vector3f vec, Mat4 src, Mat4 dest) {
        if (dest == null) {
            dest = new Mat4();
        }

        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 4; j++)
                dest.m[i][j] = src.m[i][j] * vec.x;

        return dest;
    }

    public Mat4 rotate(float angle, Vec3 axis) {
        return rotate(angle, axis, this);
    }

    public Mat4 rotate(float angle, Vec3 axis, Mat4 dest) {
        return rotate(angle, axis, this, dest);
    }

    public static Mat4 rotate(float angle, Vec3 axis, Mat4 src, Mat4 dest) {
        if (dest == null) {
            dest = new Mat4();
        }

        float c = (float) Math.cos(angle);
        float s = (float) Math.sin(angle);
        float one_minus_c = 1.0F - c;
        float xy = axis.x * axis.y;
        float yz = axis.y * axis.z;
        float xz = axis.x * axis.z;
        float xs = axis.x * s;
        float ys = axis.y * s;
        float zs = axis.z * s;
        float f00 = axis.x * axis.x * one_minus_c + c;
        float f01 = xy * one_minus_c + zs;
        float f02 = xz * one_minus_c - ys;
        float f10 = xy * one_minus_c - zs;
        float f11 = axis.y * axis.y * one_minus_c + c;
        float f12 = yz * one_minus_c + xs;
        float f20 = xz * one_minus_c + ys;
        float f21 = yz * one_minus_c - xs;
        float f22 = axis.z * axis.z * one_minus_c + c;
        float t00 = src.m[0][0] * f00 + src.m[1][0] * f01 + src.m[2][0] * f02;
        float t01 = src.m[0][1] * f00 + src.m[1][1] * f01 + src.m[2][1] * f02;
        float t02 = src.m[0][2] * f00 + src.m[1][2] * f01 + src.m[2][2] * f02;
        float t03 = src.m[0][3] * f00 + src.m[1][3] * f01 + src.m[2][3] * f02;
        float t10 = src.m[0][0] * f10 + src.m[1][0] * f11 + src.m[2][0] * f12;
        float t11 = src.m[0][1] * f10 + src.m[1][1] * f11 + src.m[2][1] * f12;
        float t12 = src.m[0][2] * f10 + src.m[1][2] * f11 + src.m[2][2] * f12;
        float t13 = src.m[0][3] * f10 + src.m[1][3] * f11 + src.m[2][3] * f12;
        dest.m[2][0] = src.m[0][0] * f20 + src.m[1][0] * f21 + src.m[2][0] * f22;
        dest.m[2][1] = src.m[0][1] * f20 + src.m[1][1] * f21 + src.m[2][1] * f22;
        dest.m[2][2] = src.m[0][2] * f20 + src.m[1][2] * f21 + src.m[2][2] * f22;
        dest.m[2][3] = src.m[0][3] * f20 + src.m[1][3] * f21 + src.m[2][3] * f22;
        dest.m[0][0] = t00;
        dest.m[0][1] = t01;
        dest.m[0][2] = t02;
        dest.m[0][3] = t03;
        dest.m[1][0] = t10;
        dest.m[1][1] = t11;
        dest.m[1][2] = t12;
        dest.m[1][3] = t13;
        return dest;
    }

    public Mat4 translate(Vector3f vec, Mat4 dest) {
        return translate(vec, this, dest);
    }

    public static Mat4 translate(Vector3f vec, Mat4 src, Mat4 dest) {
        if (dest == null) {
            dest = new Mat4();
        }

        dest.m[3][0] += src.m[0][0] * vec.x + src.m[1][0] * vec.y + src.m[2][0] * vec.z;
        dest.m[3][1] += src.m[0][1] * vec.x + src.m[1][1] * vec.y + src.m[2][1] * vec.z;
        dest.m[3][2] += src.m[0][2] * vec.x + src.m[1][2] * vec.y + src.m[2][2] * vec.z;
        dest.m[3][3] += src.m[0][3] * vec.x + src.m[1][3] * vec.y + src.m[2][3] * vec.z;
        return dest;
    }

    public Mat4 translate(Vector2f vec, Mat4 dest) {
        return translate(vec, this, dest);
    }

    public static Mat4 translate(Vector2f vec, Mat4 src, Mat4 dest) {
        if (dest == null) {
            dest = new Mat4();
        }

        dest.m[3][0] += src.m[0][0] * vec.x + src.m[1][0] * vec.y;
        dest.m[3][1] += src.m[0][1] * vec.x + src.m[1][1] * vec.y;
        dest.m[3][2] += src.m[0][2] * vec.x + src.m[1][2] * vec.y;
        dest.m[3][3] += src.m[0][3] * vec.x + src.m[1][3] * vec.y;
        return dest;
    }

    public Mat4 transpose(Mat4 dest) {
        return transpose(this, dest);
    }

    public static Mat4 transpose(Mat4 src, Mat4 dest) {
        if (dest == null) {
            dest = new Mat4();
        }
        
        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 4; j++)
                dest.m[i][j] = src.m[j][i];
        
        return dest;
    }

    public float determinant() {
        float f = m[0][0] * (m[1][1] * m[2][2] * m[3][3] + m[1][2] * m[2][3] * m[3][1] + m[1][3] * m[2][1] * m[3][2] - m[1][3] * m[2][2] * m[3][1] - m[1][1] * m[2][3] * m[3][2] - m[1][2] * m[2][1] * m[3][3]);
        f -= m[0][1] * (m[1][0] * m[2][2] * m[3][3] + m[1][2] * m[2][3] * m[3][0] + m[1][3] * m[2][0] * m[3][2] - m[1][3] * m[2][2] * m[3][0] - m[1][0] * m[2][3] * m[3][2] - m[1][2] * m[2][0] * m[3][3]);
        f += m[0][2] * (m[1][0] * m[2][1] * m[3][3] + m[1][1] * m[2][3] * m[3][0] + m[1][3] * m[2][0] * m[3][1] - m[1][3] * m[2][1] * m[3][0] - m[1][0] * m[2][3] * m[3][1] - m[1][1] * m[2][0] * m[3][3]);
        f -= m[0][3] * (m[1][0] * m[2][1] * m[3][2] + m[1][1] * m[2][2] * m[3][0] + m[1][2] * m[2][0] * m[3][1] - m[1][2] * m[2][1] * m[3][0] - m[1][0] * m[2][2] * m[3][1] - m[1][1] * m[2][0] * m[3][2]);
        return f;
    }

    private static float determinant3x3(float t00, float t01, float t02, float t10, float t11, float t12, float t20, float t21, float t22)
    {
        return t00 * (t11 * t22 - t12 * t21) + t01 * (t12 * t20 - t10 * t22) + t02 * (t10 * t21 - t11 * t20);
    }

    public Mat4 invert()
    {
        return invert(this, this);
    }

    public static Mat4 invert(Mat4 src, Mat4 dest)
    {
        float determinant = src.determinant();
        if (determinant != 0.0F) {
            if (dest == null) {
                dest = new Mat4();
            }

            float determinant_inv = 1.0F / determinant;
            float t00 = determinant3x3(src.m[1][1], src.m[1][2], src.m[1][3], src.m[2][1], src.m[2][2], src.m[2][3], src.m[3][1], src.m[3][2], src.m[3][3]);
            float t01 = -determinant3x3(src.m[1][0], src.m[1][2], src.m[1][3], src.m[2][0], src.m[2][2], src.m[2][3], src.m[3][0], src.m[3][2], src.m[3][3]);
            float t02 = determinant3x3(src.m[1][0], src.m[1][1], src.m[1][3], src.m[2][0], src.m[2][1], src.m[2][3], src.m[3][0], src.m[3][1], src.m[3][3]);
            float t03 = -determinant3x3(src.m[1][0], src.m[1][1], src.m[1][2], src.m[2][0], src.m[2][1], src.m[2][2], src.m[3][0], src.m[3][1], src.m[3][2]);
            float t10 = -determinant3x3(src.m[0][1], src.m[0][2], src.m[0][3], src.m[2][1], src.m[2][2], src.m[2][3], src.m[3][1], src.m[3][2], src.m[3][3]);
            float t11 = determinant3x3(src.m[0][0], src.m[0][2], src.m[0][3], src.m[2][0], src.m[2][2], src.m[2][3], src.m[3][0], src.m[3][2], src.m[3][3]);
            float t12 = -determinant3x3(src.m[0][0], src.m[0][1], src.m[0][3], src.m[2][0], src.m[2][1], src.m[2][3], src.m[3][0], src.m[3][1], src.m[3][3]);
            float t13 = determinant3x3(src.m[0][0], src.m[0][1], src.m[0][2], src.m[2][0], src.m[2][1], src.m[2][2], src.m[3][0], src.m[3][1], src.m[3][2]);
            float t20 = determinant3x3(src.m[0][1], src.m[0][2], src.m[0][3], src.m[1][1], src.m[1][2], src.m[1][3], src.m[3][1], src.m[3][2], src.m[3][3]);
            float t21 = -determinant3x3(src.m[0][0], src.m[0][2], src.m[0][3], src.m[1][0], src.m[1][2], src.m[1][3], src.m[3][0], src.m[3][2], src.m[3][3]);
            float t22 = determinant3x3(src.m[0][0], src.m[0][1], src.m[0][3], src.m[1][0], src.m[1][1], src.m[1][3], src.m[3][0], src.m[3][1], src.m[3][3]);
            float t23 = -determinant3x3(src.m[0][0], src.m[0][1], src.m[0][2], src.m[1][0], src.m[1][1], src.m[1][2], src.m[3][0], src.m[3][1], src.m[3][2]);
            float t30 = -determinant3x3(src.m[0][1], src.m[0][2], src.m[0][3], src.m[1][1], src.m[1][2], src.m[1][3], src.m[2][1], src.m[2][2], src.m[2][3]);
            float t31 = determinant3x3(src.m[0][0], src.m[0][2], src.m[0][3], src.m[1][0], src.m[1][2], src.m[1][3], src.m[2][0], src.m[2][2], src.m[2][3]);
            float t32 = -determinant3x3(src.m[0][0], src.m[0][1], src.m[0][3], src.m[1][0], src.m[1][1], src.m[1][3], src.m[2][0], src.m[2][1], src.m[2][3]);
            float t33 = determinant3x3(src.m[0][0], src.m[0][1], src.m[0][2], src.m[1][0], src.m[1][1], src.m[1][2], src.m[2][0], src.m[2][1], src.m[2][2]);
            dest.m[0][0] = t00 * determinant_inv;
            dest.m[1][1] = t11 * determinant_inv;
            dest.m[2][2] = t22 * determinant_inv;
            dest.m[3][3] = t33 * determinant_inv;
            dest.m[0][1] = t10 * determinant_inv;
            dest.m[1][0] = t01 * determinant_inv;
            dest.m[2][0] = t02 * determinant_inv;
            dest.m[0][2] = t20 * determinant_inv;
            dest.m[1][2] = t21 * determinant_inv;
            dest.m[2][1] = t12 * determinant_inv;
            dest.m[0][3] = t30 * determinant_inv;
            dest.m[3][0] = t03 * determinant_inv;
            dest.m[1][3] = t31 * determinant_inv;
            dest.m[3][1] = t13 * determinant_inv;
            dest.m[3][2] = t23 * determinant_inv;
            dest.m[2][3] = t32 * determinant_inv;
            return dest;
        } else {
            return null;
        }
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

        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 4; j++)
                dest.m[i][j] = -src.m[i][j];

        return dest;
    }

    public Matrix4f matrix4f()
    {
        return new Matrix4f(
            m[0][0],
            m[0][1],
            m[0][2],
            m[0][3],

            m[1][0],
            m[1][1],
            m[1][2],
            m[1][3],

            m[2][0],
            m[2][1],
            m[2][2],
            m[2][3],

            m[3][0],
            m[3][1],
            m[3][2],
            m[3][3]
        );
    }
}
