package framework;

import framework.event.MouseAdapter;
import framework.event.MouseEvent;
import framework.lang.Vec3;
import framework.lang.Vec4;
import framework.loader.ModelLoader;
import framework.entity.Light;
import framework.entity.Player;
import framework.environment.Environment;
import framework.environment.Scene;
import framework.model.TexturedModel;
import framework.particles.ParticleSystem;
import framework.particles.ParticleTexture;
import framework.swing.ContentPane;
import framework.swing.PictureBox;
import framework.terrain.Terrain;
import framework.textures.TerrainTexture;
import framework.textures.TerrainTexturePack;
import framework.textures.Texture;
import framework.entity.Entity;
import framework.water.WaterTile;

import java.util.Random;

import static framework.hardware.Display.createDisplay;
import static framework.hardware.Display.getLwjglVersionMessage;
import static framework.hardware.Display.getOpenGlVersionMessage;
import static framework.hardware.Display.setShowFPSTitle;
import static java.lang.management.ManagementFactory.getRuntimeMXBean;

public class Game
{
    static Random random = new Random();

    private static void start() {
        
        createDisplay(1280, 720);

        // Show FPS title only if debugging
        if (getRuntimeMXBean().getInputArguments().toString().contains("-agentlib:jdwp")) {
            setShowFPSTitle(true);
        }

        // uhm how about yes?
        setShowFPSTitle(true);

        System.out.println("OpenGL: " + getOpenGlVersionMessage());
        System.out.println("LWJGL: " + getLwjglVersionMessage());
    }

    public static void main(String[] args) {
        start();
        Scene scene = new Scene();
        ContentPane panel = new ContentPane();
        Environment.setScene(scene);


        Light lighting = new Light(new Vec3(100_000, 100_000, -100_000), new Vec3(.5f, .5f, .25f));

        scene.getLights().addAll(lighting);
        scene.getLights().addAll(new Light(new Vec3(0, 10, 0), new Vec3(1, 0, 1), new Vec3(1, 0f, 200f)));
        scene.getLights().addAll(new Light(new Vec3(20, 0, 20), new Vec3(1, 0, 1), new Vec3(1, 0.01f, 0.02f)));
        scene.getLights().addAll(new Light(new Vec3(40, 0, 40), new Vec3(1, 0, 1), new Vec3(1, 0.1f, 0.002f)));
        scene.getLights().addAll(new Light(new Vec3(-20, 0, 60), new Vec3(1, 0, 0), new Vec3(1, 0.01f, 0.02f)));
        scene.getLights().addAll(new Light(new Vec3(0, 0, -80), new Vec3(1, 0, 1), new Vec3(1, 0.1f, 0.002f)));

        int grass = ModelLoader.loadTexture("grass.png");
        TerrainTexturePack bg = new TerrainTexturePack(
                new TerrainTexture(grass),
                new TerrainTexture(grass),
                new TerrainTexture(grass),
                new TerrainTexture(grass)
        );
        TerrainTexture blend = new TerrainTexture(ModelLoader.loadTexture("blendMap.png"));
        Terrain terrain = new Terrain(0, 0, bg, blend);

        scene.setTerrain(terrain);

        TexturedModel barrel = new TexturedModel(ObjectLoader.loadObject("barrel.obj"),
                new Texture(ModelLoader.loadTexture("barrel.png")));
        barrel.getTexture().setNormalMap(ModelLoader.loadTexture("barrelNormal.png"));
        barrel.getTexture().setReflectivity(.002f);
        barrel.getTexture().setShineDampening(1);
        Entity barrelEntity = new Entity(barrel, new Vec3(0, 200, 0), 0, 0, 0, 1);
        Entity reference = new Entity(barrel, new Vec3(212, 0, 607), 0, 0, 0, 1);
        scene.getEntities().addAll(barrelEntity);
        Random random = new Random();
        int progress = 0;

        PictureBox image = new PictureBox();
        image.setTexture(ModelLoader.loadTexture("brat.png"));
        image.setLocation(0, 0);
        image.setScale(100, 100);
        image.setSize(100, 100);
        //panel.add(image);



        Player player = new Player(barrelEntity);
        //player.setLight();
        //player.setLightColor(255, 255, 255);
        //player.setLightAttenuation(new Vec3(.1f, .01f, .01f));
        scene.setPlayer(player);
        scene.getEntities().addAll(player);
        //scene.getLights().add(player.getLight());
        scene.setCamera(player.getCamera());

        scene.setContentPane(panel);

        Entity entity = new Entity(new TexturedModel(
                ObjectLoader.loadObject("crate.obj"),
                new Texture(ModelLoader.loadTexture("brat.png"))),
                new Vec3(0, 0, 0), 0f, 0f, 0f, 1f);
        //scene.add(entity);
        scene.add(new WaterTile(0,0, 0));

        TexturedModel chrysalis = new TexturedModel(ObjectLoader.loadObject("tree.obj"), new Texture(ModelLoader.loadTexture("grass.png")));
        chrysalis.getTexture().setShineDampening(1f);
        chrysalis.getTexture().setReflectivity(.1f);

        for(int i = 0 ; i < 500; i++) {
            float x = random.nextFloat(terrain.getSize()) - (terrain.getSize() / 2);
            float z = random.nextFloat(terrain.getSize()) - (terrain.getSize() / 2);
            float y = terrain.getHeightOfTerrain(x, z);
            Entity crystal = new Entity(chrysalis, new Vec3(x, y, z), (float) 0, 0, 0, 10f + ((float) Math.random() * 20));
            crystal.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseEntered(MouseEvent e) {
                    crystal.setHighlightColor(new Vec4(1, 0, 0, 1));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    crystal.setHighlightColor(new Vec4(0, 0, 0, 0));
                }

            });
            if(crystal.getPosition().y > 0) {
                scene.getEntities().addAll(crystal);
            }
            else i--;
        }

        ParticleSystem system = new ParticleSystem(
                100,
                25,
                .075f,
                30,
                30,
                new ParticleTexture(
                        ModelLoader.loadTexture("brat.png"),1, true
                )
        );
        //system.setDirection(new Vec3(20, 100, 20), .5f);
        system.setLifeError(.1f);
        system.setScaleError(2f);
        system.setSpeedError(.7f);
        scene.setParticleSystem(system);
        WaterTile tile = new WaterTile(0, 0,0);
        scene.add(tile);



        //Environment.run(new Count());
        Environment.start();
    }
}
