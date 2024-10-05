package framework.group;

import framework.lang.Vector3f;

/**
 * The {@code Point3f} class represents a point in 3D space, extending the {@code Vector3f} class.
 * It is used for grouping entities and serves as a reference point for creating rotations with them as center points.
 * This class can be used to bridge two groups based on the reference point.
 *
 * <p>
 * The {@code Point3f} class includes methods to get and set the face vector, transform the face vector by adding a specified vector,
 * and rotate the face vector by specified angles.
 * </p>
 *
 * <pre>
 * Example usage:
 * {@code
 * Point3f point = new Point3f();
 * point.setFace(new Vector3f(1.0f, 0.0f, 0.0f));
 * point.transformFace(1.0f, 2.0f, 3.0f);
 * point.rotateFace(90.0f, 0.0f, 0.0f);
 * }
 * </pre>
 *
 * @see Vector3f
 * @since 1.0
 */
public class Point3f extends Vector3f
{
    public Vector3f face;

    public Point3f()
    {
        super();
    }

    public Vector3f getFace()
    {
        return face;
    }

    public void setFace(Vector3f face)
    {
        this.face = face;
    }

    public void transformFace(float x, float y, float z)
    {
        transformFace(new Vector3f(x, y, z));
    }

    private void transformFace(Vector3f vector3f)
    {
        super.x += vector3f.x;
        super.y += vector3f.y;
        super.z += vector3f.z;
    }

    public void rotateFace(float x, float y, float z)
    {
        rotateFace(new Vector3f(x, y, z));
    }

    private void rotateFace(Vector3f vector3f)
    {
        face.x = (face.x + vector3f.x) % 360;
        face.y = (face.y + vector3f.y) % 360;
        face.z = (face.z + vector3f.z) % 360;
    }
}
