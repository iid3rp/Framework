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

        renderText(font, text);

        GL11.glEnable((GL11.GL_DEPTH_TEST));
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.unbind();
    }

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

    private void renderCharacter(Char c, Font font, Text text)
    {
        Vector2f size = getNormal(c.getWidth() * fontSize, c.getHeight() * fontSize);
        Vector2f pos = setLocation(text,
                cursorX + ((c.getXOffset() + 5) * fontSize),
                cursorY + ((c.getYOffset() + 5) * fontSize), size);

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

    public Vector2f setLocation(Text text, float x, float y, Vector2f size)
    {
        x = switch(text.getAlignment())
        {
            case Text.LEFT -> x;
            //case Text.CENTER -> (x + text.getMaxWidth() - width) / 2;
            //case Text.RIGHT -> x + text.getMaxWidth() - width;
            default -> throw new IllegalStateException("Unexpected value: " + text.getAlignment());
        };

        //System.out.println(text.getMaxWidth() + " " + width);

        float posX = ((x / (Display.getWidth())) * 2) - 1;
        float posY = 1 - ((y / (Display.getHeight())) * 2);

        posX += size.x;
        posY -= size.y;

        return new Vector2f(posX, posY);
    }

    public Vector2f getNormal(float x, float y)
    {
        float _x = x / Display.getWidth();
        float _y = y / Display.getHeight();
        return new Vector2f(_x, _y);
    }
}
