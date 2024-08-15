package framework.fontExperiment;

import java.util.List;

public class Word
{
    public List<Char> chars;
    public int width;
    public Word(List<Char> chars)
    {
        this.chars = chars;
        for(Char c : chars)
            width += c.getXAdvance() - 16;
    }

    public int getWidth()
    {
        return width;
    }

    @Override
    public String toString()
    {
        StringBuilder s = new StringBuilder();
        for(Char c : chars)
        {
            s.append(c.getCharacter());
        }
        return s.toString();
    }
}
