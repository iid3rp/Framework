package framework.entity;

import framework.environment.Engine;
import framework.hardware.Keyboard;
import framework.lang.GeomMath;
import framework.lang.Mat4;

public class Camera
{
    private float posX, posY, posZ;
    private float rotX, rotY, rotZ; // x = pitch, y = yaw, z = roll
    private Mat4 viewMatrix;
    private static final float MAX_DISTANCE = 500;
    private static final float MIN_DISTANCE = 0;
    private static float REFRESH_RATE = 120;
    private static final float WALK_SPEED = .005f;
    private static final float ROTATIONAL_SPEED = 180 / REFRESH_RATE;
    private float speed;
    private float rotationalSpeed;
    private boolean isMoving;
    //    private SmoothFloat angleAroundPlayer = new SmoothFloat(0, .1f);
//    private SmoothFloat distanceFromPlayer = new SmoothFloat(30, 1f);
//    private SmoothFloat pitch = new SmoothFloat(30, .1f);
    private boolean playerCamera;

    public Camera()
    {
        viewMatrix = new Mat4();
        updateViewMatrix();
    }

    public void setPosition(float x, float y, float z)
    {
        posX = x;
        posY = y;
        posZ = z;
    }

    public void setPitch(float angle)
    {
        rotX = angle;
    }

    public void setYaw(float angle)
    {
        rotY = angle;
    }

    public void setRoll(float angle)
    {
        rotZ = angle;
    }

//    public Camera(Player player)
//    {
//        this.player = player;
//        playerCamera = true;
//        setCameraDefaultPosition();
//    }
    public void move()
    {
        if(Keyboard.areKeysDown(Keyboard.W, Keyboard.A, Keyboard.S, Keyboard.D))
            isMoving = true;
        if(Keyboard.isKeyDown(Keyboard.W))
            posZ -= WALK_SPEED;
        if(Keyboard.isKeyDown(Keyboard.A))
            posX -= WALK_SPEED;
        if(Keyboard.isKeyDown(Keyboard.S))
            posZ += WALK_SPEED;
        if(Keyboard.isKeyDown(Keyboard.D))
            posX += WALK_SPEED;

        if(isMoving) {
            updateViewMatrix();
            isMoving = false;
        }
    }

    public void updateViewMatrix()
    {
        Engine.mainExecute(() -> viewMatrix = GeomMath.createViewMatrix(posX,posY, posZ, rotX, rotY, rotZ,viewMatrix,viewMatrix));

    }

//    public void move()
//    {
//        if(!playerCamera) {
//
//            float angleInRadians = (float) Math.toRadians(rotY);
//            float dx = (float) (speed * Math.sin(angleInRadians));
//            float dz = (float) (speed * Math.cos(angleInRadians));
//
//            if(Keyboard.isKeyDown(GLFW_KEY_TAB)) {
//                if(Keyboard.isKeyDown(GLFW_KEY_W)) {
//                    posX += dx;
//                    posZ -= dz;
//                }
//                if(Keyboard.isKeyDown(GLFW_KEY_D)) {
//                    posX += dz;
//                    posZ += dx;
//                }
//                if(Keyboard.isKeyDown(GLFW_KEY_A)) {
//                    posX -= dz;
//                    posZ -= dx;
//                }
//                if(Keyboard.isKeyDown(GLFW_KEY_S)) {
//                    posX -= dx;
//                    posZ += dz;
//                }
//            }
//            if(Keyboard.isKeyDown(GLFW_KEY_SPACE)) {
//                posY += speed;
//            }
//            if(Keyboard.isKeyDown(GLFW_KEY_LEFT_SHIFT)) {
//                posY -= speed;
//            }
//            if(Keyboard.isKeyDown(GLFW_KEY_LEFT)) {
//                rotY -= rotationalSpeed;
//            }
//            if(Keyboard.isKeyDown(GLFW_KEY_RIGHT)) {
//                rotY += rotationalSpeed;
//            }
//        }
//    }

//    public void movePlayerCamera() {
//        calculateZoom();
//        calculatePitch();
//        calculateAngleAroundPlayer();
//
//        float horizontalDistance = calculateHorizontalDistance();
//        float verticalDistance = calculateVerticalDistance();
//        calculateCameraPosition(horizontalDistance, verticalDistance);
//        yaw = (180 - angleAroundPlayer.get()) % 360;
//        pitch.setTarget((360 + pitch.get()) % 360);
//    }


    public void setX(int x)
    {
        posX = x;
    }

    public void setY(int y)
    {
        posY = y;
    }

    public void setZ(int z)
    {
        posZ = z;
    }

    public Mat4 getViewMatrix()
    {
        return viewMatrix;
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

//    private void calculateZoom()
//    {
//        if(Mouse.isScrolling())
//        {
//            float zoomLevel = (float) (Mouse.getMouseScrollY() * 6f * (1 + Display.getDeltaInSeconds()));
//            distanceFromPlayer.increaseTarget(-zoomLevel);
//            distanceFromPlayer.setTarget(
//                    distanceFromPlayer.get() - zoomLevel > MAX_DISTANCE? MAX_DISTANCE:
//                            Math.max(distanceFromPlayer.get() - zoomLevel, MIN_DISTANCE)
//            );
//            distanceFromPlayer.update();
//        }
//    }
//
//    private void calculatePitch() {
//        if ((Mouse.isButtonDown(GLFW_MOUSE_BUTTON_RIGHT) || Mouse.isButtonDown(GLFW_MOUSE_BUTTON_LEFT)) && Mouse.isMoving())
//        {
//            float pitchChange = (float) Mouse.getSwipeY() * .2f * (1 + Display.getDeltaInSeconds());
//            pitch.increaseTarget(pitchChange);
//        }
//        pitch.setDelta(1);
//        pitch.update();
//
//    }

//    private void calculateAngleAroundPlayer() {
//        if ((Mouse.isButtonDown(GLFW_MOUSE_BUTTON_RIGHT) || Mouse.isButtonDown(GLFW_MOUSE_BUTTON_LEFT)) && Mouse.isMoving()) {
//            float angleChange = (float) Mouse.getSwipeX() * 0.2f;
//            angleAroundPlayer -= angleChange;
//        }
//    }

//    private void calculateAngleAroundPlayer(){
//        if ((Mouse.isButtonDown(GLFW_MOUSE_BUTTON_RIGHT) || Mouse.isButtonDown(GLFW_MOUSE_BUTTON_LEFT)) && Mouse.isMoving()) {
//            float angleChange = (float) Mouse.getSwipeX() * .2f * (1 + Display.getDeltaInSeconds());
//            angleAroundPlayer.increaseTarget(-angleChange);
//        }else if(Keyboard.isKeyDown(Keyboard.R)){
//            angleAroundPlayer.increaseTarget(0.5f);
//        }
//        angleAroundPlayer.update();
//    }

//    private void setCameraDefaultPosition() {
//        posX = posY = posZ = 0;
//        distanceFromPlayer.setTarget(50);
//        rotX = 20f;
//        rotY = 180f;
//        rotZ = 0f;
//        angleAroundPlayer.setTarget(0);
//    }

//    private void checkInputs() {
//        // reset camera to default position
//        if (Keyboard.isKeyDown(GLFW_KEY_HOME)) {
//            setCameraDefaultPosition();
//        }
//    }
//
//    public Vec3 getNegativePosition()
//    {
//        return new Vec3(posX, -posY, -posZ);
//    }
//
//    public void invertPitch()
//    {
//        this.pitch.setActual(-pitch.get());
//    }
//
//    private float calculateHorizontalDistance() {
//        return (float) (distanceFromPlayer.get() * Math.cos(Math.toRadians(pitch.get())));
//    }
//
//    private float calculateVerticalDistance() {
//        return (float) (distanceFromPlayer.get() * Math.sin(Math.toRadians(pitch.get()))) + 5;
//    }

//        private void calculateCameraPosition(float horizontalDistanceFromPlayer, float verticalDistanceFromPlayer) {
//            float theta = angleAroundPlayer.get();
//            float offsetXOfCameraFromPlayer = (float) (horizontalDistanceFromPlayer * Math.sin(Math.toRadians(theta)));
//            float offsetZOfCameraFromPlayer = (float) (horizontalDistanceFromPlayer * Math.cos(Math.toRadians(theta)));
//            posX = player.getPosition().x - offsetXOfCameraFromPlayer;
//            posZ = player.getPosition().z - offsetZOfCameraFromPlayer;
//            position.y = player.getPosition().y + verticalDistanceFromPlayer;
//        }

//    public float getDistance()
//    {
//        return distanceFromPlayer.get();
//    }
}

