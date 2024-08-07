package framework.util;

import framework.entity.Camera;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class GeomMath
{
    public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.identity();
        matrix.translate(translation.x, translation.y, translation.z);
        matrix.rotate((float) Math.toRadians(rx), new Vector3f(1, 0, 0));
        matrix.rotate((float) Math.toRadians(ry), new Vector3f(0, 1, 0));
        matrix.rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1));
        matrix.scale(new Vector3f(scale, scale, scale));
        return matrix;
    }

    public static Matrix4f createViewMatrix(Camera camera) {
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.identity();
        viewMatrix.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0));
        viewMatrix.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0,1, 0));
        viewMatrix.rotate((float) Math.toRadians(camera.getRoll()), new Vector3f(0,0, 1));

        Vector3f cameraPos = camera.getPosition();
        Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);

        // move the world in field of view in the opposite direction to make the camera appear to move
        viewMatrix.translate(negativeCameraPos);

        return viewMatrix;
    }

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
    public static float barryCentric(Vector3f vec3_1, Vector3f vec3_2, Vector3f vec3_3, Vector2f pos)
    {
        float det = (vec3_2.z - vec3_3.z) * (vec3_1.x - vec3_3.x) + (vec3_3.x - vec3_2.x) * (vec3_1.z - vec3_3.z);
        float l1 = ((vec3_2.z - vec3_3.z) * (pos.x - vec3_3.x) + (vec3_3.x - vec3_2.x) * (pos.y - vec3_3.z)) / det;
        float l2 = ((vec3_3.z - vec3_1.z) * (pos.x - vec3_3.x) + (vec3_1.x - vec3_3.x) * (pos.y - vec3_3.z)) / det;
        float l3 = 1.0f - l1 - l2;
        return l1 * vec3_1.y + l2 * vec3_2.y + l3 * vec3_3.y;
    }

    /**
     * Creates a transformation matrix based on the provided translation and scale.
     *
     * @param translation The 2D translation vector.
     * @param scale The 2D scale vector.
     * @return The transformation matrix.
     */
    public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale)
    {
        Matrix4f matrix = new Matrix4f();
        matrix.identity();
        matrix.translate(new Vector3f(translation.x, translation.y, 1), matrix);
        matrix.scale(scale.x, scale.y, 1f);
        return matrix;
    }

    /**
     * Creates a transformation matrix based on the provided 2D position, 3D rotation,
     * and 2D scale vectors.
     * This method initializes a new 4x4 identity matrix and then applies translation,
     * rotation, and scaling transformations
     * in the specified order to create the final transformation matrix.
     *
     * @param position The 2D vector representing the translation in x and y axes.
     * @param rotation The 3D vector representing rotations around the x, y, and z axes in degrees.
     * @param scale The 2D vector representing the scaling factors in x and y axes.
     * @return The transformation matrix that combines the translation, rotation, and scaling operations.
     */
    public static Matrix4f createTransformationMatrix(Vector2f position, Vector3f rotation, Vector2f scale)
    {

        Matrix4f matrix = new Matrix4f();
        matrix.identity();
        matrix.translate(position.x, position.y, 1f);
        matrix.rotate((float) java.lang.Math.toRadians(rotation.x), new Vector3f(1, 0, 0));
        matrix.rotate((float) java.lang.Math.toRadians(rotation.y), new Vector3f(0, 1, 0));
        matrix.rotate((float) java.lang.Math.toRadians(rotation.z), new Vector3f(0, 0, 1));
        matrix.scale(scale.x, scale.y, 1f);
        return matrix;
    }


    /**
     *
     * @param camera the camera of the certain scene
     * @return the view matrix
     */
    public static Matrix4f createViewMatrix(Camera camera, float staticPositionY)
    {
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.identity();
        viewMatrix.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0 , 0));
        viewMatrix.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 0, 0));
        viewMatrix.rotate((float) Math.toRadians(camera.getRoll()), new Vector3f(0, 0, 1));
        Vector3f positiveCameraPosition = new Vector3f(camera.getPosition().x, staticPositionY, camera.getPosition().z);
        Vector3f negativeCameraPosition = new Vector3f(-camera.getPosition().x, -staticPositionY, -camera.getPosition().z);
        viewMatrix.translate(negativeCameraPosition);
        return viewMatrix;
    }

}
