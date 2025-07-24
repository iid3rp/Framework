package framework.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;

public class BufferTexture implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;
    private int[] array;
    private int width;
    private int height;
    private String name;
    public static final String TEXTURE_DIR = System.getProperty("user.home") + File.separator + "framework" + File.separator + "textures";

    public BufferTexture(String name, int width, int height)
    {
        this.name = name;
        this.width = width;
        this.height = height;
    }

    public int[] getArray()
    {
        return array;
    }

    public void exportObject()
    {
        File directory = new File(TEXTURE_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file = new File(directory, name + ".ser");
        try(ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file)))
        {
            stream.writeObject(this);
        }
        catch(IOException e) {
            throw new RuntimeException("Failed to export mesh: " + name, e);
        }
    }

    public static BufferTexture importObject(String fileName)
    {
        File file = new File(fileName);
        if (file.exists()) {
            try(ObjectInputStream stream = new ObjectInputStream(new FileInputStream(file)))
            {
                return (BufferTexture) stream.readObject();
            }
            catch(IOException | ClassNotFoundException e)
            {
                throw new RuntimeException("Failed to import mesh from file: " + fileName, e);
            }
        }
        throw new RuntimeException("Failed to import mesh from file: " + fileName);
    }

    public void setArray(int[] array)
    {
        this.array = array;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
