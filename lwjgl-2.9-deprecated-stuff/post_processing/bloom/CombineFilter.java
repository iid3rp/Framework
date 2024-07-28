package framework.post_processing.bloom;

import framework.post_processing.ImageRenderer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class CombineFilter {
	
	private ImageRenderer renderer;
	private CombineShader shader;
	
	public CombineFilter(int width, int height){
		shader = new CombineShader();
		shader.bind();
		shader.connectTextureUnits();
		shader.unbind();
		renderer = new ImageRenderer(width, height);
	}
	
	public void render(int colourTexture, int highlightTexture){
		shader.bind();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, colourTexture);
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, highlightTexture);
		renderer.renderQuad();
		shader.unbind();
	}

	public int getOutputTexture()
	{
		return renderer.getOutputTexture();
	}
	
	public void dispose(){
		renderer.dispose();
		shader.dispose();
	}

}
