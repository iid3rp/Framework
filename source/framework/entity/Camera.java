package framework.entity;

import framework.event.Keyboard;
import framework.event.Mouse;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {
    private static final float MAX_DISTANCE = 1500;
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

    private float distanceFromPlayer = 50;
    private float angleAroundPlayer = 0;
    private float pitch;
    private boolean playerCamera;

    public Camera(Player player)
    {
        this.player = player;
        playerCamera = true;
        setCameraDefaultPosition();
    }

    public void move()
    {
        if(!playerCamera)
        {

            float angleInRadians = (float) Math.toRadians(yaw);
            float dx = (float) (walkSpeed * Math.sin(angleInRadians));
            float dz = (float) (walkSpeed * Math.cos(angleInRadians));

            if(Keyboard.isKeyDown(GLFW_KEY_TAB)) {
                if(Keyboard.isKeyDown(GLFW_KEY_W)) {
                    position.x += dx;
                    position.z -= dz;
                }
                if(Keyboard.isKeyDown(GLFW_KEY_D)) {
                    position.x += dz;
                    position.z += dx;
                }
                if(Keyboard.isKeyDown(GLFW_KEY_A)) {
                    position.x -= dz;
                    position.z -= dx;
                }
                if(Keyboard.isKeyDown(GLFW_KEY_S)) {
                    position.x -= dx;
                    position.z += dz;
                }
            }
            if(Keyboard.isKeyDown(GLFW_KEY_SPACE)) {
                position.y += walkSpeed;
            }
            if(Keyboard.isKeyDown(GLFW_KEY_LEFT_SHIFT)) {
                position.y -= walkSpeed;
            }
            if(Keyboard.isKeyDown(GLFW_KEY_LEFT)) {
                yaw -= rotationalSpeed;
            }
            if(Keyboard.isKeyDown(GLFW_KEY_RIGHT)) {
                yaw += rotationalSpeed;
            }
        }
    }

    public void movePlayerCamera() {
        calculateZoom();
        calculatePitch();
        calculateAngleAroundPlayer();

        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();
        calculateCameraPosition(horizontalDistance, verticalDistance);
        yaw = 180 - angleAroundPlayer;

        Mouse.mouseMoved = false;
        Mouse.mouseScrolled = false;
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }

    public void setX(int x) {
        position.x = x;
    }

    public void setY(int y) {
        position.y = y;
    }

    public void setZ(int z) {
        position.z = z;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }

    public void calculateZoom()
    {
        if(Mouse.isScrolling())
        {
            float zoomLevel = (float) Mouse.getMouseScrollY() * 2;
            distanceFromPlayer =
                    distanceFromPlayer - zoomLevel > MAX_DISTANCE? MAX_DISTANCE :
                            Math.max(distanceFromPlayer - zoomLevel, MIN_DISTANCE);
        }
    }

    private void calculatePitch() {
        if ((Mouse.isButtonDown(GLFW_MOUSE_BUTTON_RIGHT) || Mouse.isButtonDown(GLFW_MOUSE_BUTTON_LEFT)) && Mouse.isMoving())
        {
            float pitchChange = (float) Mouse.getSwipeY() * 0.2f;
            pitch += pitchChange;
        }
    }

    private void calculateAngleAroundPlayer() {
        if ((Mouse.isButtonDown(GLFW_MOUSE_BUTTON_RIGHT) || Mouse.isButtonDown(GLFW_MOUSE_BUTTON_LEFT)) && Mouse.isMoving()) {
            float angleChange = (float) Mouse.getSwipeX() * 0.2f;
            angleAroundPlayer -= angleChange;
        }
    }

    private void setCameraDefaultPosition() {
        position = new Vector3f(0, 0,0);
        distanceFromPlayer = 50;
        pitch = 20;
        yaw = 0;
        roll = 0;
        angleAroundPlayer = 0;
    }

    private void checkInputs() {
        // reset camera to default position
        if (Keyboard.isKeyDown(GLFW_KEY_HOME)) {
            setCameraDefaultPosition();
        }
    }

    public Vector3f getNegativePosition()
    {
        return new Vector3f(-position.x, -position.y, -position.z);
    }

    public void invertPitch()
    {
        this.pitch = -pitch;
    }

    private float calculateHorizontalDistance() {
        return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
    }

    private float calculateVerticalDistance() {
        return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch))) + 5;
    }

    private void calculateCameraPosition(float horizontalDistanceFromPlayer, float verticalDistanceFromPlayer) {
        float theta = angleAroundPlayer;
        float offsetXOfCameraFromPlayer = (float) (horizontalDistanceFromPlayer * Math.sin(Math.toRadians(theta)));
        float offsetZOfCameraFromPlayer = (float) (horizontalDistanceFromPlayer * Math.cos(Math.toRadians(theta)));
        position.x = player.getPosition().x - offsetXOfCameraFromPlayer;
        position.z = player.getPosition().z - offsetZOfCameraFromPlayer;
        position.y = player.getPosition().y + verticalDistanceFromPlayer;
    }
}
