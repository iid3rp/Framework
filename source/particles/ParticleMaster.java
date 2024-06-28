package particles;

import entity.Camera;
import org.lwjgl.util.vector.Matrix4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ParticleMaster
{
    private static List<Particle> particles = new ArrayList<>();
    private static ParticleRenderer renderer;
    public static void initialize(Matrix4f projection)
    {
        renderer = new ParticleRenderer(projection);
    }

    public static void update()
    {
        Iterator<Particle> i = particles.iterator();
        while(i.hasNext())
        {
            Particle particle = i.next();
            boolean stillAlive = particle.update();
            if(!stillAlive)
            {
                i.remove();
            }
        }
    }

    public static void renderParticles(Camera camera)
    {
        renderer.render(particles, camera);
    }

    public static void dispose()
    {
        renderer.dispose();
    }

    public static void addParticle(Particle particle)
    {
        particles.add(particle);
    }

    public ParticleMaster()
    {

    }
}
