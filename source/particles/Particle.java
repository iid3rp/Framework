package particles;

import entity.Camera;
import entity.Player;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import render.DisplayManager;

public class Particle {
    private Vector3f position;
    private Vector3f velocity;
    private float gravity;
    private float lifeLength;
    private float rotation;
    private float scale;
    private ParticleTexture texture;
    private Vector2f textureOffset1 = new Vector2f();
    private Vector2f textureOffset2 = new Vector2f();
    private float blendFactor;
    private float distance;
    private Vector3f change = new Vector3f();

    private float elapsedTime = 0;


    public Particle(Vector3f position, Vector3f velocity, float gravity, float lifeLength, float rotation, float scale, ParticleTexture texture)
    {
        this.texture = texture;
        this.position = position;
        this.velocity = velocity;
        this.gravity = gravity;
        this.lifeLength = lifeLength;
        this.rotation = rotation;
        this.scale = scale;
        ParticleMaster.addParticle(this);
    }

    public float getDistance()
    {
        return distance;
    }

    public Vector2f getTextureOffset1()
    {
        return textureOffset1;
    }

    public Vector2f getTextureOffset2()
    {
        return textureOffset2;
    }

    public float getBlendFactor()
    {
        return blendFactor;
    }

    public ParticleTexture getTexture()
    {
        return texture;
    }

    public void setTexture(ParticleTexture texture)
    {
        this.texture = texture;
    }

    public Particle(Vector3f position,
                    Vector3f velocity,
                    float gravity,
                    float lifeLength,
                    float rotation,
                    float scale)
    {
        this.position = position;
        this.velocity = velocity;
        this.gravity = gravity;
        this.lifeLength = lifeLength;
        this.rotation = rotation;
        this.scale = scale;
        ParticleMaster.addParticle(this);
    }

    public Vector3f getPosition()
    {
        return position;
    }

    public void setPosition(Vector3f position)
    {
        this.position = position;
    }

    public Vector3f getVelocity()
    {
        return velocity;
    }

    public void setVelocity(Vector3f velocity)
    {
        this.velocity = velocity;
    }

    public float getGravity()
    {
        return gravity;
    }

    public void setGravity(float gravity)
    {
        this.gravity = gravity;
    }

    public float getLifeLength()
    {
        return lifeLength;
    }

    public void setLifeLength(float lifeLength)
    {
        this.lifeLength = lifeLength;
    }

    public float getRotation()
    {
        return rotation;
    }

    public void setRotation(float rotation)
    {
        this.rotation = rotation;
    }

    public float getScale()
    {
        return scale;
    }

    public void setScale(float scale)
    {
        this.scale = scale;
    }

    public float getElapsedTime()
    {
        return elapsedTime;
    }

    protected boolean update(Camera camera)
    {
        velocity.y += Player.GRAVITY * gravity * DisplayManager.getFrameTimeSeconds();
        change.set(velocity);
        change.scale(DisplayManager.getFrameTimeSeconds());
        Vector3f.add(change, position, position);
        distance = Vector3f.sub(camera.getPosition(),position, null).lengthSquared();
        updateTextureCoordinates();
        elapsedTime += DisplayManager.getFrameTimeSeconds();
        return elapsedTime < lifeLength;
    }

    private void updateTextureCoordinates()
    {
        float lifeFactor = elapsedTime / lifeLength;
        int stageCount = texture.getNumOfRows() * texture.getNumOfRows();
        float atlasProgression = lifeFactor * stageCount;
        int index1 = (int) Math.floor(atlasProgression);
        int index2 = index1 < stageCount - 1? index1 + 1 : index1;
        this.blendFactor = atlasProgression % 1;
        setTextureOffset(textureOffset1, index1);
        setTextureOffset(textureOffset2, index2);
    }

    private void setTextureOffset(Vector2f offset,  int index)
    {
        int column = index % texture.getNumOfRows();
        int row = index / texture.getNumOfRows();
        offset.x = (float) column / texture.getNumOfRows();
        offset.y = (float) row / texture.getNumOfRows();
    }
}
