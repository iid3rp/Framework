package framework.swing;

import java.util.List;

public class Panel extends Container implements SwingInterface
{
    private List<GUITexture> components;
    public Panel()
    {

    }

    public List<GUITexture> getComponents()
    {
        return components;
    }
}
