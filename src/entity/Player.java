package entity;

import model.TexturedModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;
import render.DisplayManager;
import terrain.Terrain;
import util.Intention;

public class Player extends Entity
{
    private static  final float RUN_SPEED = 40f;
    private static final float TURN_SPEED = 100f;
    private static final float GRAVITY = -98.1f;
    private static final float JUMP_POWER = 40;

    private static final float TERRAIN_Y = 0;

    private float walkSpeed;
    private float rotationalSpeed;
    private float upwardSpeed;
    public Camera camera;
    private boolean isInAir;

    public Player(TexturedModel model, Vector3f position, float rotationX, float rotationY, float rotationZ, float scale)
    {
        super(model, position, rotationX, rotationY, rotationZ, scale);
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

    private void jump()
    {
        if(!isInAir)
        {
            this.upwardSpeed = JUMP_POWER;
            isInAir = true;
        }
    }

    private float calculateTargetAngle(float targetAngle)
    {
        float currentAngle = getRotationY();
        float angleDifference = targetAngle - currentAngle;
        float shortestAngle = (angleDifference + 180) % 360 - 180;
        return shortestAngle;
    }

    private void adjustRotationSpeed(float targetAngle)
    {
        float angleTurn = Math.min(targetAngle, -targetAngle);
        float rotationalSpeed = angleTurn == targetAngle? angleTurn : -angleTurn;

        if(this.rotationalSpeed != rotationalSpeed)
        {
            this.rotationalSpeed = rotationalSpeed;
        }
    }

    public void movePlayer(Terrain terrain)
    {
        // player movement radian...
        float angleInRadians = (float) Math.toRadians(camera.getYaw());
        float distance = walkSpeed * DisplayManager.getFrameTimeSeconds();

        // camera angle distance
        float startingAngle = Math.abs((camera.getYaw() + 180) % 360);
        float currentPlayerAngle = (((getRotationY() + startingAngle) % 360) + 360) % 360;

        float dx = (float) (distance * Math.sin(angleInRadians));
        float dz = (float) (distance * Math.cos(angleInRadians));

        // the reference of the angle
        float angle = 0f;
        boolean isMoving = false;

        if(!Keyboard.isKeyDown(Keyboard.KEY_TAB))
        {
            if(Keyboard.isKeyDown(Keyboard.KEY_W))
            {
                getPosition().x += dx;
                getPosition().z -= dz;
                angle = Keyboard.isKeyDown(Keyboard.KEY_A)? 45 :
                        Keyboard.isKeyDown(Keyboard.KEY_D)? 315 : 0;
                isMoving = true;
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_D))
            {
                getPosition().x += dz;
                getPosition().z += dx;
                angle = Keyboard.isKeyDown(Keyboard.KEY_W)? 315 :
                        Keyboard.isKeyDown(Keyboard.KEY_S)? 225 : 270;
                isMoving = true;
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_A))
            {
                getPosition().x -= dz;
                getPosition().z -= dx;
                angle = Keyboard.isKeyDown(Keyboard.KEY_W)? 45 :
                        Keyboard.isKeyDown(Keyboard.KEY_S)? 135 : 90;
                isMoving = true;
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_S))
            {
                getPosition().x -= dx;
                getPosition().z += dz;
                angle = Keyboard.isKeyDown(Keyboard.KEY_A)? 135 :
                        Keyboard.isKeyDown(Keyboard.KEY_D)? 225 : 180;
                isMoving = true;
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_SPACE))
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
                transformRotation(0, shortestAngle * DisplayManager.getFrameTimeSeconds() * 12, 0);
            }
        }
        upwardSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
        transformPosition(0, upwardSpeed * DisplayManager.getFrameTimeSeconds(), 0);
        float terrainHeight = terrain.getHeightOfTerrain(getPosition().x, getPosition().z);
        if(getPosition().y < terrainHeight)
        {
            upwardSpeed = 0;
            isInAir = false;
            getPosition().y = terrainHeight;
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

    @Intention(reason = "code garbage storing")
    @Deprecated(forRemoval = false)
    @SuppressWarnings("write-only-objects")
    public void move(String[] args)
    {
        Vector3f position = new Vector3f();
        float walkSpeed = 0;
        float rotationalSpeed = 0;
        float yaw = 0;

        float angleInRadians = (float) Math.toRadians(yaw);
        float dx = (float) (walkSpeed * Math.sin(angleInRadians));
        float dz = (float) (walkSpeed * Math.cos(angleInRadians));

        if(Keyboard.isKeyDown(Keyboard.KEY_W))
        {
            getPosition().x += dx;
            getPosition().z -= dz;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_D))
        {
            getPosition().x += dz;
            getPosition().z += dx;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_A))
        {
            getPosition().x -= dz;
            getPosition().z -= dx;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_S))
        {
            getPosition().x -= dx;
            getPosition().z += dz;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE))
        {
            getPosition().y += walkSpeed;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
        {
            getPosition().y -= walkSpeed;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_LEFT))
        {
            yaw -= rotationalSpeed;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
        {
            yaw += rotationalSpeed;
        }
        var x = getPosition();
        var y = yaw;
    }

    private void calculateAngleFromPlayer()
    {

    }
}
