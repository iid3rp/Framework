package entity;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera
{
    private static final float SPEED = .1f;
    private Vector3f position;
    private float pitch;
    private float yaw;
    private float roll;
    private float speed;

    public Camera()
    {
        position = new Vector3f(0, 0, 0);
        speed = SPEED;
    }

    public Camera(Vector3f position, float pitch, float yaw, float roll, float speed)
    {
        this.position = position;
        this.pitch = pitch;
        this.yaw = yaw;
        this.roll = roll;
        this.speed = speed;
    }

    public Camera(Vector3f position, float pitch, float yaw, float roll)
    {
        this.position = position;
        this.pitch = pitch;
        this.yaw = yaw;
        this.roll = roll;
        speed = SPEED;
    }

    public void move()
    {
        if(Keyboard.isKeyDown(Keyboard.KEY_W))
        {
           position.z -= speed;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_D))
        {
            position.x += speed;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_A))
        {
            position.x -= speed;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_S))
        {
            position.z += speed;
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
