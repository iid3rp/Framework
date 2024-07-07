package framework.event;

import framework.entity.Camera;
import framework.environment.Environment;
import framework.terrain.Terrain;
import framework.toolbox.GeomMath;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class MouseEvent
{
    public Vector3f currentRay;
    private static int recursionCount = 200;
    private static float rayRange = 600f;
    private Matrix4f projection;
    private Matrix4f view;
    private Camera camera;
    private Terrain terrain;
    private Vector3f currentTerrainPoint;

    public MouseEvent() {}

    public MouseEvent(Camera camera, Matrix4f projection)
    {
        this.camera = camera;
        this.projection = projection;
        this.terrain = Environment.getScene().getTerrain();
        this.view = GeomMath.createViewMatrix(camera);
    }

    public void setProjectionMatrix(Matrix4f projection)
    {
        this.projection = projection;
    }

    public Vector3f getCurrentTerrainPoint() {
        return currentTerrainPoint;
    }

    public Vector3f getCurrentRay()
    {
        return currentRay;
    }

    public void update()
    {
        view = GeomMath.createViewMatrix(camera);
        currentRay = calculateMouseRay();
        currentRay = calculateMouseRay();
        if(intersectionInRange(0, rayRange, currentRay))
        {
            currentTerrainPoint = binarySearch(0, 0, rayRange, currentRay);
        }
        else
        {
            currentTerrainPoint = null;
        }
    }

    private Vector3f calculateMouseRay()
    {
        float mouseX = Mouse.getX();
        float mouseY = Mouse.getY();
        Vector2f normalizedCoordinates = getNormalizedDeviceCoordinates(mouseX,  mouseY);
        Vector4f clipCoords = new Vector4f(normalizedCoordinates.x, normalizedCoordinates.y, -1f, 1f);
        Vector4f eyeCoordinates = toEyeCoordinates(clipCoords);
        Vector3f worldRay = toWorldCoordinates(eyeCoordinates);
        return  worldRay;
    }

    public Matrix4f getProjection()
    {
        return projection;
    }

    public void setProjection(Matrix4f projection)
    {
        this.projection = projection;
    }

    public Matrix4f getView()
    {
        return view;
    }

    public void setView(Matrix4f view)
    {
        this.view = view;
    }

    public Camera getCamera()
    {
        return camera;
    }

    public void setCamera(Camera camera)
    {
        this.camera = camera;
    }

    private Vector3f toWorldCoordinates(Vector4f eyeCoordinates)
    {
        Matrix4f invertedView = Matrix4f.invert(view, null);
        Vector4f rayWorld = Matrix4f.transform(invertedView, eyeCoordinates, null);
        Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
        mouseRay.normalise();
        return mouseRay;
    }

    private Vector4f toEyeCoordinates(Vector4f clipCoordinates)
    {
        Matrix4f invertedProjection = Matrix4f.invert(projection, null);
        Vector4f eyeCoordinates = Matrix4f.transform(invertedProjection, clipCoordinates, null);
        return new Vector4f(eyeCoordinates.x, eyeCoordinates.y, -1f, 0f);
    }

    private Vector2f getNormalizedDeviceCoordinates(float mouseX, float mouseY)
    {
        float x = (2f * mouseX) / Display.getWidth() - 1;
        float y = (2f * mouseY) / Display.getHeight() - 1;
        return new Vector2f(x, y);
    }

    private Vector3f getPointOnRay(Vector3f ray, float distance) {
        Vector3f camPos = camera.getPosition();
        Vector3f start = new Vector3f(camPos.x, camPos.y, camPos.z);
        Vector3f scaledRay = new Vector3f(ray.x * distance, ray.y * distance, ray.z * distance);
        return Vector3f.add(start, scaledRay, null);
    }

    private Vector3f binarySearch(int count, float start, float finish, Vector3f ray) {
        float half = start + ((finish - start) / 2f);
        if (count >= recursionCount) {
            Vector3f endPoint = getPointOnRay(ray, half);
            Terrain terrain = getTerrain(endPoint.getX(), endPoint.getZ());
            if (terrain != null) {
                return endPoint;
            } else {
                return null;
            }
        }
        if (intersectionInRange(start, half, ray)) {
            return binarySearch(count + 1, start, half, ray);
        } else {
            return binarySearch(count + 1, half, finish, ray);
        }
    }

    private boolean intersectionInRange(float start, float finish, Vector3f ray) {
        Vector3f startPoint = getPointOnRay(ray, start);
        Vector3f endPoint = getPointOnRay(ray, finish);
        return !isUnderGround(startPoint) && isUnderGround(endPoint);
    }

    private boolean isUnderGround(Vector3f testPoint) {
        Terrain terrain = getTerrain(testPoint.getX(), testPoint.getZ());
        float height = 0;
        if (terrain != null) {
            height = terrain.getHeightOfTerrain(testPoint.getX(), testPoint.getZ());
        }
        return testPoint.y < height;
    }

    private Terrain getTerrain(float worldX, float worldZ) {
        return terrain;
    }
}
