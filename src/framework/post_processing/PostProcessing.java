package framework.post_processing;

import framework.hardware.Display;
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
	private static ContrastChanger contrastChanger;
	private static VerticalBlur verticalBlur;
	private static HorizontalBlur horizontalBlur;
	private static BrightFilter brightFilter;
	private static CombineFilter combineFilter;


	public static void initialize()
	{
		quad = ModelLoader.loadToVaoInt(POSITIONS, 2);
		contrastChanger = new ContrastChanger(Display.getWidth(), Display.getHeight());
		verticalBlur = new VerticalBlur(Display.getWidth() / 2, Display.getHeight() / 2);
		horizontalBlur = new HorizontalBlur(Display.getWidth() / 2, Display.getHeight() / 2);
		brightFilter = new BrightFilter(Display.getWidth() / 2, Display.getHeight() / 2);
		combineFilter = new CombineFilter();
	}
	
	public static void doPostProcessing(int colorTexture, int brightTexture){
		start();

		horizontalBlur.render(brightTexture);
		verticalBlur.render(horizontalBlur.getOutputTexture());
		contrastChanger.render(colorTexture);
		combineFilter.render(contrastChanger.getOutputTexture(), verticalBlur.getOutputTexture());
		end();
	}
	
	public static void dispose(){
		contrastChanger.dispose();
		verticalBlur.dispose();
		horizontalBlur.dispose();
		brightFilter.dispose();
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
