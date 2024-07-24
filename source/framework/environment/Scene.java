package framework.environment;

import framework.entity.Camera;
import framework.entity.Entity;
import framework.entity.Light;
import framework.entity.Player;
import framework.event.MouseEvent;
import framework.particles.ParticleSystem;
import framework.swing.ContentPane;
import framework.swing.GUITexture;
import framework.terrains.Terrain;
import framework.water.WaterTile;

import java.util.ArrayList;
import java.util.List;

public class Scene
{
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
        //normalMappedEntities = new ArrayList<>();
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

    /*public Vector4f getClipPlane()
    {
        return clipPlane;
    }

    public void setClipPlane(Vector4f clipPlane)
    {
        this.clipPlane = clipPlane;
    }*/

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

    public void add(GUITexture texture)
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

//    public List<Entity> getNormalMappedEntities()
//    {
//        return normalMappedEntities;
//    }

//    public void setParticleSystem(ParticleSystem particleSystem)
//    {
//        this.particleSystem = particleSystem;
//    }
//
//    public ParticleSystem getParticleSystem()
//    {
//        return particleSystem;
//    }

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
