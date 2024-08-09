package framework.swing;

import framework.font.GUIText;
import framework.font.TextMasterRenderer;

public interface SwingInterface
{
    void add(Container c);

    void remove(Container c);
    default void add(GUIText text)
    {
        TextMasterRenderer.loadText(text);
    }

    default void remove(GUIText text)
    {
        TextMasterRenderer.remove(text);
    }
}
