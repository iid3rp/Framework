package framework.particles;


import framework.hardware.Display;
import framework.entity.Camera;
import framework.entity.Player;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Particle {

    private Vector3f position;
    private Vector3f velocity;
    private float gravity;
    private float lifeLength;
    private float rotation;
    private float scale;

    private float elapsedTime = 0;
    private Vector3f change = new Vector3f();
    private ParticleTexture texture;
    private float distance;

    private Vector2f[] offset = new Vector2f[]
    {
            new Vector2f(),
            new Vector2f()
    };
    private float blend;

    public Particle(Vector3f position, Vector3f velocity, float gravity, float lifeLength, float rotation, float scale)
    {
        this.position = new Vector3f(position);
        this.velocity = new Vector3f(velocity);
        this.gravity = gravity;
        this.lifeLength = lifeLength;
        this.rotation = rotation;
        this.scale = scale;
        ParticleMaster.addParticle(this);
    }

    public Particle(Vector3f position, Vector3f velocity, float gravity, float lifeLength, float rotation, float scale, ParticleTexture texture)
    {
        this.position = position;
        this.velocity = velocity;
        this.gravity = gravity;
        this.lifeLength = lifeLength;
        this.rotation = rotation;
        this.scale = scale;
        this.texture = texture;
        ParticleMaster.addParticle(this);
    }

    public ParticleTexture getTexture()
    {
        return texture;
    }

    protected boolean update(Camera camera)
    {
        velocity.y += Player.GRAVITY * gravity * Display.getDeltaInSeconds();
        change.set(velocity);
        change.mul(Display.getDeltaInSeconds());
        position.add(change);
        distance = new Vector3f(camera.getPosition()).sub(position).lengthSquared();
        updateTextureCoordsInfo();
        elapsedTime += Display.getDeltaInSeconds();
        return elapsedTime < lifeLength;
    }

    private void updateTextureCoordsInfo()
    {
        float lifeFactor = elapsedTime / lifeLength;
        int stageCount = (int) Math.pow(texture.getNumOfRows(), 2);
        float atlasProgression = lifeFactor * stageCount;
        int index1 = (int) Math.floor(atlasProgression);
        int index2 = index1 < stageCount - 1 ? index1 + 1 : index1;
        this.blend = atlasProgression % 1;
        setTextureOffset(offset[0], index1);
        setTextureOffset(offset[1], index2);
    }

    private void setTextureOffset(Vector2f vec2, int index)
    {
        int column = index % offset.length;
        int row = index / offset.length;
        vec2.x = (float) column / texture.getNumOfRows();
        vec2.y = (float) row / texture.getNumOfRows();
    }

    public Vector3f getPosition()
    {
        return position;
    }

    public Vector3f getVelocity()
    {
        return velocity;
    }

    public float getGravity()
    {
        return gravity;
    }

    public float getLifeLength()
    {
        return lifeLength;
    }

    public float getRotation()
    {
        return rotation;
    }

    public float getScale()
    {
        return scale;
    }

    public float getBlend()
    {
        return blend;
    }

    public Vector2f[] getOffset()
    {
        return offset;
    }

    public float getDistance()
    {
        return distance;
    }
}
