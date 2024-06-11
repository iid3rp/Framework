package entity;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera
{
    private static float REFRESH_RATE = 60;
    private static final float WALK_SPEED = .25f;
    private static final float ROTATIONAL_SPEED = 180 / REFRESH_RATE;
    private Vector3f position;
    private float pitch;
    private float yaw;
    private float roll;
    private float walkSpeed;
    private float rotationalSpeed;

    public Camera()
    {
        position = new Vector3f(0, 2f, 0);
        this.pitch = 10;
        this.yaw = 0;
        this.roll = 0;
        walkSpeed = WALK_SPEED;
        rotationalSpeed = ROTATIONAL_SPEED;
    }

    public Camera(Vector3f position, float pitch, float yaw, float roll, float speed)
    {
        this.position = position;
        this.pitch = pitch;
        this.yaw = yaw;
        this.roll = roll;
        this.walkSpeed = speed;
        this.rotationalSpeed = ROTATIONAL_SPEED;
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
        float angleInRadians = (float) Math.toRadians(yaw);
        float dx = (float) (walkSpeed * Math.sin(angleInRadians));
        float dz = (float) (walkSpeed * Math.cos(angleInRadians));

        if(Keyboard.isKeyDown(Keyboard.KEY_W))
        {
            position.x += dx;
            position.z -= dz;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_D))
        {
            position.x += dz;
            position.z += dx;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_A))
        {
            position.x -= dz;
            position.z -= dx;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_S))
        {
            position.x -= dx;
            position.z += dz;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE))
        {
            position.y += walkSpeed;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
        {
            position.y -= walkSpeed;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_LEFT))
        {
            yaw -= rotationalSpeed;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
        {
            yaw += rotationalSpeed;
        }

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

    public Vector3f getNegativePosition()
    {
        return new Vector3f(-position.x, -position.y, -position.z);
    }
}
