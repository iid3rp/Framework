package framework.group;

import framework.lang.Vec3;

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
 * @see Vec3
 * @since 1.0
 */
public class Point3f extends Vec3
{
    public Vec3 face;

    public Point3f()
    {
        super();
    }

    public Vec3 getFace()
    {
        return face;
    }

    public void setFace(Vec3 face)
    {
        this.face = face;
    }

    public void transformFace(float x, float y, float z)
    {
        transformFace(new Vec3(x, y, z));
    }

    private void transformFace(Vec3 vec3)
    {
        super.x += vec3.x;
        super.y += vec3.y;
        super.z += vec3.z;
    }

    public void rotateFace(float x, float y, float z)
    {
        rotateFace(new Vec3(x, y, z));
    }

    private void rotateFace(Vec3 vec3)
    {
        face.x = (face.x + vec3.x) % 360;
        face.y = (face.y + vec3.y) % 360;
        face.z = (face.z + vec3.z) % 360;
    }
}
