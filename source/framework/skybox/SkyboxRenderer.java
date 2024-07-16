package framework.skybox;

import framework.ModelLoader;
import framework.entity.Camera;
import framework.model.Model;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import framework.textures.Texture;

public class SkyboxRenderer
{
    public static float SIZE = 1000f;

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
        cube = ModelLoader.loadToVao(VERTICES, 3);
        texture = new Texture(ModelLoader.loadCubeMap(textureFiles));
        nightTexture = new Texture(ModelLoader.loadCubeMap(nightTextureFiles));
        shader = new SkyboxShader();
        shader.bind();
        shader.connectTextureUnits();
        shader.loadProjectionMatrix(projection);
        shader.unbind();
    }

    public void render(Camera camera)
    {
        shader.bind();
        shader.loadViewMatrix(camera);
        GL30.glBindVertexArray(cube.getVaoId());
        GL20.glEnableVertexAttribArray(0);
        bindTextures();
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
        GL30.glBindVertexArray(0);
        shader.unbind();
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


