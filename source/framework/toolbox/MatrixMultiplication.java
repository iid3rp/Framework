package framework.toolbox;

import framework.entity.Camera;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class MatrixMultiplication
{
    private MatrixMultiplication() {}

    // this is where we basically transform a certain geom
    public static Matrix4f transformMatrix(Vector3f vector3f,
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
