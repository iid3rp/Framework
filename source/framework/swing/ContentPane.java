package framework.swing;

import framework.swing.GUIRenderer;
import framework.swing.GUITexture;

import java.util.ArrayList;
import java.util.List;

public class ContentPane extends GUIRenderer
{
    List<GUITexture> containers;
    public ContentPane()
    {
        super();
        containers = new ArrayList<>();
    }

    public void add(GUITexture texture)
    {
        containers.add(texture);
    }

    public List<GUITexture> getComponents()
    {
        return containers;
    }
}
