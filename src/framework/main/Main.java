package framework.main;

import framework.entity.Camera;
import framework.entity.Entity;
import framework.entity.Light;
import framework.environment.Engine;
import framework.hardware.Display;
import framework.model.Model;
import framework.model.TexturedModel;
import framework.io.Resources;

import java.util.Arrays;

import static framework.hardware.Display.getLwjglVersionMessage;
import static framework.hardware.Display.getOpenGlVersionMessage;

public class Main
{

    public static Model model;
    public static TexturedModel tm;
    public static Entity ent;
    public static Light light;

    private static void start() {

        Display.createDisplay(1280, 720);
        Display.setShowFPSTitle(true);

        Arrays.asList("OpenGL: " + getOpenGlVersionMessage(), "LWJGL: " + getLwjglVersionMessage()).forEach(
                System.out::println);
        Engine.createThreads();
    }

    public static void main(String... args)
    {
        start();
        Resources.createResourcePool();

        float[] vertices = {
                -.5f, .5f, 0,
                -.5f, -.5f, 0,
                .5f, -.5f, 0,
                .5f, .5f, 0
        };
        float[] textures = {
                0, 0, 0, 1, 1, 1, 1, 0
        };
        int[] indices = {
                0, 1, 3,
                3, 1, 2
        };

        tm = new TexturedModel(
                Resources.getModel("box"),
                Resources.getTexture("box")
        );
        ent = new Entity(tm);
        //ent.setColor(200, 255, 255, 255);
        light = new Light();
        light.setColor(1,1, 1);
        light.setPosition(0, 0, 20);
        ent.setPosition(0, 0, -20);
        ent.setRotation(0, 180,0);
        ent.setScale(1, 1, 1);
        Camera camera = new Camera();
        Engine.setCamera(camera);
        Engine.start();

    }
}
