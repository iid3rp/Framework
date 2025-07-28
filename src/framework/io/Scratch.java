package framework.io;

import framework.loader.ObjectLoader;
import framework.loader.TextureLoader;

public class Scratch
{
    public static void main(String... args)
    {
        ObjectLoader.loadAllObjects();
        TextureLoader.loadAllTextures();
    }
}
