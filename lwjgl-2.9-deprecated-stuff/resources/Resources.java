package framework.resources;

import framework.model.Model;
import framework.display.ObjectLoader;
import framework.texture.Texture;

import java.io.File;
import java.util.HashMap;

public final class Resources
{
    private Resources() {}

    public static HashMap<String, Model> models = new HashMap<>();
    public static HashMap<String, Texture> textures = new HashMap<>();
    public static File resourcesFolder;
    public static File objectsFolder;
    public static File texturesFolder;

    public static void setResource(String resourceFileName)
    {
        resourcesFolder = new File( resourceFileName);
        var a = resourcesFolder.mkdirs();

        objectsFolder = new File(resourceFileName + "/" + "objects");
        var b = objectsFolder.mkdirs();

        texturesFolder = new File(resourceFileName + "/" + "textures");
        var c = texturesFolder.mkdirs();

        if(a || b || c)
        {
            System.out.println("Resources Files Have Been Made.");
            System.out.println("Have fun developing! :3");
        }
    }

    public static void loadAllResources()
    {
        File[] objectFiles = objectsFolder.listFiles(file ->
            file.isFile() &&
            file.getName().endsWith(".obj")
        );

        File[] textureFiles = texturesFolder.listFiles(file ->
            file.isFile() &&
            file.getName().endsWith(".png")
        );

        if(objectFiles != null) {
            for(File file : objectFiles)
            {
                ObjectLoader.loadObject(file.getPath());
            }
        }
    }
}
