package Main;

import render.DisplayManager;
import render.MasterRenderer;
import render.ModelLoader;
import streamio.Resources;

public class LoadingResources
{
    public static void main(String[] args)
    {
        DisplayManager.createDisplay();
        MasterRenderer.setRenderer();
        Resources.setResource("resources");
        Resources.loadAllResources();
        MasterRenderer.dispose();
        ModelLoader.dispose();
        DisplayManager.closeDisplay();
    }
}
