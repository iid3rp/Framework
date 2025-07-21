package framework.swing;

import framework.hardware.Display;
import framework.lang.Vec2;
import framework.lang.Vec3;

import java.util.ArrayList;
import java.util.List;

public class GUITexture implements SwingInterface
{
    private Vec2 size;
    private int diffuse;
    private int clipTexture;
    private Vec2 position;
    private Vec2 rawPosition;
    private Vec2 scale;
    private Vec3 rotation;
    private int width;
    private int height;
    private int x;
    private int y;
    private int imgX;
    private int imgY;
    private int zLayer;
    private int desiredZLayer;
    private List<Container> children;
    private Vec2 imageLocation;

    public GUITexture()
    {
        diffuse = 0;
        imageLocation = new Vec2();
        position = new Vec2();
        scale = new Vec2();
        rotation = new Vec3();
        size = new Vec2();
        rawPosition = new Vec2();
        children = new ArrayList<>();
    }

    public GUITexture(int texture, Vec2 position, Vec2 scale, Vec2 size)
    {
        this.diffuse = texture;
        this.position = position;
        this.size = size;
        this.scale = scale;
    }

    public Vec3 getRotation()
    {
        return rotation;
    }

    public void setRotation(Vec3 rotation)
    {
        this.rotation = rotation;
    }

    public void transformRotation(Vec3 rotation)
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
        this.scale.set(w, h);
        setLocation(this.x, this.y);
    }

    public void setImageLocation(int x, int y)
    {
        imgX = x;
        imgY = y;
        float w = (float) x / Display.getWidth();
        float h = (float) y / Display.getHeight();

        this.imageLocation.set(w, h);
    }

    public Vec2 getImageLocation()
    {
        return imageLocation;
    }

    public void setSize(int width, int height)
    {
        float w = (float) width / Display.getWidth();
        float h = (float) height / Display.getHeight();
        this.size.set(w, h);
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

        position.set(posX, posY);
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

    public Vec2 getPosition()
    {
        return position;
    }

    public Vec2 getScale()
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

    public Vec2 getRawPosition()
    {
        return rawPosition;
    }

    public void mirrorZ()
    {
        rotation.z += 180;
    }

    public Vec2 getSize()
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
