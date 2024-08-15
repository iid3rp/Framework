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
    private int cursorX;
    private int cursorY;
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
                cursorY += font.getLineHeight() - 16;
                chars = new ArrayList<>();
            }
            else
            {
                chars.add(font.getCharacterMap().get(c));
            }
        }
        setWord(font, chars, text);
    }

    private void setWord(Font font, List<Char> chars, Text text)
    {
        int width = 0;
        for(Char c : chars)
            width += c.getWidth();
        if (cursorX + width > text.getMaxWidth()) {
            cursorY += font.getLineHeight() - 16;
            cursorX = 0;
        }
        for(Char c : chars) {
            renderCharacter(c, font, text);
            cursorX += c.getXAdvance() - 16;
        }
        cursorX += font.getCharacterMap().get(' ').getXAdvance() - 16; // space character
    }

//    private void renderText(Font font, Text text)
//    {
//        cursorX = 0;
//        cursorY = 0;
//        width = 0;
//
//        // rendering
//        List<Char> chars = new ArrayList<>();
//        for(char c : text.getText().toCharArray())
//        {
//            if(c == '\n')
//            {
//                renderLine(font, chars, text);
//                cursorY += font.getLineHeight() - 16;
//                chars.clear();
//            }
//            else
//                chars.add(font.getCharacterMap().get(c));
//        }
//        renderLine(font, chars, text);
//    }

//    private void renderLine(Font font, List<Char> chars, Text text)
//    {
//        List<Char> wrap = new ArrayList<>();
//        List<Char> forWord = new ArrayList<>();
//        Word word = null;
//        for(Char c : chars)
//        {
//            if(c.getCharacter() == ' ')
//            {
//                wrap.add(c);
//                forWord.add(c);
//                word = new Word(forWord);
//                System.out.println(word);
//                width += word.getWidth();
//                forWord = new ArrayList<>();
//            }
//
//            if(width > text.getMaxWidth())
//            {
//                cursorY += font.getLineHeight() - 16;
//                assert word != null;
//                width -= word.getWidth() + font.getCharacterMap().get(' ').getXAdvance() + 16;
//                renderWords(font, wrap, text);
//                wrap.clear();
//                width = 0;
//            }
//            else
//            {
//                wrap.add(c);
//                forWord.add(c);
//            }
//        }
//        renderWords(font, wrap, text);
//        wrap.clear();
//        width = 0;
//    }

//    private void renderWords(Font font, List<Char> chars, Text text)
//    {
//        List<Char> wrap = new ArrayList<>();
//        for(Char c : chars)
//        {
//            if(c.getCharacter() == ' ')
//            {
//                renderWord(font, wrap, text);
//            }
//            else wrap.add(c);
//        }
//        renderWord(font, wrap, text);
//    }
//
//    private void renderWord(Font font, List<Char> chars, Text text)
//    {
//        for(Char c : chars) {
//            renderCharacter(c, font, text);
//            cursorX += c.getXAdvance() - 16;
//        }
//        cursorX += font.getCharacterMap().get(' ').getXAdvance() - 16; // space character
//    }

    private void renderCharacter(Char c, Font font, Text text)
    {
        Vector2f size = getNormal(c.getWidth(), c.getHeight());
        Vector2f pos = setLocation(text,cursorX + c.getXOffset() + 5, cursorY + c.getYOffset() + 5, size);

        // set the transformation matrix of the whole font texture
        Matrix4f letterMatrix = GeomMath.createTransformationMatrix(
                (pos),
                new Vector3f(0),
                (size)
        );

        shader.loadSize(size);
        shader.loadScale(getNormal(font.getScaleW(), font.getScaleH()));
        shader.loadTransformation(letterMatrix);
        shader.loadFontLocation(getNormal(-c.getX(), -c.getY()));

        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCount());
    }

    public Vector2f setLocation(Text text, int x, int y, Vector2f size)
    {
        x = switch(text.getAlignment())
        {
            case Text.LEFT -> x;
            //case Text.CENTER -> (x + text.getMaxWidth() - width) / 2;
            //case Text.RIGHT -> x + text.getMaxWidth() - width;
            default -> throw new IllegalStateException("Unexpected value: " + text.getAlignment());
        };

        //System.out.println(text.getMaxWidth() + " " + width);

        float posX = (((float) x / (Display.getWidth())) * 2) - 1;
        float posY = 1 - (((float) y / (Display.getHeight())) * 2);

        posX += size.x;
        posY -= size.y;

        return new Vector2f(posX, posY);
    }

    public Vector2f getNormal(int x, int y)
    {
        float _x = (float) x / Display.getWidth();
        float _y = (float) y / Display.getHeight();
        return new Vector2f(_x, _y);
    }
}
