package framework.shadow;

import framework.entity.Camera;
import framework.hardware.Display;
import framework.lang.Mat4;
import framework.lang.Vec3;
import framework.lang.Vec4;
import framework.renderer.MasterRenderer;

/**
 * Represents the 3D cuboidal area of the world in which objects will cast
 * shadows (basically represents the orthographic projection area for the shadow
 * render pass). It is updated each frame to optimise the area, making it as
 * small as possible (to allow for optimal shadow map resolution) while not
 * being too small to avoid objects not having shadows when they should.
 * Everything inside the cuboidal area represented by this object will be
 * rendered to the shadow map in the shadow render pass. Everything outside the
 * area won't be.
 * 
 * @author Karl
 *
 */
public class ShadowBox {

	private static final float OFFSET = 0;
	private static final Vec4 UP = new Vec4(0, 1, 0, 0);
	private static final Vec4 FORWARD = new Vec4(0, 0, -1, 0);
	public static float SHADOW_DISTANCE = 2000;

	private float minX, maxX;
	private float minY, maxY;
	private float minZ, maxZ;
	private Mat4 lightViewMatrix;
	private Camera cam;

	private float farHeight, farWidth, nearHeight, nearWidth;

	/**
	 * Creates a new shadow box and calculates some initial values relating to
	 * the camera's view frustum, namely the width and height of the near plane
	 * and (possibly adjusted) far plane.
	 * 
	 * @param lightViewMatrix
	 *            - basically the "view matrix" of the light. Can be used to
	 *            transform a point from world space into "light" space (i.e.
	 *            changes a point's coordinates from being in relation to the
	 *            world's axis to being in terms of the light's local axis).
	 * @param camera
	 *            - the in-game camera.
	 */
	protected ShadowBox(Mat4 lightViewMatrix, Camera camera) {
		this.lightViewMatrix = lightViewMatrix;
		this.cam = camera;
		calculateWidthsAndHeights();
	}

	/**
	 * Updates the bounds of the shadow box based on the light direction and the
	 * camera's view frustum, to make sure that the box covers the smallest area
	 * possible while still ensuring that everything inside the camera's view
	 * (within a certain range) will cast shadows.
	 */
	protected void update() {
		Mat4 rotation = calculateCameraRotationMatrix();
		Vec3 forwardVector = new Vec3(Mat4.transform(rotation, FORWARD, null));

		Vec3 toFar = new Vec3(forwardVector);
		toFar.scale(SHADOW_DISTANCE);
		Vec3 toNear = new Vec3(forwardVector);
		toNear.scale(MasterRenderer.NEAR_PLANE);
		Vec3 centerNear = Vec3.add(toNear, cam.getPosition(), null);
		Vec3 centerFar = Vec3.add(toFar, cam.getPosition(), null);

		Vec4[] points = calculateFrustumVertices(rotation, forwardVector, centerNear,
				centerFar);

		boolean first = true;
		for (Vec4 point : points) {
			if (first) {
				minX = point.x;
				maxX = point.x;
				minY = point.y;
				maxY = point.y;
				minZ = point.z;
				maxZ = point.z;
				first = false;
				continue;
			}
			if (point.x > maxX) {
				maxX = point.x;
			} else if (point.x < minX) {
				minX = point.x;
			}
			if (point.y > maxY) {
				maxY = point.y;
			} else if (point.y < minY) {
				minY = point.y;
			}
			if (point.z > maxZ) {
				maxZ = point.z;
			} else if (point.z < minZ) {
				minZ = point.z;
			}
		}
		maxZ += OFFSET;

	}

	/**
	 * Calculates the center of the "view cuboid" in light space first, and then
	 * converts this to world space using the inverse light's view matrix.
	 * 
	 * @return The center of the "view cuboid" in world space.
	 */
	protected Vec3 getCenter() {
		float x = (minX + maxX) / 2f;
		float y = (minY + maxY) / 2f;
		float z = (minZ + maxZ) / 2f;
		Vec4 cen = new Vec4(x, y, z, 1);
		Mat4 invertedLight = new Mat4();
		Mat4.invert(lightViewMatrix, invertedLight);
		return new Vec3(Mat4.transform(invertedLight, cen, null));
	}

	/**
	 * @return The width of the "view cuboid" (orthographic projection area).
	 */
	protected float getWidth() {
		return maxX - minX;
	}

	/**
	 * @return The height of the "view cuboid" (orthographic projection area).
	 */
	protected float getHeight() {
		return maxY - minY;
	}

	/**
	 * @return The length of the "view cuboid" (orthographic projection area).
	 */
	protected float getLength() {
		return maxZ - minZ;
	}

	/**
	 * Calculates the position of the vertex at each corner of the view frustum
	 * in light space (8 vertices in total, so this returns 8 positions).
	 * 
	 * @param rotation
	 *            - camera's rotation.
	 * @param forwardVector
	 *            - the direction that the camera is aiming, and thus the
	 *            direction of the frustum.
	 * @param centerNear
	 *            - the center point of the frustum's near plane.
	 * @param centerFar
	 *            - the center point of the frustum's (possibly adjusted) far
	 *            plane.
	 * @return The positions of the vertices of the frustum in light space.
	 */
	private Vec4[] calculateFrustumVertices(Mat4 rotation, Vec3 forwardVector,
			Vec3 centerNear, Vec3 centerFar) {
		Vec3 upVector = new Vec3(Mat4.transform(rotation, UP, null));
		Vec3 rightVector = Vec3.cross(forwardVector, upVector, null);
		Vec3 downVector = new Vec3(-upVector.x, -upVector.y, -upVector.z);
		Vec3 leftVector = new Vec3(-rightVector.x, -rightVector.y, -rightVector.z);
		Vec3 farTop = Vec3.add(centerFar, new Vec3(upVector.x * farHeight,
				upVector.y * farHeight, upVector.z * farHeight), null);
		Vec3 farBottom = Vec3.add(centerFar, new Vec3(downVector.x * farHeight,
				downVector.y * farHeight, downVector.z * farHeight), null);
		Vec3 nearTop = Vec3.add(centerNear, new Vec3(upVector.x * nearHeight,
				upVector.y * nearHeight, upVector.z * nearHeight), null);
		Vec3 nearBottom = Vec3.add(centerNear, new Vec3(downVector.x * nearHeight,
				downVector.y * nearHeight, downVector.z * nearHeight), null);
		Vec4[] points = new Vec4[8];
		points[0] = calculateLightSpaceFrustumCorner(farTop, rightVector, farWidth);
		points[1] = calculateLightSpaceFrustumCorner(farTop, leftVector, farWidth);
		points[2] = calculateLightSpaceFrustumCorner(farBottom, rightVector, farWidth);
		points[3] = calculateLightSpaceFrustumCorner(farBottom, leftVector, farWidth);
		points[4] = calculateLightSpaceFrustumCorner(nearTop, rightVector, nearWidth);
		points[5] = calculateLightSpaceFrustumCorner(nearTop, leftVector, nearWidth);
		points[6] = calculateLightSpaceFrustumCorner(nearBottom, rightVector, nearWidth);
		points[7] = calculateLightSpaceFrustumCorner(nearBottom, leftVector, nearWidth);
		return points;
	}

	/**
	 * Calculates one of the corner vertices of the view frustum in world space
	 * and converts it to light space.
	 * 
	 * @param startPoint
	 *            - the starting center point on the view frustum.
	 * @param direction
	 *            - the direction of the corner from the start point.
	 * @param width
	 *            - the distance of the corner from the start point.
	 * @return - The relevant corner vertex of the view frustum in light space.
	 */
	private Vec4 calculateLightSpaceFrustumCorner(Vec3 startPoint, Vec3 direction,
			float width) {
		Vec3 point = Vec3.add(startPoint,
				new Vec3(direction.x * width, direction.y * width, direction.z * width), null);
		Vec4 point4f = new Vec4(point.x, point.y, point.z, 1f);
		Mat4.transform(lightViewMatrix, point4f, point4f);
		return point4f;
	}

	/**
	 * @return The rotation of the camera represented as a matrix.
	 */
	private Mat4 calculateCameraRotationMatrix() {
		Mat4 rotation = new Mat4();
		rotation.rotate((float) Math.toRadians(-cam.getYaw()), Vec3.yAxis);
		rotation.rotate((float) Math.toRadians(-cam.getPitch()), Vec3.xAxis);
		return rotation;
	}

	/**
	 * Calculates the width and height of the near and far planes of the
	 * camera's view frustum. However, this doesn't have to use the "actual" far
	 * plane of the view frustum. It can use a shortened view frustum if desired
	 * by bringing the far-plane closer, which would increase shadow resolution
	 * but means that distant objects wouldn't cast shadows.
	 */
	private void calculateWidthsAndHeights() {
		farWidth = (float) (SHADOW_DISTANCE * Math.tan(Math.toRadians(MasterRenderer.FOV)));
		nearWidth = (float) (MasterRenderer.NEAR_PLANE
				* Math.tan(Math.toRadians(MasterRenderer.FOV)));
		farHeight = farWidth / getAspectRatio();
		nearHeight = nearWidth / getAspectRatio();
	}

	/**
	 * @return The aspect ratio of the display (width:height ratio).
	 */
	private float getAspectRatio() {
		return (float) Display.getWidth() / (float) Display.getHeight();
	}

}
