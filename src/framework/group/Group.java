package framework.group;

import framework.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class Group
{
    public List<Entity> entities;
    public List<Point3f> reference;
    public Group()
    {
        entities = new ArrayList<>();
    }

    public void add(Entity entity)
    {
        entities.add(entity);
    }

    public void remove(Entity entity)
    {
        entities.remove(entity);
    }

    public void transformPosition(float x, float y, float z)
    {
        transformPosition(new Vector3f(x, y, z));
    }

    public void transformPosition(Vector3f vector3f)
    {
        for(Entity entity : entities)
        {
            entity.transformPosition(vector3f);
        }
    }

    public void transformRotation(Point3f reference, float x, float y, float z)
    {
        for(Entity entity : entities)
        {
            // Calculate the offset from the reference point to the entity's position
            Vector3f offset = new Vector3f(entity.getPosition()).sub(reference);

            // Apply the rotation transformation
            offset.rotateX(x);
            offset.rotateY(y);
            offset.rotateZ(z);

            // Update the entity's position based on the rotated position relative to the reference point
            entity.setPosition(reference.add(offset));

            // Update the entity's rotation
            entity.transformRotation(x, y, z);
        }
    }
}
