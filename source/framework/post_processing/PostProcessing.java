package framework.post_processing;

import framework.Display.DisplayManager;
import framework.loader.ModelLoader;
import framework.model.Model;
import framework.post_processing.bloom.BrightFilter;
import framework.post_processing.bloom.CombineFilter;
import framework.post_processing.blur.HorizontalBlur;
import framework.post_processing.blur.VerticalBlur;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class PostProcessing {
	
	private static final float[] POSITIONS = { -1, 1, -1, -1, 1, 1, 1, -1 };	
	private static Model quad;
	private static ContrastChanger changer;
	private static HorizontalBlur horizontalBlur;
	private static VerticalBlur verticalBlur;
	private static BrightFilter brightFilter;
	private static CombineFilter combineFilter;

	public static void initialize(){
		quad = ModelLoader.loadToVao(POSITIONS, 2);
		changer = new ContrastChanger();
		horizontalBlur = new HorizontalBlur(DisplayManager.getWindowWidth() / 4, DisplayManager.getWindowHeight() / 4);
		verticalBlur = new VerticalBlur(DisplayManager.getWindowWidth() / 4, DisplayManager.getWindowHeight() / 4);
		brightFilter = new BrightFilter(DisplayManager.getWindowWidth() / 2, DisplayManager.getWindowHeight() / 2);
		combineFilter = new CombineFilter(DisplayManager.getWindowWidth() / 2, DisplayManager.getWindowHeight() / 2);
	}
	
	public static void doPostProcessing(int colorTexture, int brightTexture){
		start();
		//intentional
//		if(false)
//		{
//			horizontalBlur.render(colorTexture);
//			verticalBlur.render(horizontalBlur.getOutputTexture());
//			changer.render(verticalBlur.getOutputTexture());
//		}
		//brightFilter.render(colorTexture);
		horizontalBlur.render(brightTexture);
		verticalBlur.render(horizontalBlur.getOutputTexture());
		combineFilter.render(colorTexture, verticalBlur.getOutputTexture());
		changer.render(combineFilter.getOutputTexture());
		stop();
	}
	
	public static void dispose()
	{
		changer.dispose();
		horizontalBlur.dispose();
		verticalBlur.dispose();
		brightFilter.dispose();
		combineFilter.dispose();
	}
	
	private static void start(){
		GL30.glBindVertexArray(quad.getVaoId());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}
	
	private static void stop(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}


}
