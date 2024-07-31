package framework.post_processing;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexParameteri;

public class PixelBufferObject {
    private final int id;
    private final int size;
    private final int target;
    private IntBuffer buffer;
    private int textureId;

    public PixelBufferObject(int size, int target) {
        this.size = size;
        this.target = target;

        IntBuffer pboBuffer = BufferUtils.createIntBuffer(1);
        GL30.glGenBuffers(pboBuffer);
        id = pboBuffer.get(0);

        GL30.glBindBuffer(target, id);
        GL30.glBufferData(target, size, GL30.GL_STREAM_DRAW);

        // Create texture
        IntBuffer textureBuffer = BufferUtils.createIntBuffer(1);
        glGenTextures(textureBuffer);
        textureId = textureBuffer.get(0);
        glBindTexture(GL_TEXTURE_2D, textureId);
        // Texture parameters (adjust as needed)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    }

    public void copyFBOToTexture(FrameBufferObject object)
    {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, object.getColorTexture()); // Bind FBO

        // Assuming color attachment 2,
        // (because it is meant for mouse events)
        GL11.glReadBuffer(GL30.GL_COLOR_ATTACHMENT2);

        bind();
        GL11.glCopyTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, 0, 0, object.getWidth(), object.getHeight(), 0);
        unbind();

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }

    public IntBuffer getDataFromPBO() {
        return getBuffer(); // Assuming a getData() method in PixelBufferObject
    }

    public void bind() {
        GL30.glBindBuffer(target, id);
    }

    public void unbind() {
        GL30.glBindBuffer(target, 0);
    }

    public int getTextureId()
    {
        return textureId;
    }

    public int getId() {
        return id;
    }

    public int getSize() {
        return size;
    }

    public int getTarget() {
        return target;
    }

    public IntBuffer getBuffer()
    {
        return buffer;
    }

    public void dispose() {
        GL30.glDeleteBuffers(id);
    }
}
