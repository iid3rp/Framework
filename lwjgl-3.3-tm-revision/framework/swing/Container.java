package framework.swing;

import java.awt.Color;
import java.util.List;

public class Container extends GUITexture
{
    private int parentX;
    private int parentY;
    private int parentWidth;
    private int parentHeight;
    private Vector2f parentPosition;
    private Vector2f parentSize;
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

    public Vector2f getParentPosition()
    {
        return parentPosition;
    }

    public Vector2f getParentSize()
    {
        return parentSize;
    }

    public Color getHighlightColor()
    {
        return highlightColor;
    }
}
