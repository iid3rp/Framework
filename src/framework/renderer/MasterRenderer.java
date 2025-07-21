package framework.renderer;



import framework.model.Model;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;

public class MasterRenderer {
    public static final float FOV = 60;
    public static final float NEAR_PLANE = 0.1f;
    public static final float FAR_PLANE = 100_000;
    public static final float SKY_RED = 0.95f;
    public static final float SKY_GREEN = 0.9f;
    public static final float SKY_BLUE = 0.67f;



    public static void setRenderer()
    {
        enableCulling();
        createProjectionMatrix();

    }

    public static void enableCulling() {
        // don't texture surface with normal vectors facing away from the "camera"
        // don't render back the faces of a framework.model.
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
    }

    public static void disableCulling() {
        glDisable(GL_CULL_FACE);
    }

    public static void processEntity() {

    }

    public static void render(Model model) {
        glBindVertexArray(model.getVaoId());
        glEnableVertexAttribArray(0);
        glDrawElements(GL_TRIANGLES, model.getVertexCount(), GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);

    }

    public static void prepare() {
        //glEnable(GL_DEPTH_TEST);    // test which triangles are in front and render them in the correct order
        glClearColor(1,0, 0, 1);      // Load selected color into the color buffer
        glClear(GL_COLOR_BUFFER_BIT);     // Clear the screen and draw with color in color buffer
    }

    private static void createProjectionMatrix() {

    }

}
