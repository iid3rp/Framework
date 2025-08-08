package framework.environment;

import framework.entity.Camera;
import framework.entity.Entity;
import framework.entity.Light;
import framework.hardware.Display;
import framework.lang.Vec2;

import java.util.ArrayList;
import java.util.List;

public class Scene
{
    public static Vec2 offset;
    public static List<Entity> entities;
    public static List<Light> lights;
    public static Camera camera;

    public Scene()
    {
        entities = new ArrayList<>();
        lights = new ArrayList<>();
        offset = new Vec2();
    }

    public static Vec2 getOffset()
    {
        return offset;
    }

    public static void setOffset(int x, int y)
    {
        float _x = (float) x / Display.getWidth();
        float _y = (float) y / Display.getHeight();
        offset = new Vec2(_x, _y);
    }

    @Override
    public String toString()
    {
        return "";
    }

    public void add(Entity ent)
    {
        entities.add(ent);
    }

    public void add(Light light)
    {
        lights.add(light);
    }

    public void moveCamera(float deltaTime)
    {
        camera.move(deltaTime);
    }

    public void setCamera(Camera camera)
    {
        this.camera = camera;
    }
}
