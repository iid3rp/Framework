package framework.post_processing;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class GodRayRenderer
{
    private ImageRenderer renderer;
    private GodRayShader shader;

    public GodRayRenderer(int width, int height){
        shader = new GodRayShader();
        renderer = new ImageRenderer(width, height);
    }

    public void render(int texture){
        shader.bind();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        renderer.renderQuad();
        shader.unbind();
    }

    public int getOutputTexture(){
        return renderer.getOutputTexture();
    }

    public void dispose(){
        renderer.dispose();
        shader.dispose();
    }
}
