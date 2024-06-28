package particles;

import java.util.List;

import entity.Camera;
import model.Model;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import render.ModelLoader;
import toolbox.GeomMath;

public class ParticleRenderer {
	
	private static final float[] VERTICES = {-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f};
	private Model quad;
	private ParticleShader shader;
	
	protected ParticleRenderer(Matrix4f projectionMatrix){
		quad = ModelLoader.loadToVAO(VERTICES, 2);
		shader = new ParticleShader();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	protected void render(List<Particle> particles, Camera camera){
		Matrix4f view = GeomMath.createViewMatrix(camera);
		prepare();
		for(Particle particle : particles) {
			updateModelViewMatrix(particle.getPosition(), particle.getRotation(), particle.getScale(), view);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		}
		finishRendering();

	}

	private void updateModelViewMatrix(Vector3f position, float rotation, float scale, Matrix4f view)
	{
		Matrix4f model = new Matrix4f();
		Matrix4f.translate(position, model, model);
		model.m00 = view.m00;
		model.m01 = view.m10;
		model.m02 = view.m20;
		model.m10 = view.m01;
		model.m11 = view.m11;
		model.m12 = view.m21;
		model.m20 = view.m02;
		model.m21 = view.m12;
		model.m22 = view.m22;
		Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0, 0 ,1), model, model);
		Matrix4f.scale(new Vector3f(scale, scale, scale), model, model);
		Matrix4f modelView = Matrix4f.mul(view, model, null);
		shader.loadModelViewMatrix(modelView);
	}

	protected void dispose()
	{
		shader.dispose();
	}
	
	private void prepare()
	{
		shader.start();
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDepthMask(false);

	}
	
	private void finishRendering()
	{
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

}
