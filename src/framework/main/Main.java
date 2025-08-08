package framework.main;

import framework.entity.Camera;
import framework.entity.Entity;
import framework.entity.Light;
import framework.environment.Engine;
import framework.environment.Scene;
import framework.hardware.Display;
import framework.model.Model;
import framework.model.TexturedModel;
import framework.io.Resources;

import java.util.Arrays;
import java.util.Random;

import static framework.hardware.Display.getLwjglVersionMessage;
import static framework.hardware.Display.getOpenGlVersionMessage;

public class Main
{

    public static Model model;
    public static TexturedModel tm;
    public static Entity ent;
    public static Light light;
    public static Scene scene;

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
        scene = new Scene();
        Resources.createResourcePool();

        tm = new TexturedModel(
                Resources.getModel("box"),
                Resources.getTexture("box")
        );


        light = new Light();
        light.setColor(1,1, 1);
        light.setPosition(0, 0, 20);

        ent = new Entity(tm);
        ent.setPosition(0, 0, -20);
        ent.setRotation(0, 180,0);
        ent.setScale(1, 1, 1);

        Random r = new Random();

        for(int i = 0; i < 1000; i++)
        {
            Entity entity = new Entity(tm);
            entity.setPosition(r.nextFloat(200) - 100, r.nextFloat(100) - 50, -r.nextFloat(100));
            entity.setScale(1,1, 1);
            entity.setName(i + "");
            scene.add(entity);
        }

        Camera camera = new Camera();

        scene.add(ent);
        scene.add(light);
        scene.setCamera(camera);

        Engine.setScene(scene);
        Engine.start();

    }
}
