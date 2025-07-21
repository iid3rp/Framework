package framework.entity;

import framework.hardware.Display;
import framework.hardware.Keyboard;
import framework.hardware.Mouse;
import framework.lang.SmoothFloat;
import framework.lang.Vec3;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {
    private static final float MAX_DISTANCE = 500;
    private static final float MIN_DISTANCE = 0;
    private static float REFRESH_RATE = 120;
    private static final float WALK_SPEED = .5f;
    private static final float ROTATIONAL_SPEED = 180 / REFRESH_RATE;
    private Vec3 position = new Vec3();
    private float yaw;
    private float roll;
    private float walkSpeed;
    private float rotationalSpeed;
    private Player player;

    private SmoothFloat angleAroundPlayer = new SmoothFloat(0, .1f);
    private SmoothFloat distanceFromPlayer = new SmoothFloat(30, 1f);
    private SmoothFloat pitch = new SmoothFloat(30, .1f);
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
        yaw = (180 - angleAroundPlayer.get()) % 360;
        pitch.setTarget((360 + pitch.get()) % 360);
    }

    public Vec3 getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch.get();
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
        this.pitch.setTarget(pitch);
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }

//    public void calculateZoom()
//    {
//        if(Mouse.isScrolling())
//        {
//            float zoomLevel = (float) Mouse.getMouseScrollY() * 2;
//            distanceFromPlayer =
//                    distanceFromPlayer - zoomLevel > MAX_DISTANCE? MAX_DISTANCE:
//                            Math.max(distanceFromPlayer - zoomLevel, MIN_DISTANCE);
//        }
//    }

    private void calculateZoom()
    {
        if(Mouse.isScrolling())
        {
            float zoomLevel = (float) (Mouse.getMouseScrollY() * 6f * (1 + Display.getDeltaInSeconds()));
            distanceFromPlayer.increaseTarget(-zoomLevel);
            distanceFromPlayer.setTarget(
                    distanceFromPlayer.get() - zoomLevel > MAX_DISTANCE? MAX_DISTANCE:
                            Math.max(distanceFromPlayer.get() - zoomLevel, MIN_DISTANCE)
            );
            distanceFromPlayer.update();
        }
    }

    private void calculatePitch() {
        if ((Mouse.isButtonDown(GLFW_MOUSE_BUTTON_RIGHT) || Mouse.isButtonDown(GLFW_MOUSE_BUTTON_LEFT)) && Mouse.isMoving())
        {
            float pitchChange = (float) Mouse.getSwipeY() * .2f * (1 + Display.getDeltaInSeconds());
            pitch.increaseTarget(pitchChange);
        }
        pitch.setDelta(1);
        pitch.update();

    }

//    private void calculateAngleAroundPlayer() {
//        if ((Mouse.isButtonDown(GLFW_MOUSE_BUTTON_RIGHT) || Mouse.isButtonDown(GLFW_MOUSE_BUTTON_LEFT)) && Mouse.isMoving()) {
//            float angleChange = (float) Mouse.getSwipeX() * 0.2f;
//            angleAroundPlayer -= angleChange;
//        }
//    }

    private void calculateAngleAroundPlayer(){
        if ((Mouse.isButtonDown(GLFW_MOUSE_BUTTON_RIGHT) || Mouse.isButtonDown(GLFW_MOUSE_BUTTON_LEFT)) && Mouse.isMoving()) {
            float angleChange = (float) Mouse.getSwipeX() * .2f * (1 + Display.getDeltaInSeconds());
            angleAroundPlayer.increaseTarget(-angleChange);
        }else if(Keyboard.isKeyDown(Keyboard.R)){
            angleAroundPlayer.increaseTarget(0.5f);
        }
        angleAroundPlayer.update();
    }

    private void setCameraDefaultPosition() {
        position = new Vec3();
        distanceFromPlayer.setTarget(50);
        pitch.setTarget(20);
        yaw = 180;
        roll = 0;
        angleAroundPlayer.setTarget(0);
    }

    private void checkInputs() {
        // reset camera to default position
        if (Keyboard.isKeyDown(GLFW_KEY_HOME)) {
            setCameraDefaultPosition();
        }
    }

    public Vec3 getNegativePosition()
    {
        return new Vec3(position.x, -position.y, -position.z);
    }

    public void invertPitch()
    {
        this.pitch.setActual(-pitch.get());
    }

    private float calculateHorizontalDistance() {
        return (float) (distanceFromPlayer.get() * Math.cos(Math.toRadians(pitch.get())));
    }

    private float calculateVerticalDistance() {
        return (float) (distanceFromPlayer.get() * Math.sin(Math.toRadians(pitch.get()))) + 5;
    }

    private void calculateCameraPosition(float horizontalDistanceFromPlayer, float verticalDistanceFromPlayer) {
        float theta = angleAroundPlayer.get();
        float offsetXOfCameraFromPlayer = (float) (horizontalDistanceFromPlayer * Math.sin(Math.toRadians(theta)));
        float offsetZOfCameraFromPlayer = (float) (horizontalDistanceFromPlayer * Math.cos(Math.toRadians(theta)));
        position.x = player.getPosition().x - offsetXOfCameraFromPlayer;
        position.z = player.getPosition().z - offsetZOfCameraFromPlayer;
        position.y = player.getPosition().y + verticalDistanceFromPlayer;
    }

    public float getDistance()
    {
        return distanceFromPlayer.get();
    }

    public Vec3 getPositivePosition()
    {
        return position;
    }
}
