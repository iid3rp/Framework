package framework.particles;

import java.nio.FloatBuffer;
import java.util.List;
import java.util.Map;

import framework.entity.Camera;
import framework.loader.ModelLoader;
import framework.model.Model;
import framework.utils.GeomMath;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;

public class ParticleRenderer {
	
	private static final float[] VERTICES = {-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f};
	public static final int MAX_INSTANCE = 10_000;
	public static final int INSTANCE_DATA_LENGTH = 21;
	private Model quad;
	private ParticleShader shader;
	private static final FloatBuffer buffer = BufferUtils.createFloatBuffer(MAX_INSTANCE * INSTANCE_DATA_LENGTH);
	private int vbo;
	private int pointer = 0;
	
	protected ParticleRenderer(Matrix4f projectionMatrix) {
		vbo = ModelLoader.createEmptyVbo(INSTANCE_DATA_LENGTH * MAX_INSTANCE);
		quad = ModelLoader.loadToVao(VERTICES, 2);

		// instances go here
		ModelLoader.addInstanceAttribs(quad.getVaoId(), vbo, 1, 4, INSTANCE_DATA_LENGTH, 0);
		ModelLoader.addInstanceAttribs(quad.getVaoId(), vbo, 2, 4, INSTANCE_DATA_LENGTH, 4);
		ModelLoader.addInstanceAttribs(quad.getVaoId(), vbo, 3, 4, INSTANCE_DATA_LENGTH, 8);
		ModelLoader.addInstanceAttribs(quad.getVaoId(), vbo, 4, 4, INSTANCE_DATA_LENGTH, 12);
		ModelLoader.addInstanceAttribs(quad.getVaoId(), vbo, 5, 4, INSTANCE_DATA_LENGTH, 16);
		ModelLoader.addInstanceAttribs(quad.getVaoId(), vbo, 6, 4, INSTANCE_DATA_LENGTH, 20);

		shader = new ParticleShader();
		shader.bind();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.unbind();
	}
	
	protected void render(Map<ParticleTexture, List<Particle>> particles, Camera camera){
		Matrix4f view = GeomMath.createViewMatrix(camera);
		prepare();
		for(ParticleTexture t : particles.keySet())
		{
			bindTexture(t);
			List<Particle> particleList = particles.get(t);
			System.out.println(particleList.size());
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

	private void updateTextCoordinateInfo(Particle particle, float[] data)
	{
		data[pointer++] = particle.getTextureOffset1().x;
		data[pointer++] = particle.getTextureOffset1().y;
		data[pointer++] = particle.getTextureOffset2().x;
		data[pointer++] = particle.getTextureOffset2().y;
		data[pointer++] = particle.getBlendFactor();
	}

	private void bindTexture(ParticleTexture texture)
	{
		// binding
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
		shader.loadNumberOfRows(texture.getNumOfRows());
	}

	private void updateModelViewMatrix(Vector3f position, float rotation, float scale, Matrix4f view, float[] data)
	{
		Matrix4f model = new Matrix4f();
		model.translate(position);
		model.m00(view.m00());
		model.m01(view.m10());
		model.m02(view.m20());
		model.m10(view.m01());
		model.m11(view.m11());
		model.m12(view.m21());
		model.m20(view.m02());
		model.m21(view.m12());
		model.m22(view.m22());
		model.rotate((float) Math.toRadians(rotation), 0, 0, 1);
		model.scale(scale);

		Matrix4f modelView = new Matrix4f(view).mul(model);
		storeMatrixData(modelView, data);
	}

	private void storeMatrixData(Matrix4f matrix, float[] data)
	{
		data[pointer++] = matrix.m00();
		data[pointer++] = matrix.m01();
		data[pointer++] = matrix.m02();
		data[pointer++] = matrix.m03();
		data[pointer++] = matrix.m10();
		data[pointer++] = matrix.m11();
		data[pointer++] = matrix.m12();
		data[pointer++] = matrix.m13();
		data[pointer++] = matrix.m20();
		data[pointer++] = matrix.m21();
		data[pointer++] = matrix.m22();
		data[pointer++] = matrix.m23();
		data[pointer++] = matrix.m30();
		data[pointer++] = matrix.m31();
		data[pointer++] = matrix.m32();
		data[pointer++] = matrix.m33();
	}

	protected void dispose()
	{
		shader.dispose();
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
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDepthMask(false);

	}
	
	private void finishRendering()
	{
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
