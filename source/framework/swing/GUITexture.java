package framework.swing;

import framework.DisplayManager;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class GUITexture
{
    private int texture;
    private Vector2f position;
    private Vector2f scale;
    private Vector3f rotation;
    private int width;
    private int height;
    private int x;
    private int y;
    private int zLayer;
    private int desiredZLayer;
    public GUITexture()
    {
        texture = 0;
        position = new Vector2f(0, 0);
        scale = new Vector2f(0, 0);
        rotation = new Vector3f(0, 0, 0);
    }

    public GUITexture(int texture, Vector2f position, Vector2f scale)
    {
        this.texture = texture;
        this.position = position;
        this.scale = scale;
    }

    public Vector3f getRotation()
    {
        return rotation;
    }

    public void setRotation(Vector3f rotation)
    {
        this.rotation = rotation;
    }

    public void setBackgroundImage(int texture)
    {
        this.texture = texture;
    }


    public void setZLayer(int z)
    {
        desiredZLayer = z;
    }

    public void setSize(int width, int height)
    {
        float w = (float) width / DisplayManager.getWindowWidth();
        float h = (float) height / DisplayManager.getWindowHeight();
        this.scale = new Vector2f(w, h);
        setLocation(this.x, this.y);
    }

    public void setLocation(int x, int y)
    {
        this.x = x;
        this.y = y;
        float posX = (((float) x / (DisplayManager.getWindowWidth())) * 2) - 1;
        float posY = 1 - (((float) y / (DisplayManager.getWindowHeight())) * 2);

        posX += scale.x;
        posY -= scale.y;

        this.position = new Vector2f(posX, posY);
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getzLayer()
    {
        return zLayer;
    }

    public int getDesiredZLayer()
    {
        return desiredZLayer;
    }

    public int getTexture()
    {
        return texture;
    }

    public Vector2f getPosition()
    {
        return position;
    }

    public Vector2f getScale()
    {
        return scale;
    }

    public void mirrorX()
    {
        rotation.x += 180;
    }
    public void mirrorY()
    {
        rotation.y += 180;
    }public void mirrorZ()
    {
        rotation.z += 180;
    }
}
