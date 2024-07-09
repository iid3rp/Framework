package framework.post_processing;

import framework.display.ModelLoader;
import framework.model.Model;
import framework.post_processing.blur.HorizontalBlur;
import framework.post_processing.blur.VerticalBlur;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class PostProcessing {
	
	private static final float[] POSITIONS = { -1, 1, -1, -1, 1, 1, 1, -1 };	
	private static Model quad;
	private static ContrastChanger changer;
	private static HorizontalBlur horizontalBlur;
	private static VerticalBlur verticalBlur;

	public static void initialize(){
		quad = ModelLoader.loadToVAO(POSITIONS, 2);
		changer = new ContrastChanger();
		horizontalBlur = new HorizontalBlur(Display.getWidth() / 2, Display.getHeight() / 2);
		verticalBlur = new VerticalBlur(Display.getWidth() / 2, Display.getHeight() / 2);
	}
	
	public static void doPostProcessing(int colorTexture){
		start();
		//intentional
//		if(false)
//		{
//			horizontalBlur.render(colorTexture);
//			verticalBlur.render(horizontalBlur.getOutputTexture());
//			changer.render(verticalBlur.getOutputTexture());
//		}
		changer.render(colorTexture);
		end();
	}
	
	public static void dispose()
	{
		changer.dispose();
		horizontalBlur.dispose();
		verticalBlur.dispose();
	}
	
	private static void start(){
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}
	
	private static void end(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}


}
