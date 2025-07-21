package framework.shadow;


import framework.entity.Entity;
import framework.lang.Mat4;
import framework.model.Model;
import framework.model.TexturedModel;
import framework.util.Key;
import framework.util.LinkList;
import framework.util.Map;


import static org.lwjgl.opengl.GL46.*;

public class ShadowMapEntityRenderer {

	private Mat4 projectionViewMatrix;
	private ShadowShader shader;

	/**
	 * @param shader
	 *            - the simple shader program being used for the shadow render
	 *            pass.
	 * @param projectionViewMatrix
	 *            - the orthographic projection matrix multiplied by the light's
	 *            "view" matrix.
	 */
	protected ShadowMapEntityRenderer(ShadowShader shader, Mat4 projectionViewMatrix) {
		this.shader = shader;
		this.projectionViewMatrix = projectionViewMatrix;
	}

	/**
	 * Renders entieis to the shadow map. Each framework.model is first bound and then all
	 * of the entities using that framework.model are rendered to the shadow map.
	 * 
	 * @param entities
	 *            - the entities to be rendered to the shadow map.
	 */
	protected void render(Map<TexturedModel, LinkList<Entity>> entities) {
		for (Key<TexturedModel, LinkList<Entity>> model : entities) {
			Model rawModel = model.getKey().getModel();
			bindModel(rawModel);
			for (Entity entity : model.getValue()) {
				prepareInstance(entity);
				glDrawElements(GL_TRIANGLES, rawModel.vertexCount(),
						GL_UNSIGNED_INT, 0);
			}
		}
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
	}

	/**
	 * Binds a raw framework.model before rendering. Only the attribute 0 is enabled here
	 * because that is where the positions are stored in the VAO, and only the
	 * positions are required in the vertex shader.
	 * 
	 * @param rawModel
	 *            - the framework.model to be bound.
	 */
	private void bindModel(Model rawModel) {
		glBindVertexArray(rawModel.vaoId());
		glEnableVertexAttribArray(0);
	}

	/**
	 * Prepares an entity to be rendered. The framework.model matrix is created in the
	 * usual way and then multiplied with the projection and view matrix (often
	 * in the past we've done this in the vertex shader) to create the
	 * mvp-matrix. This is then loaded to the vertex shader as a uniform.
	 * 
	 * @param entity
	 *            - the entity to be prepared for rendering.
	 */
	private void prepareInstance(Entity entity)
	{
		Mat4 modelMatrix = framework.lang.Math.createTransformationMatrix(entity.getPosition(),
				entity.getRotationX(), entity.getRotationY(), entity.getRotationZ(), entity.getScale());
		Mat4 mvpMatrix = Mat4.mul(projectionViewMatrix, modelMatrix, null);
		shader.loadMvpMatrix(mvpMatrix);
	}

}
