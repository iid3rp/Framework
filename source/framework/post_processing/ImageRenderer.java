package framework.post_processing;

import org.lwjgl.opengl.GL11;

public class ImageRenderer {

	private FrameBufferObjects fbo;

	public ImageRenderer(int width, int height) {
		this.fbo = new FrameBufferObjects(width, height, FrameBufferObjects.NONE);
	}

	protected ImageRenderer() {}

	public void renderQuad() {
		if (fbo != null) {
			fbo.bindFrameBuffer();
		}
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		if (fbo != null) {
			fbo.unbindFrameBuffer();
		}
	}

	public int getOutputTexture() {
		return fbo.getColourTexture();
	}

	public void dispose() {
		if (fbo != null) {
			fbo.dispose();
		}
	}

}
