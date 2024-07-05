package toolbox;

import entity.Camera;
import environment.Scene;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class GeomMath
{
    /**
     * no one uses this constructor
     */
    private GeomMath() {}

    /**
     * Calculates the interpolation of a point within a triangle using barycentric coordinates.
     * This method takes three 3D points p1, p2, and p3 representing the vertices of a triangle,
     * and a 2D point pos within the triangle. It then computes the barycentric coordinates l1, l2, and l3
     * which determine the weight of each vertex in the interpolation.
     * The barycentric coordinates are calculated based on the determinant of the triangle formed by the three points.
     * The interpolation is then computed using the barycentric coordinates to blend the values of p1.y, p2.y, and p3.y.
     * @param p1 The first vertex of the triangle.
     * @param p2 The second vertex of the triangle.
     * @param p3 The third vertex of the triangle.
     * @param pos The 2D point within the triangle for interpolation.
     * @return The interpolated value at the given 2D point within the triangle.
     */
    public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos)
    {
        float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
        float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
        float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
        float l3 = 1.0f - l1 - l2;
        return l1 * p1.y + l2 * p2.y + l3 * p3.y;
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
        matrix.setIdentity();
        Matrix4f.translate(translation, matrix, matrix);
        Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
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
        matrix.setIdentity();
        Matrix4f.translate(position, matrix, matrix);
        Matrix4f.rotate((float) java.lang.Math.toRadians(rotation.getX()), new Vector3f(1, 0, 0), matrix, matrix);
        Matrix4f.rotate((float) java.lang.Math.toRadians(rotation.getY()), new Vector3f(0, 1, 0), matrix, matrix);
        Matrix4f.rotate((float) java.lang.Math.toRadians(rotation.getZ()), new Vector3f(0, 0, 1), matrix, matrix);
        Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
        return matrix;
    }


    /**
     * Creates a transformation matrix based on the provided 3D vector, rotations, and scale.
     *
     * @param vector3f The 3D translation vector.
     * @param rotationX The rotation around the X-axis.
     * @param rotationY The rotation around the Y-axis.
     * @param rotationZ The rotation around the Z-axis.
     * @param scale The scale factor.
     * @return The transformation matrix.
     */
    public static Matrix4f createTransformationMatrix(Vector3f vector3f,
                                                      float rotationX,
                                                      float rotationY,
                                                      float rotationZ,
                                                      float scale)
    {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
        Matrix4f.translate(vector3f, matrix, matrix);
        Matrix4f.rotate((float) java.lang.Math.toRadians(rotationX), new Vector3f(1, 0, 0), matrix, matrix);
        Matrix4f.rotate((float) java.lang.Math.toRadians(rotationY), new Vector3f(0, 1, 0), matrix, matrix);
        Matrix4f.rotate((float) java.lang.Math.toRadians(rotationZ), new Vector3f(0, 0, 1), matrix, matrix);
        Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
        return matrix;
    }

    // this is where we mve the objects where the camera moves...

    /**
     *
     * @param camera the camera of the certain {@link Scene}
     * @return the view matrix
     */
    public static Matrix4f createViewMatrix(Camera camera)
    {
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.setIdentity();
        Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0 , 0), viewMatrix, viewMatrix);
        Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
        Matrix4f.rotate((float) Math.toRadians(camera.getRoll()), new Vector3f(0, 0, 1), viewMatrix, viewMatrix);
        Vector3f positiveCameraPosition = camera.getPositivePosition();
        Vector3f negativeCameraPosition = camera.getNegativePosition();
        Matrix4f.translate(negativeCameraPosition, viewMatrix, viewMatrix);
        return viewMatrix;
    }
}
