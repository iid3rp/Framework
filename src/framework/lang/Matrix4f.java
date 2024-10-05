package framework.lang;

import java.nio.FloatBuffer;

public class Matrix4f
{
    public float[][] m;

    public Matrix4f() {
        identity();
    }

    public Matrix4f(Matrix4f src) {
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

    public Matrix4f identity() {
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

    public static Matrix4f identity(Matrix4f m) {

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

    public Matrix4f setZero()
    {
        return setZero(this);
    }

    public static Matrix4f setZero(Matrix4f m)
    {
        m.m = new float[4][4];
        return m;
    }

    public Matrix4f load(Matrix4f src)
    {
        return load(src, this);
    }

    public static Matrix4f load(Matrix4f src, Matrix4f dest)
    {
        dest.m = src.m;
        return dest;
    }

    public Matrix4f load(FloatBuffer buf) {
        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 4; j++)
                m[i][j] = buf.get();
        return this;
    }

    public Matrix4f loadTranspose(FloatBuffer buf) {
        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 4; j++)
                m[j][i] = buf.get();
        return this;
    }

    public FloatBuffer store(FloatBuffer buf) {
        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 4; j++)
                buf.put(m[i][j]);
        return buf;
    }

    public Matrix4f storeTranspose(FloatBuffer buf) {
        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 4; j++)
                buf.put(m[j][i]);
        return this;
    }

    public Matrix4f store3f(FloatBuffer buf) {
        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 3; j++)
                buf.put(m[i][j]);
        return this;
    }

    public static Matrix4f add(Matrix4f left, Matrix4f right, Matrix4f dest) {
        if (dest == null)
            dest = new Matrix4f();

        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 4; j++)
                dest.m[i][j] = left.m[i][j] + right.m[i][j];

        return dest;
    }

    public static Matrix4f sub(Matrix4f left, Matrix4f right, Matrix4f dest) {
        if (dest == null)
            dest = new Matrix4f();

        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 4; j++)
                dest.m[i][j] = left.m[i][j] - right.m[i][j];

        return dest;
    }

    public static Matrix4f mul(Matrix4f left, Matrix4f right, Matrix4f dest)
    {
        if (dest == null)
            dest = new Matrix4f();

        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 4; j++)
                dest.m[i][j] = left.m[0][j] * right.m[i][0] +
                                left.m[1][j] * right.m[i][1] +
                                left.m[2][j] * right.m[i][2] +
                                left.m[3][j] * right.m[i][3];

        return dest;
    }

    public static Vector4f transform(Matrix4f left, Vector4f right, Vector4f dest) {
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

    public Matrix4f transpose() {
        return transpose(this);
    }

    public Matrix4f translate(Vector2f vec) {
        return translate(vec, this);
    }

    public Matrix4f translate(Vector3f vec) {
        return translate(vec, this);
    }

    public Matrix4f scale(Vector3f vec) {
        return scale(vec, this, this);
    }

    public static Matrix4f scale(Vector3f vec, Matrix4f src, Matrix4f dest) {
        if (dest == null) {
            dest = new Matrix4f();
        }

        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 4; j++)
                dest.m[i][j] = src.m[i][j] * vec.x;

        return dest;
    }

    public Matrix4f rotate(float angle, Vector3f axis) {
        return rotate(angle, axis, this);
    }

    public Matrix4f rotate(float angle, Vector3f axis, Matrix4f dest) {
        return rotate(angle, axis, this, dest);
    }

    public static Matrix4f rotate(float angle, Vector3f axis, Matrix4f src, Matrix4f dest) {
        if (dest == null) {
            dest = new Matrix4f();
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

    public Matrix4f translate(Vector3f vec, Matrix4f dest) {
        return translate(vec, this, dest);
    }

    public static Matrix4f translate(Vector3f vec, Matrix4f src, Matrix4f dest) {
        if (dest == null) {
            dest = new Matrix4f();
        }

        dest.m[3][0] += src.m[0][0] * vec.x + src.m[1][0] * vec.y + src.m[2][0] * vec.z;
        dest.m[3][1] += src.m[0][1] * vec.x + src.m[1][1] * vec.y + src.m[2][1] * vec.z;
        dest.m[3][2] += src.m[0][2] * vec.x + src.m[1][2] * vec.y + src.m[2][2] * vec.z;
        dest.m[3][3] += src.m[0][3] * vec.x + src.m[1][3] * vec.y + src.m[2][3] * vec.z;
        return dest;
    }

    public Matrix4f translate(Vector2f vec, Matrix4f dest) {
        return translate(vec, this, dest);
    }

    public static Matrix4f translate(Vector2f vec, Matrix4f src, Matrix4f dest) {
        if (dest == null) {
            dest = new Matrix4f();
        }

        dest.m[3][0] += src.m[0][0] * vec.x + src.m[1][0] * vec.y;
        dest.m[3][1] += src.m[0][1] * vec.x + src.m[1][1] * vec.y;
        dest.m[3][2] += src.m[0][2] * vec.x + src.m[1][2] * vec.y;
        dest.m[3][3] += src.m[0][3] * vec.x + src.m[1][3] * vec.y;
        return dest;
    }

    public Matrix4f transpose(Matrix4f dest) {
        return transpose(this, dest);
    }

    public static Matrix4f transpose(Matrix4f src, Matrix4f dest) {
        if (dest == null) {
            dest = new Matrix4f();
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

    public Matrix4f invert()
    {
        return invert(this, this);
    }

    public static Matrix4f invert(Matrix4f src, Matrix4f dest)
    {
        float determinant = src.determinant();
        if (determinant != 0.0F) {
            if (dest == null) {
                dest = new Matrix4f();
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

    public Matrix4f negate() {
        return negate(this);
    }

    public Matrix4f negate(Matrix4f dest) {
        return negate(this, dest);
    }

    public static Matrix4f negate(Matrix4f src, Matrix4f dest) {
        if (dest == null) {
            dest = new Matrix4f();
        }

        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 4; j++)
                dest.m[i][j] = -src.m[i][j];

        return dest;
    }
}
