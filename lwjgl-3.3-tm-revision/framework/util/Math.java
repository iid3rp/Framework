package framework.util;

import framework.entity.Camera;
import framework.lang.Matrix4f;
import framework.lang.Vector2f;
import framework.lang.Vector3f;
import framework.lang.Vector4f;

public class Math
{
    private static Matrix4f m = new Matrix4f();
    private static Vector2f v2 = new Vector2f();
    private static Vector3f v3 = new Vector3f();
    private static Vector4f v4 = new Vector4f();

    public static Matrix4f createTransformationMatrix(Vector2f position, Vector3f rotation, Vector2f scale)
    {
        m.identity();
        Matrix4f.translate(position, m, m);
        Matrix4f.rotate((float) java.lang.Math.toRadians(rotation.x), new Vector3f(1, 0, 0), m, m);
        Matrix4f.rotate((float) java.lang.Math.toRadians(rotation.y), new Vector3f(0, 1, 0), m, m);
        Matrix4f.rotate((float) java.lang.Math.toRadians(rotation.z), new Vector3f(0, 0, 1), m, m);
        Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), m, m);
        return m;
    }

    public static Matrix4f createTransformationMatrix(Vector3f vector3f, float rotationX, float rotationY, float rotationZ, float scale)
    {
        m.identity();
        Matrix4f.translate(vector3f, m, m);
        Matrix4f.rotate((float) java.lang.Math.toRadians(rotationX), new Vector3f(1, 0, 0), m, m);
        Matrix4f.rotate((float) java.lang.Math.toRadians(rotationY), new Vector3f(0, 1, 0), m, m);
        Matrix4f.rotate((float) java.lang.Math.toRadians(rotationZ), new Vector3f(0, 0, 1), m, m);
        Matrix4f.scale(new Vector3f(scale, scale, scale), m, m);
        return m;
    }

    public static Matrix4f createViewMatrix(Camera camera, float staticPositionY)
    {
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.setIdentity();
        Matrix4f.rotate((float) java.lang.Math.toRadians(camera.getPitch()), Vector3f.xAxis, viewMatrix, viewMatrix);
        Matrix4f.rotate((float) java.lang.Math.toRadians(camera.getYaw()), Vector3f.yAxis, viewMatrix, viewMatrix);
        Matrix4f.rotate((float) java.lang.Math.toRadians(camera.getRoll()), new Vector3f(0, 0, 1), viewMatrix, viewMatrix);
        Vector3f positiveCameraPosition = new Vector3f(camera.getPosition().x, staticPositionY, camera.getPosition().z);
        Vector3f negativeCameraPosition = new Vector3f(-camera.getPosition().x, -staticPositionY, -camera.getPosition().z);
        Matrix4f.translate(negativeCameraPosition, viewMatrix, viewMatrix);
        return viewMatrix;
    }

    public static Matrix4f createViewMatrix(Camera camera) {
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.identity();
        viewMatrix.rotate((float) java.lang.Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0));
        viewMatrix.rotate((float) java.lang.Math.toRadians(camera.getYaw()), new Vector3f(0,1, 0));
        viewMatrix.rotate((float) java.lang.Math.toRadians(camera.getRoll()), new Vector3f(0,0, 1));

        Vector3f cameraPos = camera.getPosition();
        Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);

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
    public static float barryCentric(Vector3f vec3_1, Vector3f vec3_2, Vector3f vec3_3, Vector2f pos)
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
    public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale)
    {
        Matrix4f m = new Matrix4f();
        m.identity();
        m.scale(new Vector3f(scale.x, scale.y, 1f));
        m.translate(new Vector3f(translation.x, translation.y, 1), m);
        return m;
    }

    public static int abs(int n)
    {
        return (~n ^ 1) + 1;
    }

}
