package framework.lang;

import java.nio.FloatBuffer;

public class Mat4_E
{
    public float[][] m;

    public Mat4_E()
    {
        identity();
    }

    public Mat4_E(Mat4_E src)
    {
        m = src.m;
    }

    public String toString()
    {
        String buf = String.valueOf(
                m[0][0]) + ' ' + m[1][0] + ' ' + m[2][0] + ' ' + m[3][0] + '\n' +
                m[0][1] + ' ' + m[1][1] + ' ' + m[2][1] + ' ' + m[3][1] + '\n' +
                m[0][2] + ' ' + m[1][2] + ' ' + m[2][2] + ' ' + m[3][2] + '\n' +
                m[0][3] + ' ' + m[1][3] + ' ' + m[2][3] + ' ' + m[3][3] + '\n';
        return buf;
    }

    public Mat4_E identity()
    {
        m = new float[4][4];
        m[0][0] = 1f;
        m[1][1] = 1f;
        m[2][2] = 1f;
        m[3][3] = 1f;
        return this;
    }

    public static Mat4_E identity(Mat4_E m)
    {
        m.m = new float[4][4];
        m.m[0][0] = 1.0F;
        m.m[1][1] = 1.0F;
        m.m[2][2] = 1.0F;
        m.m[3][3] = 1.0F;
        return m;
    }

    public Mat4_E setZero()
    {
        return setZero(this);
    }

    public static Mat4_E setZero(Mat4_E m)
    {
        m.m = new float[4][4];
        return m;
    }

    public Mat4_E load(Mat4_E src)
    {
        return load(src, this);
    }

    public static Mat4_E load(Mat4_E src, Mat4_E dest)
    {
        dest.m = src.m;
        return dest;
    }

    public Mat4_E load(FloatBuffer buf)
    {
        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 4; j++)
                m[i][j] = buf.get();
        return this;
    }

    public Mat4_E loadTranspose(FloatBuffer buf)
    {
        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 4; j++)
                m[j][i] = buf.get();
        return this;
    }

    public Mat4_E store(FloatBuffer buf)
    {
        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 4; j++)
                buf.put(m[i][j]);
        return this;
    }

    public Mat4_E storeTranspose(FloatBuffer buf)
    {
        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 4; j++)
                buf.put(m[j][i]);
        return this;
    }

    public Mat4_E store3f(FloatBuffer buf)
    {
        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 3; j++)
                buf.put(m[i][j]);
        return this;
    }

    public static Mat4_E add(Mat4_E left, Mat4_E right, Mat4_E dest)
    {
        if(dest == null)
            dest = new Mat4_E();

        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 4; j++)
                dest.m[i][j] = left.m[i][j] + right.m[i][j];

        return dest;
    }

    public static Mat4_E sub(Mat4_E left, Mat4_E right, Mat4_E dest)
    {
        if(dest == null)
            dest = new Mat4_E();

        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 4; j++)
                dest.m[i][j] = left.m[i][j] - right.m[i][j];

        return dest;
    }

    public static Mat4_E mul(Mat4_E left, Mat4_E right, Mat4_E dest)
    {
        if(dest == null)
            dest = new Mat4_E();

        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 4; j++)
                dest.m[i][j] = left.m[0][j] * right.m[i][0] +
                        left.m[1][j] * right.m[i][1] +
                        left.m[2][j] * right.m[i][2] +
                        left.m[3][j] * right.m[i][3];

        return dest;
    }

    public static Vec3 transform(Mat4_E left, Vec3 right, Vec3 dest)
    {
        if(dest == null)
            dest = new Vec3();
        dest.x = left.m[0][0] * right.x + left.m[1][0] * right.y + left.m[2][0] * right.z;
        dest.y = left.m[0][1] * right.x + left.m[1][1] * right.y + left.m[2][1] * right.z;
        dest.z = left.m[0][2] * right.x + left.m[1][2] * right.y + left.m[2][2] * right.z;
        return dest;
    }

    public static Vec4 transform(Mat4_E left, Vec4 right, Vec4 dest) {
        if (dest == null)
            dest = new Vec4();
        dest.x = left.m[0][0] * right.x + left.m[1][0] * right.y + left.m[2][0] * right.z + left.m[3][0] * right.w;
        dest.y = left.m[0][1] * right.x + left.m[1][1] * right.y + left.m[2][1] * right.z + left.m[3][1] * right.w;
        dest.z = left.m[0][2] * right.x + left.m[1][2] * right.y + left.m[2][2] * right.z + left.m[3][2] * right.w;
        dest.w = left.m[0][3] * right.x + left.m[1][3] * right.y + left.m[2][3] * right.z + left.m[3][3] * right.w;
        return dest;
    }

    public Mat4_E transpose() {
        return transpose(this);
    }

    public Mat4_E translate(Vec2 vec) {
        return translate(vec, this);
    }

    public Mat4_E translate(Vec3 vec) {
        return translate(vec, this);
    }

    public Mat4_E scale(float scale)
    {
        return scale(new Vec3(scale));
    }

    public Mat4_E scale(Vec3 vec) {
        return scale(vec, this, this);
    }

    public static Mat4_E scale(Vec3 vec, Mat4_E src, Mat4_E dest) {
        if (dest == null)
            dest = new Mat4_E();

        for(int i = 0; i < 4; i++)
        {
            dest.m[0][i] = src.m[0][i] * vec.x;
            dest.m[1][i] = src.m[1][i] * vec.y;
            dest.m[2][i] = src.m[2][i] * vec.z;
        }

        return dest;
    }

    public Mat4_E rotate(float angle, Vec3 axis) {
        return rotate(angle, axis, this);
    }

    public Mat4_E rotate(float angle, Vec3 axis, Mat4_E dest) {
        return rotate(angle, axis, this, dest);
    }

    public static Mat4_E rotate(float angle, Vec3 axis, Mat4_E src, Mat4_E dest) {
        if (dest == null) {
            dest = new Mat4_E();
        }

        float c = Math.cos(angle);
        float s = Math.sin(angle);
        float omc = 1f - c; // omc means one minus cosine

        // axis and its sine
        float xy = axis.x * axis.y;
        float xz = axis.x * axis.z;
        float yz = axis.y * axis.z;
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

        for(int i = 0; i < 4; i++)
        {
            dest.m[0][i] = src.m[0][i] * f00 + src.m[1][i] * f01 + src.m[2][i] * f02;
            dest.m[1][i] = src.m[0][i] * f10 + src.m[1][i] * f11 + src.m[2][i] * f12;
            dest.m[2][i] = src.m[0][i] * f20 + src.m[1][i] * f21 + src.m[2][i] * f22;
        }

        return dest;
    }

    public Mat4_E translate(Vec3 vec, Mat4_E dest) {
        return translate(vec, this, dest);
    }

    public static Mat4_E translate(Vec3 vec, Mat4_E src, Mat4_E dest) {
        if (dest == null) {
            dest = new Mat4_E();
        }

        for(int i = 0; i < 4; i++)
            dest.m[3][i] += src.m[0][i] * vec.x + src.m[1][i] * vec.y + src.m[2][i] * vec.z;

        return dest;
    }

    public Mat4_E translate(Vec2 vec, Mat4_E dest) {
        return translate(vec, this, dest);
    }

    public static Mat4_E translate(Vec2 vec, Mat4_E src, Mat4_E dest) {
        if (dest == null) {
            dest = new Mat4_E();
        }

        for(int i = 0; i < 4; i++)
            dest.m[3][i] += src.m[0][i] * vec.x + src.m[1][i] * vec.y;

        return dest;
    }

    public Mat4_E transpose(Mat4_E dest) {
        return transpose(this, dest);
    }

    public static Mat4_E transpose(Mat4_E src, Mat4_E dest) {
        if (dest == null) {
            dest = new Mat4_E();
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

    public Mat4_E invert()
    {
        return invert(this, this);
    }

    public static Mat4_E invert(Mat4_E src, Mat4_E dest)
    {
        float determinant = src.determinant();
        if (determinant != 0.0F) {
            if (dest == null) {
                dest = new Mat4_E();
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

    public Mat4_E negate() {
        return negate(this);
    }

    public Mat4_E negate(Mat4_E dest) {
        return negate(this, dest);
    }

    public static Mat4_E negate(Mat4_E src, Mat4_E dest) {
        if (dest == null) {
            dest = new Mat4_E();
        }

        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 4; j++)
                dest.m[i][j] = -src.m[i][j];

        return dest;
    }
}
