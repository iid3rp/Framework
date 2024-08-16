package framework.fontExperiment;

import framework.h.Display;
import framework.loader.ModelLoader;
import framework.model.Model;
import framework.swing.GUIRenderer;
import framework.util.GeomMath;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.List;

public class TextEntityRenderer
{
    private Model quad;
    private static TextShader shader;
    private Vector2f size;
    private float cursorX;
    private float cursorY;
    private float fontSize;

    //private int width;
    public TextEntityRenderer()
    {
        quad = ModelLoader.loadToVao(GUIRenderer.positions, GUIRenderer.coords);
        size = new Vector2f(1, 1);
        shader = new TextShader();
    }

    public void render(Font font, Text text)
    {
        shader.bind();
        GL30.glBindVertexArray(quad.getVaoId());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, font.getTexture());

        renderString(font, text);

        GL11.glEnable((GL11.GL_DEPTH_TEST));
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.unbind();
    }

    public void renderString(Font font, Text text)
    {
        fontSize = (text.getFontSize() / font.getLineHeight());
        cursorX = cursorY = 0;

        String[] lines = text.getText().split("\n");
        for(String s : lines)
        {
            renderLine(font, text, s);
            System.out.println();
        }
    }

    private void renderLine(Font font, Text text, String line)
    {
        float[] width = {0, 0};
        cursorX = 0;
        String[] words = line.split(" ");
        List<String> w = new ArrayList<>();
        List<Char> chars = new ArrayList<>();
        for(String word : words)
        {
            for(char c : word.toCharArray())
            {
                width[0] += (font.getCharacterMap().get(c)
                            .getXAdvance() - 16) * fontSize;
            }
            width[0] += (font.getCharacterMap().get(' ')
                            .getXAdvance() - 16) * fontSize;

            if(width[0] > text.getMaxWidth())
            {
                cursorX = 0;
                renderWord(font, text, chars, width[1]);
                cursorY += (font.getLineHeight() - 16) * fontSize;

                width = new float[] { 0, 0 };
                chars.clear();
                for(char c : word.toCharArray())
                {
                    Char ch = font.getCharacterMap().get(c);
                    width[0] += (ch.getXAdvance() - 16) * fontSize;
                    chars.add(ch);
                }
                Char ch = font.getCharacterMap().get(' ');
                width[0] += (ch.getXAdvance() - 16) * fontSize;
                chars.add(ch);
                width[1] = width[0];
            }
            else
            {
                width[1] = width[0];
                for(char c : word.toCharArray())
                    chars.add(font.getCharacterMap().get(c));
                chars.add(font.getCharacterMap().get(' '));
            }
        }
        cursorX = 0;
        renderWord(font, text, chars, width[1]);
        cursorY += (font.getLineHeight() - 16) * fontSize;

    }

    private void renderWord(Font font, Text text, List<Char> chars, float width)
    {
        for(Char c : chars)
        {
            renderChar(c, font, text, width);
            cursorX += (c.getXAdvance() - 16) * fontSize;
        }
    }

    private void renderChar(Char c, Font font, Text text, float width)
    {
        Vector2f size = getNormal(c.getWidth() * fontSize, c.getHeight() * fontSize);
        Vector2f pos = sl(text,
                cursorX + ((c.getXOffset() + 5) * fontSize),
                cursorY + ((c.getYOffset() + 5) * fontSize), size, width);

        System.out.println(c.getCharacter() + " >> cursor X = " + cursorX + " >> cursor Y = " + cursorY);

        // set the transformation matrix of the whole font texture
        Matrix4f letterMatrix = GeomMath.createTransformationMatrix(
                (pos),
                new Vector3f(0),
                (size)
        );

        shader.loadTransformation(letterMatrix);
        shader.loadSize(size);
        shader.loadFontLocation(getNormal(-c.getX() * fontSize, -c.getY() * fontSize));
        shader.loadScale(getNormal(font.getScaleW() * fontSize, font.getScaleH() * fontSize));

        shader.loadWidth(text.getSmoothWidth());
        shader.loadEdge(text.getEdge());
        shader.loadForegroundColor(text.getForegroundColor());
        shader.loadOutlineColor(text.getOutlineColor());
        shader.loadBorderWidth(text.getBorderWidth());
        shader.loadBorderEdge(text.getBorderEdge());
        shader.loadOffset(text.getOffset());

        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCount());
    }

    private Vector2f sl(Text text, float x, float y, Vector2f size, float width)
    {
        if(text.getAlignment() == Text.RIGHT)
        {
            x = x + text.getMaxWidth() - width;
        }
        else if(text.getAlignment() == Text.CENTER)
        {
            x = x + ((text.getMaxWidth() - width) / 2);
        }

        float posX = ((x / (Display.getWidth())) * 2) - 1;
        float posY = 1 - ((y / (Display.getHeight())) * 2);

        posX += size.x;
        posY -= size.y;

        return new Vector2f(posX, posY);
    }

    @Deprecated
    private void renderText(Font font, Text text)
    {
        fontSize = (text.getFontSize() / font.getLineHeight());
        cursorX = 0;
        cursorY = 0;

        // rendering
        List<Char> chars = new ArrayList<>();
        for(char c : text.getText().toCharArray()) {
            if(c == ' ')
            {
                setWord(font, chars, text);
                chars = new ArrayList<>();
            }
            else if(c == '\n')
            {
                setWord(font, chars, text);
                cursorY += (font.getLineHeight() - 16) * fontSize;
                chars = new ArrayList<>();
            }
            else chars.add(font.getCharacterMap().get(c));
        }
        setWord(font, chars, text);
    }

    @Deprecated
    private void setWord(Font font, List<Char> chars, Text text)
    {
        float width = 0;
        for(Char c : chars)
            width += (c.getWidth()) * fontSize;
        if (cursorX + width > text.getMaxWidth()) {
            cursorY += (font.getLineHeight() - 16) * fontSize;
            cursorX = 0;
        }
        for(Char c : chars) {
            renderCharacter(c, font, text);
            cursorX += (c.getXAdvance() - 16) * fontSize;
        }
        cursorX += (font.getCharacterMap().get(' ').getXAdvance() - 16) * fontSize; // space character
    }

    @Deprecated
    private void renderCharacter(Char c, Font font, Text text)
    {
        Vector2f size = getNormal(c.getWidth() * fontSize, c.getHeight() * fontSize);
        Vector2f pos = getNormal(
                cursorX + ((c.getXOffset() + 5) * fontSize),
                cursorY + ((c.getYOffset() + 5) * fontSize));

        // set the transformation matrix of the whole font texture
        Matrix4f letterMatrix = GeomMath.createTransformationMatrix(
                (pos),
                new Vector3f(0),
                (size)
        );

        shader.loadSize(size);
        shader.loadScale(getNormal(font.getScaleW() * fontSize, font.getScaleH() * fontSize));
        shader.loadTransformation(letterMatrix);
        shader.loadFontLocation(getNormal(-c.getX() * fontSize, -c.getY() * fontSize));

        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCount());
    }

    public Vector2f getNormal(float x, float y)
    {
        float _x = x / Display.getWidth();
        float _y = y / Display.getHeight();
        return new Vector2f(_x, _y);
    }
}
