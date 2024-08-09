package framework.font;

import framework.loader.ModelLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextMasterRenderer
{
    private static Map<FontType, List<GUIText>> texts = new HashMap<>();
    private static FontRenderer fontRenderer;

    public static void initialize()
    {
        fontRenderer = new FontRenderer();
    }

    public static void render()
    {
        fontRenderer.render(texts);
    }

    public static boolean loadText(GUIText text)
    {
        if(texts.get(text.getFont()).contains(text))
            return false;

        FontType fontType = text.getFont();
        TextMeshData data = fontType.loadText(text);
        int vao = ModelLoader.loadToVao(data.getVertexPositions(), data.getTextureCoords());
        text.setMeshInfo(vao, data.getVertexCount());
        List<GUIText> textSection = texts.computeIfAbsent(fontType, func -> new ArrayList<>());
        textSection.add(text);

        return true;
    }

    public static void remove(GUIText text)
    {
        List<GUIText> textBatch = texts.get(text.getFont());
        textBatch.remove(text);
        if(textBatch.isEmpty())
        {
            texts.remove(text.getFont());
        }
    }

    public static void setText(GUIText gui, String text)
    {
        remove(gui);
        GUIText newText = gui;
        gui.setText(text);
        loadText(newText);
    }

    public static void dispose()
    {
        fontRenderer.dispose();
    }
}
