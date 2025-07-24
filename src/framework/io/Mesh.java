package framework.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;

public class Mesh implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;
    public static final String MESH_DIRECTORY = System.getProperty("user.home") + File.separator + "framework" + File.separator + "meshes";
    private String name;
    public float[] positions;
    public float[] textures;
    public float[] normals;
    public int[] indices;
    public Mesh(String name)
    {
        this.name = name;
    }

    public float[] getPositions()
    {
        return positions;
    }

    public float[] getTextures()
    {
        return textures;
    }

    public float[] getNormals()
    {
        return normals;
    }

    public int[] getIndices()
    {
        return indices;
    }

    public void setPositions(float[] positions)
    {
        this.positions = positions;
    }

    public void setTextures(float[] textures)
    {
        this.textures = textures;
    }

    public void setNormals(float[] normals)
    {
        this.normals = normals;
    }

    public void setIndices(int[] indices)
    {
        this.indices = indices;
    }

    public void exportObject()
    {
        File directory = new File(MESH_DIRECTORY);
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

    public static Mesh importObject(String fileName)
    {
        File file = new File(fileName);
        if (file.exists()) {
            try(ObjectInputStream stream = new ObjectInputStream(new FileInputStream(file)))
            {
                return (Mesh) stream.readObject();
            }
            catch(IOException | ClassNotFoundException e)
            {
                throw new RuntimeException("Failed to import mesh from file: " + fileName, e);
            }
        }

        // Fallback to classpath resource
        try(ObjectInputStream stream = new ObjectInputStream(Resources.class.getResourceAsStream(
                "/framework/io/meshes/" + fileName)))
        {
            return (Mesh) stream.readObject();
        }
        catch(IOException | ClassNotFoundException e)
        {
            throw new RuntimeException("Failed to import mesh from resources: " + fileName + "\nCouldn't parse the serialized file.", e);
        }
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
