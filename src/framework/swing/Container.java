package framework.swing;

import framework.lang.Vec2;

import java.awt.Color;
import java.util.List;

public class Container extends GUITexture
{
    private int parentX;
    private int parentY;
    private int parentWidth;
    private int parentHeight;
    private Vec2 parentPosition;
    private Vec2 parentSize;
    private Color highlightColor;

    public Container()
    {
        super();
    }

    public List<Container> getChildren()
    {
        return super.getChildren();
    }

    public int getParentX()
    {
        return parentX;
    }

    public int getParentY()
    {
        return parentY;
    }

    public int getParentWidth()
    {
        return parentWidth;
    }

    public int getParentHeight()
    {
        return parentHeight;
    }

    public Vec2 getParentPosition()
    {
        return parentPosition;
    }

    public Vec2 getParentSize()
    {
        return parentSize;
    }

    public Color getHighlightColor()
    {
        return highlightColor;
    }
}
