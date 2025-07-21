package framework.main;

import framework.environment.Environment;
import framework.hardware.Display;
import framework.loader.ModelLoader;
import framework.model.Model;

import java.util.Arrays;

import static framework.hardware.Display.getLwjglVersionMessage;
import static framework.hardware.Display.getOpenGlVersionMessage;

public class Main
{

    public static Model model;

    private static void start() {

        Display.createDisplay(1280, 720);
        Display.setShowFPSTitle(true);

        Arrays.asList("OpenGL: " + getOpenGlVersionMessage(), "LWJGL: " + getLwjglVersionMessage()).forEach(
                System.out::println);


    }

    public static void main(String... args)
    {

        start();
        float[] vertices = {
                -.5f, .5f, 0,
                -.5f, -.5f, 0,
                .5f, -.5f, 0,
                .5f, .5f, 0,
        };
        int[] indices = {
                0, 1, 3, 3, 1, 2
        };
        model = ModelLoader.loadToVao(vertices, indices);

        Environment.start();

    }
}
