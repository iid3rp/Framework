package framework.water;

import framework.entity.Camera;
import framework.entity.Light;
import framework.model.Model;
import framework.display.DisplayManager;
import framework.display.MasterRenderer;
import framework.display.ModelLoader;
import framework.texture.Texture;
import framework.toolbox.GeomMath;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

public class WaterRenderer {

	private static final String DuDv_MAP = "dude";
	private static final String NORMALS_MAP = "normalWaterMap";
	private static final float WAVE_SPEED = 0.04f;
	private Model quad;
	private WaterShader shader;
	private WaterFrameBuffers buffer;
	private Texture duDvTexture;
	private Texture normalTexture;
	private float moveFactor;

	public WaterRenderer(WaterShader shader, Matrix4f projectionMatrix, WaterFrameBuffers buffer) {
		this.shader = shader;
		this.buffer = buffer;
		duDvTexture = new Texture(ModelLoader.loadTexture(DuDv_MAP));
		normalTexture = new Texture(ModelLoader.loadTexture(NORMALS_MAP));
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
		setUpVAO();
	}

	public void render(List<WaterTile> water, Camera camera, Light light) {
		prepareRender(camera, light);
		for (WaterTile tile : water) {
			MasterRenderer.disableCulling();
			Matrix4f modelMatrix = GeomMath.createTransformationMatrix(
					new Vector3f(tile.getX(), tile.getHeight(), tile.getZ()), 0, 0, 0,
					WaterTile.TILE_SIZE);
			shader.loadModelMatrix(modelMatrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCount());
			MasterRenderer.enableCulling();
		}
		unbind();
	}

	private void prepareRender(Camera camera, Light light)
	{
		shader.start();
		shader.loadViewMatrix(camera);
		moveFactor += WAVE_SPEED * DisplayManager.getFrameTimeSeconds();
		moveFactor %= 1;
		shader.loadLight(light);
		shader.loadMoveFactor(moveFactor);
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, buffer.getReflectionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, buffer.getRefractionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, duDvTexture.getID());
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, normalTexture.getID());
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, buffer.getRefractionDepthTexture());

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	private void unbind()
	{
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

	private void setUpVAO()
	{
		// Just x and z vertex positions here, y is set to 0 in v.shader
		float[] vertices = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1};
		quad = ModelLoader.loadToVAO(vertices, 2);
	}
}
