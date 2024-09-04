package framework.font;

public class Char
{
    private int id;
    private char c;
    private int x;
    private int y;
    private int width;
    private int height;
    private int xAdvance;
    private int yOffset;
    private int xOffset;
    public Char() {}

    public Char(char c)
    {
        this.c = c;
    }

    public char getCharacter()
    {
        return c;
    }

    public int getId()
    {
        return id;
    }

    public Char setId(int id)
    {
        this.id = id;
        c = (char) id;
        return this;
    }

    public int getX()
    {
        return x;
    }

    public Char setX(int x)
    {
        this.x = x;
        return this;
    }

    public int getY()
    {
        return y;
    }

    public Char setY(int y)
    {
        this.y = y;
        return this;
    }

    public int getWidth()
    {
        return width;
    }

    public Char setWidth(int width)
    {
        this.width = width;
        return this;
    }

    public int getHeight()
    {
        return height;
    }

    public Char setHeight(int height)
    {
        this.height = height;
        return this;
    }

    public int getXAdvance()
    {
        return xAdvance;
    }

    public Char setXAdvance(int xAdvance)
    {
        this.xAdvance = xAdvance;
        return this;
    }

    public int getYOffset()
    {
        return yOffset;
    }

    public Char setYOffset(int yOffset)
    {
        this.yOffset = yOffset;
        return this;
    }

    public int getXOffset()
    {
        return xOffset;
    }

    public Char setXOffset(int xOffset)
    {
        this.xOffset = xOffset;
        return this;
    }

    public String toString()
    {
        return "Character " + c + " -> " +
                "\tid: " + getId() +
                "\tx = " + getX() +
                "\ty = " + getY() +
                "\twidth: " + getWidth() +
                "\theight: " + getHeight() +
                "\txOffset: " + getXOffset() +
                "\tyOffset: " + getYOffset() +
                "\txAdvance: " + getXAdvance();
    }
}
