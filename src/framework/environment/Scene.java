package framework.environment;

import framework.entity.Camera;
import framework.entity.Entity;
import framework.entity.Light;
import framework.entity.Player;
import framework.event.MouseEvent;
import framework.hardware.Display;
import framework.particles.ParticleSystem;
import framework.swing.Container;
import framework.swing.ContentPane;
import framework.swing.GUITexture;
import framework.terrain.Terrain;
import framework.water.WaterTile;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class Scene
{
    public static Vector2f offset;
    private List<Entity> entities;
    private List<Light> lights;
    private List<WaterTile> waters;
    private Terrain terrain;

    private Camera camera;
    private Player player;
    private ContentPane pane;
    public MouseEvent event;
    private ParticleSystem particleSystem;

    //private Vector4f clipPlane;
    public Scene()
    {
        pane = new ContentPane();
        entities = new ArrayList<>();
        lights = new ArrayList<>();
        terrain = new Terrain();
        waters = new ArrayList<>();
        offset = new Vector2f();
    }

    public static Vector2f getOffset()
    {
        return offset;
    }

    public static void setOffset(int x, int y)
    {
        float _x = (float) x / Display.getWidth();
        float _y = (float) y / Display.getHeight();
        offset = new Vector2f(_x, _y);
    }

    public List<Entity> getEntities()
    {
        return entities;
    }

    public Player getPlayer()
    {
        return player;
    }

    public Terrain getTerrain()
    {
        return terrain;
    }

    public void add(WaterTile water)
    {
        waters.add(water);
    }
    public void add(Entity entity)
    {
        entities.add(entity);
    }

    public List<Light> getLights()
    {
        return lights;
    }

    public void setTerrain(Terrain terrain)
    {
        this.terrain = terrain;
    }

    public void add(Light light)
    {
        lights.add(light);
    }

    public void add(Container texture)
    {
        pane.add(texture);
    }

    public void setPlayer(Player player)
    {
        this.player = player;
    }

    public ContentPane getContentPane()
    {
        return pane;
    }

    public void setContentPane(ContentPane pane)
    {
        this.pane = pane;
    }

    public MouseEvent getEvent()
    {
        return event;
    }

    public void setEvent(MouseEvent event)
    {
        this.event = event;
    }

    public void setCamera(Camera camera)
    {
        this.camera = camera;
    }

    public Camera getCamera()
    {
        return camera;
    }

    public void setTerrainSize(float x, float z)
    {
        terrain.x = x;
        terrain.z = z;
    }

    public Light getMainLight()
    {
        return lights.getFirst();
    }

    @Override
    public String toString() {
        int numEntities = entities.size();
        int numLights = lights.size();
        int numGUITextures = pane.getComponents().size();
        int numTerrainVertices = terrain.getModel().getVertexCount();

        return "Scene Information:\n" +
                "Number of Entities: " + numEntities + "\n" +
                "Number of Lights: " + numLights + "\n" +
                "Number of GUI Textures: " + numGUITextures + "\n" +
                "Number of Terrain Vertices: " + numTerrainVertices;
    }

    public List<WaterTile> getWaters()
    {
        return waters;
    }

    public ParticleSystem getParticleSystem()
    {
        return particleSystem;
    }

    public void setParticleSystem(ParticleSystem particleSystem)
    {
        this.particleSystem = particleSystem;
    }
}
