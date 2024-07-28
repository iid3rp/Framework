package framework.post_processing;

import framework.Display.DisplayManager;
import framework.loader.ModelLoader;
import framework.model.Model;
import framework.post_processing.blur.HorizontalBlur;
import framework.post_processing.blur.VerticalBlur;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class PostProcessing {
	
	private static final float[] POSITIONS = { -1, 1, -1, -1, 1, 1, 1, -1 };	
	private static Model quad;
	private static ContrastChanger contrastChanger;
	private static VerticalBlur verticalBlur;
	private static HorizontalBlur horizontalBlur;


	public static void initialize()
	{
		quad = ModelLoader.loadToVao(POSITIONS, 2);
		contrastChanger = new ContrastChanger();
		verticalBlur = new VerticalBlur(DisplayManager.getWindowWidth() / 4, DisplayManager.getWindowHeight() / 4);
		horizontalBlur = new HorizontalBlur(DisplayManager.getWindowWidth() / 4, DisplayManager.getWindowHeight() / 4);
	}
	
	public static void doPostProcessing(int colourTexture){
		start();

//		horizontalBlur.render(colourTexture);
//		verticalBlur.render(horizontalBlur.getOutputTexture());
		contrastChanger.render(colourTexture);
		end();
	}
	
	public static void dispose(){
		contrastChanger.dispose();
		verticalBlur.dispose();
		horizontalBlur.dispose();
	}
	
	private static void start(){
		GL30.glBindVertexArray(quad.getVaoId());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}
	
	private static void end(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}


}
