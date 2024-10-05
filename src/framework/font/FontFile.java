package framework.font;

import framework.loader.ModelLoader;
import framework.resources.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FontFile
{
    private static BufferedReader reader;

    public static Font readFont(String s)
    {
        InputStream stream = Resources.getResource().getResourceAsStream("font/" + s + ".fnt");
        assert stream != null;
        reader = new BufferedReader(new InputStreamReader(stream));
        Font font = new Font();
        getInfo(font);
        getCommons(font);
        getTexture(font);
        getIds(font);
        close();
        return font;
    }

    private static void close()
    {
        try {
            reader.close();
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
        reader = null;
    }

    private static void getTexture(Font font)
    {
        String[] inf = read().split(" ");
        String file = inf[2].split("=")[1];
        int txt = ModelLoader.loadTexture(file.replace("\"", ""));
        font.setTexture(txt);
        read();
    }

    private static void getIds(Font font)
    {
        String line = read();
        while(line != null)
        {
            Char c = new Char();
            String[] inf = line.split(" ");
            int id, xAdvance,  y, x, width, height, xOffset, yOffset;
            id = xAdvance = y = x = width = height = xOffset = yOffset = 0;
            for(String s : inf)
            {
                if(s.isEmpty()) continue;
                if(s.contains("id"))
                    if(s.contains("width"))
                        width = Integer.parseInt(s.split("=")[1]);
                    else id = Integer.parseInt(s.split("=")[1]);
                if(s.contains("x"))
                    if(s.contains("xoffset"))
                        xOffset = Integer.parseInt(s.split("=")[1]);
                    else if(s.contains("xadvance"))
                        xAdvance = Integer.parseInt(s.split("=")[1]);
                    else x = Integer.parseInt(s.split("=")[1]);
                if(s.contains("y"))
                    if(s.contains("yoffset"))
                        yOffset = Integer.parseInt(s.split("=")[1]);
                    else y = Integer.parseInt(s.split("=")[1]);
                if(s.contains("height"))
                    height = Integer.parseInt(s.split("=")[1]);
            }


            c.setId(id)
            .setX(x)
            .setY(y)
            .setWidth(width)
            .setHeight(height)
            .setXOffset(xOffset)
            .setYOffset(yOffset)
            .setXAdvance(xAdvance);

            font.getCharacterMap().put((char) id, c);
            line = read();
        }
    }

    private static void getCommons(Font font)
    {
        String[] inf = read().split(" "); // info
        int lineHeight = Integer.parseInt(inf[1].split("=")[1]);
        int base = Integer.parseInt(inf[2].split("=")[1]);
        int scaleW = Integer.parseInt(inf[3].split("=")[1]);
        int scaleH = Integer.parseInt(inf[4].split("=")[1]);
        font.setLineHeight(lineHeight)
            .setBase(base)
            .setScaleW(scaleW)
            .setScaleH(scaleH);
    }

    private static void getInfo(Font font)
    {
        int indexSkip = 0;
        String[] inf = read().split(" "); // info

        if(inf.length != 12)
            indexSkip = inf.length - 12;

        System.out.println(inf.length);

        String face = inf[1].split("=")[1]; // font face
        int size = Integer.parseInt(inf[indexSkip + 2].split("=")[1]); // font size
        int bold = Integer.parseInt(inf[indexSkip + 3].split("=")[1]); // bold
        int italic = Integer.parseInt(inf[indexSkip + 4].split("=")[1]); // italic
        String charset = inf[indexSkip + 5].split("=")[1]; // charset
        int unicode = Integer.parseInt(inf[indexSkip + 6].split("=")[1]);
        int stretchH = Integer.parseInt(inf[indexSkip + 7].split("=")[1]); // stretchHeight (over 100)
        int smooth = Integer.parseInt(inf[indexSkip + 8].split("=")[1]);
        int aa = Integer.parseInt(inf[indexSkip + 9].split("=")[1]); // aa
        String paddingInfo = inf[indexSkip + 10].split("=")[1];
        int[] padding = new int[] // paddings
        {
                Integer.parseInt(paddingInfo.split(",")[0]),
                Integer.parseInt(paddingInfo.split(",")[1]),
                Integer.parseInt(paddingInfo.split(",")[2]),
                Integer.parseInt(paddingInfo.split(",")[3])
        };
        String spacingInfo = inf[indexSkip + 11].split("=")[1];
        int[] spacing = new int[] // spacing
        {
                Integer.parseInt(spacingInfo.split(",")[0]),
                Integer.parseInt(spacingInfo.split(",")[1])
        };

        font.setSize(size)
            .setBold(bold == 1)
            .setItalic(italic == 1)
            .setStretchH(stretchH)
            .setAA(aa)
            .setPadding(padding)
            .setSpacing(spacing);
    }

    private static String read()
    {
        try {
            return reader.readLine();
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
}
