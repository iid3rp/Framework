package framework.io;

import framework.loader.ModelLoader;
import framework.loader.TextureLoader;
import framework.model.Model;
import framework.textures.Texture;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static framework.io.BufferTexture.TEXTURE_DIR;

public final class Resources
{
    private static Class<?> resourceField = Resources.class;

    private static Map<String, Model> meshTable;
    private static Map<String, Texture> textureTable;

    public static Model getModel(String name)
    {
        return meshTable.get(name);
    }

    public static Texture getTexture(String name)
    {
        return textureTable.get(name);
    }

    public static Class<?> getClassResource()
    {
        return resourceField;
    }

    public static void createResourcePool()
    {
        meshTable = new HashMap<>();
        textureTable = new HashMap<>();

        File textureDir = new File(System.getProperty("user.home") + File.separator + "framework" + File.separator + "textures");
        System.out.println(textureDir.getAbsolutePath());
        if (!textureDir.exists()) {
            System.out.println("Mesh directory does not exist.");
            return;
        }

        File[] files = textureDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".ser"));
        if (files == null || files.length == 0) {
            System.out.println("No Texture files found in: " + TEXTURE_DIR);
            return;
        }

        for (File file : files) {
            String fileName = file.getName();
            String meshName = fileName.substring(0, fileName.lastIndexOf('.'));

            BufferTexture texture = BufferTexture.importObject(file.getAbsolutePath());
            textureTable.put(texture.getName(), TextureLoader.loadBuffer(texture));
            System.out.println("Processed and imported: " + meshName);
        }


        // meshes

        File meshDir = new File(System.getProperty("user.home") + File.separator + "framework" + File.separator + "meshes");

        files = meshDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".ser"));
        if (files == null || files.length == 0) {
            System.out.println("No OBJ files found in: " + Mesh.MESH_DIRECTORY);
            return;
        }

        for (File file : files) {
            String fileName = file.getName();
            String meshName = fileName.substring(0, fileName.lastIndexOf('.'));

            Mesh mesh = Mesh.importObject(file.getAbsolutePath());
            Model model;
            meshTable.put(mesh.getName(), model = ModelLoader.loadToVaoInt(mesh.positions, mesh.textures, mesh.normals, mesh.indices));
            System.out.println("Processed and imported: " + meshName + "\nModel ID: " + model.getVaoId() + " Count: " + model.getVertexCount());
        }

    }

    public void setResource(Class<?> resource)
    {
        this.resourceField = resource;
    }


}
