package framework.group;

import framework.entity.Entity;
import framework.lang.Vec3;

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
        transformPosition(new Vec3(x, y, z));
    }

    public void transformPosition(Vec3 vec3)
    {
        for(Entity entity : entities)
        {
            entity.transformPosition(vec3);
        }
    }

    public void transformRotation(Point3f reference, float x, float y, float z)
    {
        for(Entity entity : entities)
        {
            // Calculate the offset from the reference point to the entity's position
            Vec3 offset = Vec3.sub(entity.getPosition(), reference, null);

            // Apply the rotation transformation
            offset.rotateX(x);
            offset.rotateY(y);
            offset.rotateZ(z);

            // Update the entity's position based on the rotated position relative to the reference point
            entity.setPosition(Vec3.add(reference, offset));

            // Update the entity's rotation
            entity.transformRotation(x, y, z);
        }
    }
}
