package framework.post_processing;

import static org.lwjgl.opengl.GL33.*;

public class TextureFromFBO {
    //private final PixelBufferObject pbo;
    private int textureId;
    private int fboId;
    private int width, height;

    public TextureFromFBO(int fboId, int width, int height) {
        this.fboId = fboId;
        this.width = width;
        this.height = height;

        //pbo = new PixelBufferObject(); // Assuming RGBA format


    }

    public void copyFBOToTexture() {
        glBindFramebuffer(GL_FRAMEBUFFER, fboId); // Bind FBO
        glReadBuffer(GL_COLOR_ATTACHMENT0); // Assuming color attachment 0

        //pbo.bind();
        glCopyTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, 0, 0, width, height, 0);
        //pbo.unbind();

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    // ... other methods ...
}
