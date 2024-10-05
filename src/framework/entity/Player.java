package framework.entity;

import framework.hardware.Display;
import framework.hardware.Keyboard;
import framework.lang.Vec3;
import framework.model.TexturedModel;
import framework.terrain.Terrain;

import static org.lwjgl.glfw.GLFW.*;

public class Player extends Entity {
    private static final float RUN_SPEED = 70f;  // units per second
    private static final float TURN_SPEED = 160;    // degrees per second
    public static final float GRAVITY = -50;
    private static final float JUMP_POWER = 30;
    private float walkSpeed;
    private Vec3 position;
    private float currentSpeed;
    private float currentTurnSpeed;
    private float upwardSpeed;
    public Camera camera;
    private boolean isInAir;
    private Light light; // referential purposes only

    public Player(TexturedModel texturedModel, Vec3 position, float rotationX, float rotationY, float rotationZ, float scale) {
        super(texturedModel, position, rotationX, rotationY, rotationZ, scale);
        walkSpeed = RUN_SPEED;
    }

    public Player(Entity entity, Camera camera)
    {
        super(entity);
        this.camera = camera;
        setRotationY(180);
        walkSpeed = RUN_SPEED;
    }

    public Player(Entity entity)
    {
        super(entity);
        entity.transformRotation(0, 100, 0);
        camera = new Camera(this);
        walkSpeed = RUN_SPEED;
    }

    public void move(Terrain terrain)
    {
        movePlayer(terrain);
        camera.movePlayerCamera();
    }

    public void moveReferenceMethod(Terrain terrain) {
        checkInputs();

        // Calculate movement
        super.transformRotation(0, currentTurnSpeed * Display.getDeltaInSeconds(), 0);
        float distance = currentSpeed * Display.getDeltaInSeconds();
        float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotationY())));
        float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotationY())));
        super.transformPosition(dx, 0, dz);

        // Calculate jump
        upwardSpeed += GRAVITY * Display.getDeltaInSeconds();
        super.transformPosition(0, upwardSpeed * Display.getDeltaInSeconds(), 0);

        // Player terrain collision detection
        float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
        if (super.getPosition().y < terrainHeight) {
            upwardSpeed = 0;
            super.getPosition().y = terrainHeight;
        }
    }

    public void movePlayer(Terrain terrain)
    {
        // player movement radian...
        float angleInRadians = (float) Math.toRadians(camera.getYaw());
        float distance = walkSpeed * Display.getDeltaInSeconds();

        // camera angle distance
        float startingAngle = Math.abs((camera.getYaw() + 180) % 360);
        float currentPlayerAngle = (((getRotationY() + startingAngle) % 360) + 360) % 360;

        float dx = (float) (distance * Math.sin(angleInRadians));
        float dz = (float) (distance * Math.cos(angleInRadians));

        // the reference of the angle
        float angle = 0f;
        boolean isMoving = false;

        if(!Keyboard.isKeyDown(Keyboard.TAB))
        {
            if(Keyboard.isKeyDown(Keyboard.W))
            {
                getPosition().x += dx;
                getPosition().z -= dz;
                angle = Keyboard.isKeyDown(Keyboard.A)? 45 :
                        Keyboard.isKeyDown(Keyboard.D)? 315 : 0;
                isMoving = true;
            }
            if(Keyboard.isKeyDown(Keyboard.D))
            {
                getPosition().x += dz;
                getPosition().z += dx;
                angle = Keyboard.isKeyDown(Keyboard.W)? 315 :
                        Keyboard.isKeyDown(Keyboard.S)? 225 : 270;
                isMoving = true;
            }
            if(Keyboard.isKeyDown(Keyboard.A))
            {
                getPosition().x -= dz;
                getPosition().z -= dx;
                angle = Keyboard.isKeyDown(Keyboard.W)? 45 :
                        Keyboard.isKeyDown(Keyboard.S)? 135 : 90;
                isMoving = true;
            }
            if(Keyboard.isKeyDown(Keyboard.S))
            {
                getPosition().x -= dx;
                getPosition().z += dz;
                angle = Keyboard.isKeyDown(Keyboard.A)? 135 :
                        Keyboard.isKeyDown(Keyboard.D)? 225 : 180;
                isMoving = true;
            }
            if(Keyboard.isKeyDown(Keyboard.SPACE))
            {
                jump();
            }
        }

        float angleRotationBase = (((startingAngle + angle) % 360) - camera.getYaw() + 180) % 360;
        float shortestAngle = shortAngle(angleRotationBase, currentPlayerAngle);

        if(isMoving)
        {
            if(getRotationY() % 360 != angleRotationBase)
            {
                transformRotation(0, shortestAngle * Display.getDeltaInSeconds() * 12, 0);
            }
        }
        upwardSpeed += GRAVITY * Display.getDeltaInSeconds();
        transformPosition(0, upwardSpeed * Display.getDeltaInSeconds(), 0);
        float terrainHeight = terrain.getHeightOfTerrain(getPosition().x, getPosition().z);
        if(getPosition().y < terrainHeight)
        {
            upwardSpeed = 0;
            isInAir = false;
            getPosition().y = terrainHeight;
        }
        float getPositionX = getPosition().x;
        float getPositionY = getPosition().y + 10;
        float getPositionZ = getPosition().z;
        if(light != null)
        {
            light.setPosition(new Vec3(getPositionX, getPositionY, getPositionZ));
        }
    }

    private float shortAngle(float target, float angle)
    {
        float angleDiff = target - angle;

        angleDiff = (angleDiff > 180) ? angleDiff - 360 :
                (angleDiff < -180) ? angleDiff + 360 :
                        angleDiff;
        return angleDiff;
    }

    private void jump() {
        if(!isInAir)
        {
            this.upwardSpeed = JUMP_POWER;
            isInAir = true;
        }
    }

    private void checkInputs() {
        if (Keyboard.isKeyDown(GLFW_KEY_W) || Keyboard.isKeyDown(GLFW_KEY_UP)) {
            currentSpeed = RUN_SPEED;
        } else if (Keyboard.isKeyDown(GLFW_KEY_S) || Keyboard.isKeyDown(GLFW_KEY_DOWN)) {
            currentSpeed = -RUN_SPEED;
        } else {
            currentSpeed = 0;
        }

        if (Keyboard.isKeyDown(GLFW_KEY_D) || Keyboard.isKeyDown(GLFW_KEY_RIGHT)) {
            currentTurnSpeed = -TURN_SPEED;
        } else if (Keyboard.isKeyDown(GLFW_KEY_A) || Keyboard.isKeyDown(GLFW_KEY_LEFT)) {
            currentTurnSpeed = TURN_SPEED;
        } else {
            currentTurnSpeed = 0;
        }

        if (Keyboard.isKeyDown(GLFW_KEY_SPACE)) {
            jump();
        }
    }

    public Camera getCamera()
    {
        return camera;
    }
}
