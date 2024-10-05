package framework.particles;
import framework.hardware.Display;
import framework.lang.Mat4;
import framework.lang.Vec3;
import framework.lang.Vec4;

import java.util.Random;

public class ParticleSystem {

    private ParticleTexture texture;
    private float pps, averageSpeed, gravityCompliant, averageLifeLength, averageScale;

    private float speedError, lifeError, scaleError = 0;
    private boolean randomRotation = false;
    private Vec3 direction;
    private float directionDeviation = 0;

    private Random random = new Random();

    public ParticleSystem(float pps, float speed, float gravityCompliant, float lifeLength, float scale) {
        this.pps = pps;
        this.averageSpeed = speed;
        this.gravityCompliant = gravityCompliant;
        this.averageLifeLength = lifeLength;
        this.averageScale = scale;
    }
    public ParticleSystem(float pps, float speed, float gravityCompliant, float lifeLength, float scale, ParticleTexture texture) {
        this.pps = pps;
        this.averageSpeed = speed;
        this.gravityCompliant = gravityCompliant;
        this.averageLifeLength = lifeLength;
        this.averageScale = scale;
        this.texture = texture;
    }

    /**
     * @param direction - The average direction in which particles are emitted.
     * @param deviation - A value between 0 and 1 indicating how far from the chosen direction particles can deviate.
     */
    public void setDirection(Vec3 direction, float deviation) {
        this.direction = new Vec3(direction);
        this.directionDeviation = (float) (deviation * Math.PI);
    }

    public void randomizeRotation() {
        randomRotation = true;
    }

    /**
     * @param error
     *            - A number between 0 and 1, where 0 means no error margin.
     */
    public void setSpeedError(float error) {
        this.speedError = error * averageSpeed;
    }

    /**
     * @param error
     *            - A number between 0 and 1, where 0 means no error margin.
     */
    public void setLifeError(float error) {
        this.lifeError = error * averageLifeLength;
    }

    /**
     * @param error
     *            - A number between 0 and 1, where 0 means no error margin.
     */
    public void setScaleError(float error) {
        this.scaleError = error * averageScale;
    }

    public void generateParticles(Vec3 systemCenter) {
        Vec3 position = new Vec3(systemCenter);
        float delta = Display.getDeltaInSeconds();
        float particlesToCreate = pps * delta;
        int count = (int) Math.floor(particlesToCreate);
        float partialParticle = particlesToCreate % 1;
        for (int i = 0; i < count; i++) {
            emitParticle(position);
        }
        if (Math.random() < partialParticle) {
            emitParticle(position);
        }
    }

    private void emitParticle(Vec3 center) {
        Vec3 velocity;
        if(direction!=null){
            velocity = generateRandomUnitVectorWithinCone(direction, directionDeviation);
        }else{
            velocity = generateRandomUnitVector();
        }
        velocity.normalize();
        velocity.mul(generateValue(averageSpeed, speedError));
        float scale = generateValue(averageScale, scaleError);
        float lifeLength = generateValue(averageLifeLength, lifeError);
        new Particle(new Vec3(center), velocity, gravityCompliant, lifeLength, generateRotation(), scale, texture);
    }

    private float generateValue(float average, float errorMargin) {
        float offset = (random.nextFloat() - 0.5f) * 2f * errorMargin;
        return average + offset;
    }

    private float generateRotation() {
        if (randomRotation) {
            return random.nextFloat() * 360f;
        } else {
            return 0;
        }
    }

    private static Vec3 generateRandomUnitVectorWithinCone(Vec3 coneDirection, float angle) {
        float cosAngle = (float) Math.cos(angle);
        Random random = new Random();
        float theta = (float) (random.nextFloat() * 2f * Math.PI);
        float z = cosAngle + (random.nextFloat() * (1 - cosAngle));
        float rootOneMinusZSquared = (float) Math.sqrt(1 - z * z);
        float x = (float) (rootOneMinusZSquared * Math.cos(theta));
        float y = (float) (rootOneMinusZSquared * Math.sin(theta));

        Vec4 direction = new Vec4(x, y, z, 1);
        if (coneDirection.x != 0 || coneDirection.y != 0 || (coneDirection.z != 1 && coneDirection.z != -1)) {
            Vec3 rotateAxis = Vec3.cross(Vec3.zAxis, coneDirection, null);
            rotateAxis.normalize();
            float rotateAngle = (float) Math.acos(Vec3.dot(Vec3.zAxis, coneDirection));
            Mat4 rotationMatrix = new Mat4();
            rotationMatrix.rotate(-rotateAngle, rotateAxis);
            Mat4.transform(rotationMatrix, direction, direction);
        } else if (coneDirection.z == -1) {
            direction.z *= -1;
        }
        return new Vec3(direction.x, direction .w, direction.z);
    }

    private Vec3 generateRandomUnitVector() {
        float theta = (float) (random.nextFloat() * 2f * Math.PI);
        float z = (random.nextFloat() * 2) - 1;
        float rootOneMinusZSquared = (float) Math.sqrt(1 - z * z);
        float x = (float) (rootOneMinusZSquared * Math.cos(theta));
        float y = (float) (rootOneMinusZSquared * Math.sin(theta));
        return new Vec3(x, y, z);
    }

}
