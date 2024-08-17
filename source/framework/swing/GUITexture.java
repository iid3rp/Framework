package framework.swing;

import framework.hardware.Display;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class GUITexture implements SwingInterface
{
    private Vector2f size;
    private int diffuse;
    private int clipTexture;
    private Vector2f position;
    private Vector2f rawPosition;
    private Vector2f scale;
    private Vector3f rotation;
    private int width;
    private int height;
    private int x;
    private int y;
    private int imgX;
    private int imgY;
    private int zLayer;
    private int desiredZLayer;
    private List<Container> children;
    private Vector2f imageLocation;

    public GUITexture()
    {
        diffuse = 0;
        position = new Vector2f(0, 0);
        scale = new Vector2f(0, 0);
        rotation = new Vector3f(0, 0, 0);
        size = new Vector2f(0, 0);
        rawPosition = new Vector2f(0, 0);
        children = new ArrayList<>();
    }

    public GUITexture(int texture, Vector2f position, Vector2f scale, Vector2f size)
    {
        this.diffuse = texture;
        this.position = position;
        this.size = size;
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

    public void transformRotation(Vector3f rotation)
    {
        this.rotation.x += rotation.x;
        this.rotation.y += rotation.y;
        this.rotation.z += rotation.z;
    }

    public void setTexture(int texture)
    {
        this.diffuse = texture;
    }


    public void setZLayer(int z)
    {
        desiredZLayer = z;
    }

    public void setScale(int width, int height)
    {
        float w = (float) width / Display.getWidth();
        float h = (float) height / Display.getHeight();
        this.scale = new Vector2f(w, h);
        setLocation(this.x, this.y);
        //setImageLocation(this.imgX, this.imgY);
    }

    public void setImageLocation(int x, int y)
    {
        imgX = x;
        imgY = y;
        float w = (float) x / Display.getWidth();
        float h = (float) y / Display.getHeight();

        this.imageLocation = new Vector2f(w, h);
    }

    public Vector2f getImageLocation()
    {
        return imageLocation;
    }

    public void setSize(int width, int height)
    {
        float w = (float) width / Display.getWidth();
        float h = (float) height / Display.getHeight();
        this.size = new Vector2f(w, h);
        setLocation(this.x, this.y);
    }

    public void setLocation(int x, int y)
    {
        this.x = x;
        this.y = y;
        float posX = (((float) x / (Display.getWidth())) * 2) - 1;
        float posY = 1 - (((float) y / (Display.getHeight())) * 2);

        posX += size.x;
        posY -= size.y;

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
        return diffuse;
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
    }

    public Vector2f getRawPosition()
    {
        return rawPosition;
    }

    public void mirrorZ()
    {
        rotation.z += 180;
    }

    public Vector2f getSize()
    {
        return size;
    }

    public int getClipTexture()
    {
        return clipTexture;
    }

    public void setClipTexture(int clipTexture)
    {
        this.clipTexture = clipTexture;
    }

    @Override
    public void add(Container c)
    {
        children.add(c);
    }

    @Override
    public void remove(Container c)
    {
        children.remove(c);
    }

    protected List<Container> getChildren()
    {
        return children;
    }
}
