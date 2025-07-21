package framework.swing;

import framework.hardware.Display;
import framework.post_processing.FrameBufferObject;

public class Label extends GUITexture implements SwingInterface
{
    private FrameBufferObject display;
    private int texture;
    private String text;

    public Label()
    {
        super();
        display = new FrameBufferObject(Display.getWidth(), Display.getHeight());
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public void render()
    {

    }
}
