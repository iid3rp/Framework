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
        Mat4.rotate((float) java.lang.Math.toRadians(rotation.x), Vec3.xAxis, m, m);
        Mat4.rotate((float) java.lang.Math.toRadians(rotation.y), Vec3.yAxis, m, m);
        Mat4.rotate((float) java.lang.Math.toRadians(rotation.z), Vec3.zAxis, m, m);
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
        src.identity();
        Mat4.translate(posX, posY, posZ, src, dest);
        Mat4.rotate(Math.toRadians(rotX), 1f, 0f, 0f, src, dest);
        Mat4.rotate(Math.toRadians(rotY), 0f, 1f, 0f, src, dest);
        Mat4.rotate(Math.toRadians(rotZ), 0f, 0f, 1f, src, dest);
        Mat4.scale(scaleX, scaleY, scaleZ, m, m);
        return dest;
    }

    public static Mat4 createTransformationMatrix(Vec3 vec3, float rotationX, float rotationY, float rotationZ, float scale)
    {
        m.identity();
        Mat4.translate(vec3, m, m);
        Mat4.rotate((float) java.lang.Math.toRadians(rotationX), Vec3.xAxis, m, m);
        Mat4.rotate((float) java.lang.Math.toRadians(rotationY), Vec3.yAxis, m, m);
        Mat4.rotate((float) java.lang.Math.toRadians(rotationZ), Vec3.zAxis, m, m);
        Mat4.scale(new Vec3(scale), m, m);
        return m;
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
        float cos = (float) (java.lang.Math.sqrt(cosSquared) * (quadrant == 1 || quadrant == 4 ? 1 : -1));
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
        return (float) java.lang.Math.cos(angle);
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
}
