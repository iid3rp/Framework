package framework.swing;

import framework.swing.GUITexture;

import java.util.List;

public class Panel extends Component
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
