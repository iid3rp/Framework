package framework.lang;

public class GeomMath
{
    private static volatile Mat4 m = new Mat4();
    private static volatile Vec2 v2 = new Vec2();
    private static volatile Vec3 v3 = new Vec3();
    private static volatile Vec4 v4 = new Vec4();

    public static Mat4 createTransformationMatrix(Vec2 position, Vec3 rotation, Vec2 scale)
    {
        m.identity();
        Mat4.translate(position, m, m);
        Mat4.rotate((float) Math.toRadians(rotation.x), Vec3.xAxis, m, m);
        Mat4.rotate((float) Math.toRadians(rotation.y), Vec3.yAxis, m, m);
        Mat4.rotate((float) Math.toRadians(rotation.z), Vec3.zAxis, m, m);
        Mat4.scale(new Vec3(scale.x, scale.y, 1f), m, m);
        return m;
    }

    public static Mat4 createTransformationMatrix(float posX, float posY, float rotX, float rotY, float rotZ, float scaleX, float scaleY)
    {
        m.identity();
        Mat4.translate(posX, posY, m, m);
        Mat4.rotate(Math.toRadians(rotX), 1f, 0f, 0f, m, m);
        Mat4.rotate(Math.toRadians(rotY), 0f, 1f, 0f, m, m);
        Mat4.rotate(Math.toRadians(rotZ), 0f, 0f, 1f, m, m);
        Mat4.scale(scaleX, scaleY, 1f, m, m);
        return m;
    }
    public static Mat4 createTransformationMatrix(float posX, float posY, float posZ, float rotX, float rotY, float rotZ, float scaleX, float scaleY, float scaleZ, Mat4 src, Mat4 dest)
    {
        return Matrix.createTransformation(posX, posY, posZ, rotX, rotY, rotZ,scaleX, scaleY, scaleZ, src);
    }

    public static Mat4 createTransformationMatrix(Vec3 vec3, float rotationX, float rotationY, float rotationZ, float scale)
    {
        m.identity();
        Mat4.translate(vec3, m, m);
        Mat4.rotate((float) Math.toRadians(rotationX), Vec3.xAxis, m, m);
        Mat4.rotate((float) Math.toRadians(rotationY), Vec3.yAxis, m, m);
        Mat4.rotate((float) Math.toRadians(rotationZ), Vec3.zAxis, m, m);
        Mat4.scale(new Vec3(scale), m, m);
        return m;
    }

//    public static Mat4 createm(Camera camera, float staticPositionY)
//    {
//        Mat4 m = new Mat4();
//        Mat4.rotate((float) java.Math.toRadians(camera.getPitch()), Vec3.xAxis, m, m);
//        Mat4.rotate((float) java.Math.toRadians(camera.getYaw()), Vec3.yAxis, m, m);
//        Mat4.rotate((float) java.Math.toRadians(camera.getRoll()), Vec3.zAxis, m, m);
//        Vec3 positiveCameraPosition = new Vec3(camera.getPosition().x, staticPositionY, camera.getPosition().z);
//        Vec3 negativeCameraPosition = new Vec3(-camera.getPosition().x, -staticPositionY, -camera.getPosition().z);
//        Mat4.translate(negativeCameraPosition, m, m);
//        return m;
//    }
//
//    public static Mat4 createm(Camera camera) {
//        Mat4 m = new Mat4();
//        m.identity();
//        m.rotate((float) java.Math.toRadians(camera.getPitch()), Vec3.xAxis);
//        m.rotate((float) java.Math.toRadians(camera.getYaw()), Vec3.yAxis);
//        m.rotate((float) java.Math.toRadians(camera.getRoll()), Vec3.zAxis);
//
//        Vec3 cameraPos = camera.getPosition();
//        Vec3 negativeCameraPos = new Vec3(-cameraPos.x, -cameraPos.y, -cameraPos.z);
//
//        m.translate(negativeCameraPos);
//
//        return m;
//    }

    /**
     * no one uses this constructor
     */
    private GeomMath() {}

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

    /**
     * Creates a transformation m based on the provided translation and scale.
     *
     * @param translation The 2D translation vector.
     * @param scale The 2D scale vector.
     * @return The transformation m.
     */
    public static Mat4 createTransformationMatrix(Vec2 translation, Vec2 scale)
    {
        Mat4 m = new Mat4();
        m.identity();
        m.scale(new Vec3(scale.x, scale.y, 1f));
        m.translate(new Vec3(translation.x, translation.y, 1), m);
        return m;
    }

    public static int abs(int n)
    {
        return (~n ^ 1) + 1;
    }

    public static float cosFromSin(float sin, float angle)
    {
        int quadrant = sin >= 0 ? (angle >= 0 ? 1 : 4) : (angle >= 0 ? 2 : 3);
        float cosSquared = 1 - sin * sin;
        float cos = (float) (Math.sqrt(cosSquared) * (quadrant == 1 || quadrant == 4 ? 1 : -1));
        return cos;
    }

    public static double sin(double angle)
    {
        return StrictMath.sin(angle);
    }

    public static float sin(float angle)
    {
        return (float) StrictMath.sin(angle);
    }

    public static float cos(float angle)
    {
        return (float) Math.cos(angle);
    }

    public static Mat4 createViewMatrix(float posX, float posY, float posZ, float rotX, float rotY, float rotZ, Mat4 src, Mat4 dest)
    {
        m.identity();
        Mat4.rotate(Math.toRadians(rotX), 1f, 0f, 0f, src, dest);
        Mat4.rotate(Math.toRadians(rotY), 0f, 1f, 0f, src, dest);
        Mat4.rotate(Math.toRadians(rotZ), 0f, 0f, 0f, src, dest);
        Mat4.translate(-posX, -posY, -posZ, dest, dest);
        return m;
    }

    public static class Matrix {
        public static final float ONE = 1.0f;
        public static final float ZERO = 0.0f;

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

            // ---------------------------------------------------------------------
            // Scaling section
            // ---------------------------------------------------------------------
            return Mat4.Scale.scale(scaleX, scaleY, scaleZ, dest);
        }
    }
}
