package render;

import model.Model;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import shader.TerrainShader;
import terrain.Terrain;
import texture.TerrainTexturePack;
import toolbox.GeomMath;
import util.Intention;

import java.util.List;

public class TerrainRenderer
{
    private TerrainShader shader;
    public TerrainRenderer(TerrainShader shader, Matrix4f projection)
    {
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projection);
        shader.connectTextureUnits();
        shader.stop();
    }

    public void render(List<Terrain> terrains)
    {
        for(Terrain terrain : terrains)
        {
            prepareTerrain(terrain);
            loadTerrain(terrain);
            GL11.glDrawElements(GL11.GL_TRIANGLES,
                    terrain.getModel().getVertexCount(),
                    GL11.GL_UNSIGNED_INT, 0);
            unbindTerrain();
        }
    }

    public void prepareTerrain(Terrain terrain)
    {
        Model model = terrain.getModel();
        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        bindTextures(terrain);
        shader.loadShine(1, 0);
    }

    private void bindTextures(Terrain terrain)
    {
        TerrainTexturePack texturePack = terrain.getTexturePack();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getBackground().getTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getRed().getTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getGreen().getTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE3);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getBlue().getTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE4);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getBlendMap().getTextureID());

    }

    private void unbindTerrain()
    {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    private void loadTerrain(Terrain terrain)
    {
        @Intention(design = "for additional texture and shader effect")
        Matrix4f matrix4f = GeomMath.createTransformationMatrix(
                new Vector3f(terrain.getX(), 0, terrain.getZ()),
                0, 0, 0, 1
        );
        shader.loadTransformMatrix(matrix4f);
    }
}
