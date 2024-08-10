package framework.fontExperiment;

import framework.model.Model;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.awt.Color;
import java.awt.Point;

public class Text
{
    private String text;
    private Vector2f position2f;
    private Vector4f color4f;
    private Color color;
    private Point position;
    private float fontSize;
    public Text() {}

    public Text(String text)
    {
        this.text = text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public void setColor(Color color)
    {
        this.color = color;
        float red = color.getRed() / 255.0f;
        float green = color.getGreen() / 255.0f;
        float blue = color.getBlue() / 255.0f;
        float alpha = color.getAlpha() / 255.0f;
        this.color4f = new Vector4f(red, green, blue, alpha);
    }

    public Color getColor()
    {
        return color;
    }

    public void setFontSize(float fontSize)
    {
        this.fontSize = fontSize;
    }

    public Model getQuad()
    {
        return null;
    }
}
