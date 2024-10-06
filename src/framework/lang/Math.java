package framework.lang;

import framework.entity.Camera;

public class Math
{
    private static Mat4 m = new Mat4();
    private static Vec2 v2 = new Vec2();
    private static Vec3 v3 = new Vec3();
    private static Vec4 v4 = new Vec4();

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

    public static Mat4 createViewMatrix(Camera camera, float staticPositionY)
    {
        Mat4 viewMatrix = new Mat4();
        Mat4.rotate((float) java.lang.Math.toRadians(camera.getPitch()), Vec3.xAxis, viewMatrix, viewMatrix);
        Mat4.rotate((float) java.lang.Math.toRadians(camera.getYaw()), Vec3.yAxis, viewMatrix, viewMatrix);
        Mat4.rotate((float) java.lang.Math.toRadians(camera.getRoll()), Vec3.zAxis, viewMatrix, viewMatrix);
        Vec3 positiveCameraPosition = new Vec3(camera.getPosition().x, staticPositionY, camera.getPosition().z);
        Vec3 negativeCameraPosition = new Vec3(-camera.getPosition().x, -staticPositionY, -camera.getPosition().z);
        Mat4.translate(negativeCameraPosition, viewMatrix, viewMatrix);
        return viewMatrix;
    }

    public static Mat4 createViewMatrix(Camera camera) {
        Mat4 viewMatrix = new Mat4();
        viewMatrix.identity();
        viewMatrix.rotate((float) java.lang.Math.toRadians(camera.getPitch()), Vec3.xAxis);
        viewMatrix.rotate((float) java.lang.Math.toRadians(camera.getYaw()), Vec3.yAxis);
        viewMatrix.rotate((float) java.lang.Math.toRadians(camera.getRoll()), Vec3.zAxis);

        Vec3 cameraPos = camera.getPosition();
        Vec3 negativeCameraPos = new Vec3(-cameraPos.x, -cameraPos.y, -cameraPos.z);

        // move the world in field of view in the opposite direction to make the camera appear to move
        viewMatrix.translate(negativeCameraPos);

        return viewMatrix;
    }

    /**
     * no one uses this constructor
     */
    private Math() {}

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

    public static float sin(float angle)
    {
        return (float) java.lang.Math.sin(angle);
    }

    public static float cosFromSin(float sin, float angle)
    {
        int quadrant = sin >= 0 ? (angle >= 0 ? 1 : 4) : (angle >= 0 ? 2 : 3);
        float cosSquared = 1 - sin * sin;
        float cos = (float) (java.lang.Math.sqrt(cosSquared) * (quadrant == 1 || quadrant == 4 ? 1 : -1));
        return cos;
    }
}
