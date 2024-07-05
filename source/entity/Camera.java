package entity;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera
{
    private static final float MAX_DISTANCE = 150;
    private static final float MIN_DISTANCE = 0;
    private static float REFRESH_RATE = 60;
    private static final float WALK_SPEED = .5f;
    private static final float ROTATIONAL_SPEED = 180 / REFRESH_RATE;
    private Vector3f position = new Vector3f(0, 0, 0);
    private float yaw;
    private float roll;
    private float walkSpeed;
    private float rotationalSpeed;
    private Player player;

    private float distanceFromThePlayer = 50;
    private float angleFromPlayer = 0;
    private float pitch;
    private boolean playerCamera;

    public Camera()
    {
        position = new Vector3f(-1, 50f, -1);
        this.pitch = 25;
        this.yaw = 0;
        this.roll = 0;
        walkSpeed = WALK_SPEED;
        rotationalSpeed = ROTATIONAL_SPEED;
        playerCamera = false;
    }

    public Camera(Player player)
    {
        this.player = player;
        pitch = 25;
        playerCamera = true;
    }

    public Camera(Vector3f position, float pitch, float yaw, float roll, float speed)
    {
        this.position = position;
        this.pitch = pitch;
        this.yaw = yaw;
        this.roll = roll;
        this.walkSpeed = speed;
        this.rotationalSpeed = ROTATIONAL_SPEED;
        playerCamera = false;
    }

    public Camera(Vector3f position, float pitch, float yaw, float roll)
    {
        this.position = position;
        this.pitch = pitch;
        this.yaw = yaw;
        this.roll = roll;
        walkSpeed = WALK_SPEED;
        rotationalSpeed = WALK_SPEED;
    }

    public Camera(Vector3f position, float pitch, float yaw, float roll, float walkSpeed, float rotationalSpeed)
    {
        this.position = position;
        this.pitch = pitch;
        this.yaw = yaw;
        this.roll = roll;
        this.walkSpeed = walkSpeed;
        this.rotationalSpeed = rotationalSpeed;
    }

    public void move()
    {
        if(playerCamera) {
            return;
        }
        float angleInRadians = (float) Math.toRadians(yaw);
        float dx = (float) (walkSpeed * Math.sin(angleInRadians));
        float dz = (float) (walkSpeed * Math.cos(angleInRadians));

        if(Keyboard.isKeyDown(Keyboard.KEY_TAB)) {
            if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
                position.x += dx;
                position.z -= dz;
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
                position.x += dz;
                position.z += dx;
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
                position.x -= dz;
                position.z -= dx;
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
                position.x -= dx;
                position.z += dz;
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
                position.y += walkSpeed;
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                position.y -= walkSpeed;
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
                yaw -= rotationalSpeed;
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
                yaw += rotationalSpeed;
            }
        }
    }

    public void movePlayerCamera()
    {
        calculateZoom();
        calculatePitch();
        calculateAngleFromPlayer();
        float horizontal = calculateHorizontalDistance();
        float vertical = calculateVerticalDistance();
        calculateCameraPosition(horizontal, vertical);
        this.yaw = 180 - angleFromPlayer;
    }

    public Vector3f getPositivePosition()
    {
        return position;
    }

    public void setPosition(Vector3f position)
    {
        this.position = position;
    }

    public float getPitch()
    {
        return pitch;
    }

    public void setPitch(float pitch)
    {
        this.pitch = pitch;
    }

    public float getYaw()
    {
        return yaw;
    }

    public void setYaw(float yaw)
    {
        this.yaw = yaw;
    }

    public float getRoll()
    {
        return roll;
    }

    public void setRoll(float roll)
    {
        this.roll = roll;
    }

    private void calculateCameraPosition(float horizon, float vertical)
    {
        float theta = angleFromPlayer;
        float offsetX = (float) (horizon * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float) (horizon * Math.cos(Math.toRadians(theta)));

        position.x = player.getPosition().x - offsetX;
        position.z = player.getPosition().z - offsetZ;

        position.y = player.getPosition().y + vertical;
    }


    private float calculateHorizontalDistance()
    {
        return (float) (distanceFromThePlayer * Math.cos(Math.toRadians(pitch)));
    }

    private float calculateVerticalDistance()
    {
        return (float) (distanceFromThePlayer * Math.sin(Math.toRadians(pitch)));
    }

    public Vector3f getNegativePosition()
    {
        return new Vector3f(-position.x, -position.y, -position.z);
    }

    public void setY(float f)
    {
        position.y = f;
    }

    public void calculateZoom()
    {
        float zoomLevel = (float) Mouse.getDWheel() * 0.1f;
        distanceFromThePlayer =
        distanceFromThePlayer - zoomLevel > MAX_DISTANCE? MAX_DISTANCE :
                Math.max(distanceFromThePlayer - zoomLevel, MIN_DISTANCE);
    }

    private void calculatePitch()
    {
        if(Mouse.isButtonDown(0))
        {
            float pitchChange = (float) Mouse.getDY() * 0.1f;
            pitch -= pitchChange;
        }
    }

    private void calculateAngleFromPlayer()
    {
        if(Mouse.isButtonDown((0)))
        {
            float angleChange = (float) Mouse.getDX() * 0.3f;
            angleFromPlayer -= angleChange;
        }
    }

    public Vector3f getPosition()
    {
        return position;
    }

    public void invertPitch()
    {
        this.pitch = -pitch;
    }
}
