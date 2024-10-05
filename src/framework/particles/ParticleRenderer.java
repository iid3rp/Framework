package framework.particles;

import framework.entity.Camera;
import framework.lang.Mat4;
import framework.lang.Vec3;
import framework.loader.ModelLoader;
import framework.model.Model;
import framework.lang.Math;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;

import java.nio.FloatBuffer;
import java.util.List;
import java.util.Map;

public class ParticleRenderer {
	
	private static final float[] VERTICES = {-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f};
	public static final int MAX_INSTANCE = 100_000;
	public static final int INSTANCE_DATA_LENGTH = 21;
	private static final FloatBuffer buffer = BufferUtils.createFloatBuffer(MAX_INSTANCE * INSTANCE_DATA_LENGTH);
	private Model quad;
	private ParticleShader shader;
	private int vbo;
	private int pointer = 0;
	
	protected ParticleRenderer(Mat4 projectionMatrix)
	{
		vbo = ModelLoader.createEmptyVbo(INSTANCE_DATA_LENGTH * MAX_INSTANCE);
		quad = ModelLoader.loadToVaoInt(VERTICES, 2);

		// instances go here
		ModelLoader.addInstanceAttribs(quad.getVaoId(), vbo, 1, 4, INSTANCE_DATA_LENGTH, 0);
		ModelLoader.addInstanceAttribs(quad.getVaoId(), vbo, 2, 4, INSTANCE_DATA_LENGTH, 4);
		ModelLoader.addInstanceAttribs(quad.getVaoId(), vbo, 3, 4, INSTANCE_DATA_LENGTH, 8);
		ModelLoader.addInstanceAttribs(quad.getVaoId(), vbo, 4, 4, INSTANCE_DATA_LENGTH, 12);
		ModelLoader.addInstanceAttribs(quad.getVaoId(), vbo, 5, 4, INSTANCE_DATA_LENGTH, 16);
		ModelLoader.addInstanceAttribs(quad.getVaoId(), vbo, 6, 1, INSTANCE_DATA_LENGTH, 20);

		shader = new ParticleShader();
		shader.bind();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.unbind();
	}
	
	protected void render(Map<ParticleTexture, List<Particle>> particles, Camera camera)
	{
		Mat4 view = Math.createViewMatrix(camera);
		prepare();
		for(ParticleTexture t : particles.keySet())
		{
			bindTexture(t);
			List<Particle> particleList = particles.get(t);
			pointer = 0;
			float[] vboData = new float[particleList.size() * INSTANCE_DATA_LENGTH];
			for(Particle particle : particleList) {
				updateModelViewMatrix(particle.getPosition(), particle.getRotation(), particle.getScale(), view, vboData);
				updateTextCoordinateInfo(particle, vboData);
			}
			ModelLoader.updateVbo(vbo, vboData, buffer);
			GL31.glDrawArraysInstanced(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount(), particleList.size());
		}

		finishRendering();
	}

	private void bindTexture(ParticleTexture texture)
	{
		//bind
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA,
				texture.isAdditive()? GL11.GL_ONE :
						GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL13.glBindTexture(GL13.GL_TEXTURE_2D, texture.getTextureID());
		shader.loadNumberOfRows(texture.getNumOfRows());
	}

	private void updateTextCoordinateInfo(Particle particle, float[] data)
	{
		data[pointer++] = particle.getOffset()[0].x;
		data[pointer++] = particle.getOffset()[0].y;
		data[pointer++] = particle.getOffset()[1].x;
		data[pointer++] = particle.getOffset()[1].y;
		data[pointer++] = particle.getBlend();
	}

	private void storeMatrixData(Mat4 matrix, float[] data)
	{
		for(int i = 0; i < 4; i++)
			for(int j = 0; j < 4; j++)
				data[pointer++] = matrix.m[i][j];
	}

	protected void dispose(){
		shader.dispose();
	}

	private void updateModelViewMatrix(Vec3 position, float rotation, float scale, Mat4 view, float[] vboData)
	{
		Mat4 model = new Mat4();

		// Translate to the particle's position
		model.translate(position);

		model.m[0][0] = view.m[0][0];
		model.m[0][1] = view.m[1][0];
		model.m[0][2] = view.m[2][0];
		model.m[1][0] = view.m[0][1];
		model.m[1][1] = view.m[1][1];
		model.m[1][2] = view.m[2][1];
		model.m[2][0] = view.m[0][2];
		model.m[2][1] = view.m[1][2];
		model.m[2][2] = view.m[2][2];

		// Optional: For billboard-ing, you might not want to rotate the particles locally.
		// But if you do want some controlled rotation, you can apply it here:
		model.rotate((float) java.lang.Math.toRadians(rotation), new Vec3(1, 1, 1));
		model.scale(scale);
		Mat4 modelViewMatrix = Mat4.mul(view, model, null);
		storeMatrixData(modelViewMatrix, vboData);
	}
	
	private void prepare()
	{
		shader.bind();
		GL30.glBindVertexArray(quad.getVaoId());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL20.glEnableVertexAttribArray(3);
		GL20.glEnableVertexAttribArray(4);
		GL20.glEnableVertexAttribArray(5);
		GL20.glEnableVertexAttribArray(6);
		GL11.glEnable(GL11.GL_BLEND);
		//GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
	}
	
	private void finishRendering(){
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL20.glDisableVertexAttribArray(3);
		GL20.glDisableVertexAttribArray(4);
		GL20.glDisableVertexAttribArray(5);
		GL20.glDisableVertexAttribArray(6);
		GL30.glBindVertexArray(0);
		shader.unbind();
	}

}
