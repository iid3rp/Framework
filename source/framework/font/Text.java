package framework.font;

import framework.model.Model;
import org.joml.Vector2f;

import java.awt.Color;
import java.awt.Point;

public class Text
{
    public static final int LEFT = 0;
    public static final int CENTER = 1;
    public static final int RIGHT = 2;
    private String text;
    private Point position;
    private Color foregroundColor;
    private Color outlineColor;
    private float borderWidth;
    private float borderEdge;
    private Vector2f offset;
    private float edge;
    private float fontSize;
    private int height;
    private int width;
    private int maxWidth;
    private int alignment;
    private float smoothWidth;


    public Text()
    {
        set();
    }

    public Text(String text)
    {
        set();
        this.text = text;
    }

    private void set()
    {
        text = "";
        position = new Point();
        foregroundColor = Color.BLACK;
        outlineColor = Color.WHITE;
        borderWidth = 0;
        borderEdge = 0;
        offset = new Vector2f();
        edge = 0.02f;
        fontSize = 15;
        smoothWidth = 0.5f;
        width = 0;
        maxWidth = 0;
        alignment = LEFT;
    }

    public float getSmoothWidth()
    {
        return smoothWidth;
    }

    public void setSmoothWidth(float smoothWidth)
    {
        this.smoothWidth = smoothWidth;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public void setColor(Color color)
    {
        this.foregroundColor = color;
    }

    public void setFontSize(float pixels)
    {
        this.fontSize = pixels;
    }

    public Model getQuad()
    {
        return null;
    }

    public String getText()
    {
        return text;
    }

    public int getMaxWidth()
    {
        return maxWidth;
    }

    public void setAlignment(int alignment)
    {
        this.alignment = alignment;
    }

    public void setSize(int width, int height)
    {
        this.width = width;
        this.height = height;
    }

    public int getAlignment()
    {
        return alignment;
    }

    public void setMaxWidth(int maxWidth)
    {
        this.maxWidth = maxWidth;
    }

    public float getFontSize()
    {
        return fontSize;
    }

    public Point getPosition()
    {
        return position;
    }

    public void setPosition(Point position)
    {
        this.position = position;
    }

    public Color getForegroundColor()
    {
        return foregroundColor;
    }

    public void setForegroundColor(Color foregroundColor)
    {
        this.foregroundColor = foregroundColor;
    }

    public Color getOutlineColor()
    {
        return outlineColor;
    }

    public void setOutlineColor(Color outlineColor)
    {
        this.outlineColor = outlineColor;
    }

    public float getBorderWidth()
    {
        return borderWidth;
    }

    public void setBorderWidth(float borderWidth)
    {
        this.borderWidth = borderWidth;
    }

    public float getBorderEdge()
    {
        return borderEdge;
    }

    public void setBorderEdge(float borderEdge)
    {
        this.borderEdge = borderEdge;
    }

    public Vector2f getOffset()
    {
        return offset;
    }

    public void setOffset(Vector2f offset)
    {
        this.offset = offset;
    }

    public float getEdge()
    {
        return edge;
    }

    public void setEdge(float edge)
    {
        this.edge = edge;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }
}
