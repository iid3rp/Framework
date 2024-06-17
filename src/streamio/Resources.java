package streamio;

import model.Model;
import render.ModelLoader;
import texture.Texture;
import render.ObjectLoader;

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
    public static String resource = "resources";

    public static void setResource(String resourceFileName)
    {
        resource = resourceFileName;

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

    public static String getResource()
    {
        return resource;
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

        if(objectFiles != null)
        {
            float progress = 0;
            for(File file : objectFiles)
            {
                String name = file.getName().substring(0, file.getName().lastIndexOf("."));
                Model model = ObjectLoader.loadObject(name);
                models.put(name, model);
                System.out.println("Loading Object Models: " + ((++progress / objectFiles.length) * 100f) + "%");
            }
        }
        if(textureFiles != null)
        {
            float progress = 0;
            for(File file : textureFiles)
            {
                String name = file.getName().substring(0, file.getName().lastIndexOf("."));
                Texture texture = new Texture(ModelLoader.loadTexture(name));
                textures.put(name, texture);
                System.out.println("Loading Object Textures: " + ((++progress / textureFiles.length) * 100f) + "%");
            }
        }
    }
}
