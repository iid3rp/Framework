package framework.environment;

import framework.hardware.Display;
import framework.lang.Vec2;

public class Scene
{
    public static Vec2 offset;

    public Scene()
    {

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

}
