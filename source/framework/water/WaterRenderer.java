package framework.water;

import framework.Display.DisplayManager;
import framework.entity.Camera;
import framework.entity.Light;
import framework.loader.ModelLoader;
import framework.model.Model;
import framework.renderer.MasterRenderer;
import framework.textures.Texture;
import framework.utils.GeomMath;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.List;

public class WaterRenderer {

	private static final String DuDv_MAP = "dude.png";
	private static final String NORMALS_MAP = "normalWaterMap.png";
	private static final float WAVE_SPEED = 0.04f;
	private Model quad;
	private WaterShader shader;
	private WaterFrameBufferObject buffer;
	private Texture duDvTexture;
	private Texture normalTexture;
	private float moveFactor;

	public WaterRenderer(WaterShader shader, Matrix4f projectionMatrix, WaterFrameBufferObject buffer) {
		this.shader = shader;
		this.buffer = buffer;
		duDvTexture = new Texture(ModelLoader.loadTexture(DuDv_MAP));
		normalTexture = new Texture(ModelLoader.loadTexture(NORMALS_MAP));
		shader.bind();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.unbind();
		setUpVAO();
	}

	public void render(List<framework.water.WaterTile> water, Camera camera, Light light) {
		prepareRender(camera, light);
		for (framework.water.WaterTile tile : water) {
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
		shader.bind();
		shader.loadViewMatrix(camera);
		moveFactor += WAVE_SPEED * DisplayManager.getDeltaInSeconds();
		moveFactor %= 1;
		shader.loadLight(light);
		shader.loadMoveFactor(moveFactor);
		GL30.glBindVertexArray(quad.getVaoId());
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
		shader.unbind();
	}

	private void setUpVAO()
	{
		// Just x and z vertex positions here, y is set to 0 in v.shader
		float[] vertices = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1};
		quad = ModelLoader.loadToVao(vertices, 2);
	}
}
