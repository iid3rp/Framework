package render;

import entity.Camera;
import model.Model;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import shader.SkyboxShader;
import texture.Texture;

public class SkyboxRenderer
{
    public static float SIZE = 5000f;

    private static final float[] VERTICES = {
            -SIZE,  SIZE, -SIZE,
            -SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE,  SIZE, -SIZE,
            -SIZE,  SIZE, -SIZE,

            -SIZE, -SIZE,  SIZE,
            -SIZE, -SIZE, -SIZE,
            -SIZE,  SIZE, -SIZE,
            -SIZE,  SIZE, -SIZE,
            -SIZE,  SIZE,  SIZE,
            -SIZE, -SIZE,  SIZE,

            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,

            -SIZE, -SIZE,  SIZE,
            -SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE, -SIZE,  SIZE,
            -SIZE, -SIZE,  SIZE,

            -SIZE,  SIZE, -SIZE,
            SIZE,  SIZE, -SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            -SIZE,  SIZE,  SIZE,
            -SIZE,  SIZE, -SIZE,

            -SIZE, -SIZE, -SIZE,
            -SIZE, -SIZE,  SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            -SIZE, -SIZE,  SIZE,
            SIZE, -SIZE,  SIZE
    };

    public static String[] textureFiles =
    {
            "right",
            "left",
            "top",
            "bottom",
            "back",
            "front",
    };

    public static String[] nightTextureFiles =
    {
            "nightRight",
            "nightLeft",
            "nightTop",
            "nightBottom",
            "nightBack",
            "nightFront",
    };

    private Model cube;
    private Texture texture;
    private Texture nightTexture;
    public SkyboxShader shader;
    private float time = 0;

    public SkyboxRenderer(Matrix4f projection)
    {
        cube = ModelLoader.loadToVAO(VERTICES, 3);
        texture = new Texture(ModelLoader.loadCubeMap(textureFiles));
        nightTexture = new Texture(ModelLoader.loadCubeMap(nightTextureFiles));
        shader = new SkyboxShader();
        shader.start();
        shader.connectTextureUnits();
        shader.loadProjectionMatrix(projection);
        shader.stop();
    }

    public void render(Camera camera)
    {
        shader.start();
        shader.loadViewMatrix(camera);
        GL30.glBindVertexArray(cube.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        bindTextures();
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
        GL30.glBindVertexArray(0);
        shader.stop();
    }

    private void bindTextures()
    {
        int texture1;
        int texture2;
        texture1 = texture.getID();
        texture2 = nightTexture.getID();

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture1);
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture2);
        shader.loadBlendFactor(.5f);
    }
}


