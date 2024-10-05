package framework.particles;


import framework.hardware.Display;
import framework.entity.Camera;
import framework.entity.Player;
import framework.lang.Vec2;
import framework.lang.Vec3;

public class Particle {

    private Vec3 position;
    private Vec3 velocity;
    private float gravity;
    private float lifeLength;
    private float rotation;
    private float scale;

    private float elapsedTime = 0;
    private Vec3 change = new Vec3();
    private ParticleTexture texture;
    private float distance;

    private Vec2[] offset = new Vec2[]
    {
            new Vec2(),
            new Vec2()
    };
    private float blend;

    public Particle(Vec3 position, Vec3 velocity, float gravity, float lifeLength, float rotation, float scale)
    {
        this.position = new Vec3(position);
        this.velocity = new Vec3(velocity);
        this.gravity = gravity;
        this.lifeLength = lifeLength;
        this.rotation = rotation;
        this.scale = scale;
        ParticleMaster.addParticle(this);
    }

    public Particle(Vec3 position, Vec3 velocity, float gravity, float lifeLength, float rotation, float scale, ParticleTexture texture)
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
        distance = Vec3.sub(camera.getPosition(), position, null).lengthSquared();
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

    private void setTextureOffset(Vec2 vec2, int index)
    {
        int column = index % offset.length;
        int row = index / offset.length;
        vec2.x = (float) column / texture.getNumOfRows();
        vec2.y = (float) row / texture.getNumOfRows();
    }

    public Vec3 getPosition()
    {
        return position;
    }

    public Vec3 getVelocity()
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

    public Vec2[] getOffset()
    {
        return offset;
    }

    public float getDistance()
    {
        return distance;
    }
}
