package framework.swing;

import framework.font.GUIText;
import framework.font.TextMasterRenderer;

import java.util.ArrayList;
import java.util.List;

public class ContentPane extends GUIRenderer implements SwingInterface
{
    List<Container> containers;
    public ContentPane()
    {
        super();
        containers = new ArrayList<>();
    }

    @Override
    public void add(Container texture)
    {
        containers.add(texture);
    }

    @Override
    public void remove(Container c)
    {
        containers.remove(c);
    }

    public List<Container> getComponents()
    {
        return containers;
    }
}
