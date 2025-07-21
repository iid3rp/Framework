package framework.font;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Font
{
    private int fontTexture;
    private int size;
    private boolean bold;
    private int stretchH;
    private int aa;
    private int[] padding;
    private int[] spacing;
    private int lineHeight;
    private int base;
    private int scaleW;
    private int scaleH;
    private Map<Character, Char> characters;

    protected Font()
    {
        characters = new HashMap<>();
    }

    public List<Char> getCharacters()
    {
        return new ArrayList<>(characters.values());
    }

    protected Font setSize(int size)
    {
        this.size = size;
        return this;
    }

    protected Font setItalic(boolean bold)
    {
        this.bold = bold;
        return this;
    }

    protected Font setStretchH(int stretchH)
    {
        this.stretchH = stretchH;
        return this;
    }

    protected Font setAA(int aa)
    {
        this.aa = aa;
        return this;
    }

    protected Font setPadding(int[] padding)
    {
        this.padding = padding;
        return this;
    }

    protected Font setSpacing(int[] spacing)
    {
        this.spacing = spacing;
        return this;
    }

    protected Font setBold(boolean b)
    {
        this.bold = b;
        return this;
    }

    protected int getLineHeight()
    {
        return lineHeight;
    }

    protected Font setLineHeight(int lineHeight)
    {
        this.lineHeight = lineHeight;
        return this;
    }

    protected int getBase()
    {
        return base;
    }

    protected Font setBase(int base)
    {
        this.base = base;
        return this;
    }

    protected int getScaleW()
    {
        return scaleW;
    }

    protected Font setScaleW(int scaleW)
    {
        this.scaleW = scaleW;
        return this;
    }

    protected int getScaleH()
    {
        return scaleH;
    }

    protected Font setScaleH(int scaleH)
    {
        this.scaleH = scaleH;
        return this;
    }

    protected int getTexture()
    {
        return fontTexture;
    }

    protected Font setTexture(int texture)
    {
        this.fontTexture = texture;
        return this;
    }

    private Char getCharacter(char c)
    {
        return characters.get(c);
    }

    @Override
    public String toString()
    {
        return "Font{" +
                "size=" + size +
                ", bold=" + bold +
                ", stretchH=" + stretchH +
                ", aa=" + aa +
                ", padding=" + Arrays.toString(padding) +
                ", spacing=" + Arrays.toString(spacing);
    }

    public Map<Character, Char> getCharacterMap()
    {
        return characters;
    }
}
