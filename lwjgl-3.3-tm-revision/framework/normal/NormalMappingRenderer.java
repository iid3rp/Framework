package framework.normal;

import framework.entity.Camera;
import framework.entity.Entity;
import framework.entity.Light;
import framework.model.Model;
import framework.model.TexturedModel;
import framework.renderer.MasterRenderer;
import framework.textures.Texture;
import framework.util.JOMLMath;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.List;
import java.util.Map;

public class NormalMappingRenderer
{

	private NormalMappingShader shader;

	public NormalMappingRenderer(Matrix4f projectionMatrix) {
		this.shader = new NormalMappingShader();
		shader.bind();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.connectTextureUnits();
		shader.unbind();
	}

	public void render(Map<TexturedModel, List<Entity>> entities, Vector4f clipPlane, List<Light> lights, Camera camera) {
		shader.bind();
		prepare(clipPlane, lights, camera);
		for (TexturedModel model : entities.keySet()) {
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			for (Entity entity : batch) {
				prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
		shader.unbind();
	}
	
	public void dispose(){
		shader.dispose();
	}

	private void prepareTexturedModel(TexturedModel model) {
		Model rawModel = model.getModel();
		GL30.glBindVertexArray(rawModel.getVaoId());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		Texture texture = model.getTexture();
		shader.loadNumberOfRows(texture.getNumberOfRows());
		if (texture.hasTransparency()) {
			MasterRenderer.disableCulling();
		}
		shader.loadShineVariables(texture.getShineDampening(), texture.getReflectivity());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
	}

	private void unbindTexturedModel() {
		MasterRenderer.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}

	private void prepareInstance(Entity entity) {
		Matrix4f transformationMatrix = JOMLMath.createTransformationMatrix(entity.getPosition(), entity.getRotationX(),
				entity.getRotationY(), entity.getRotationZ(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
		shader.loadOffset(entity.getTextureXOffset(), entity.getTextureYOffset());
	}

	private void prepare(Vector4f clipPlane, List<Light> lights, Camera camera) {
		shader.loadClipPlane(clipPlane);
		//need to be public variables in MasterRenderer
		shader.loadSkyColour(MasterRenderer.SKY_RED, MasterRenderer.SKY_GREEN, MasterRenderer.SKY_BLUE);
		Matrix4f viewMatrix = JOMLMath.createViewMatrix(camera);
		
		shader.loadLights(lights, viewMatrix);
		shader.loadViewMatrix(viewMatrix);
	}

}
