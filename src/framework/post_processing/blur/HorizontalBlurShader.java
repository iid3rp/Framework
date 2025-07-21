package framework.post_processing.blur;

import framework.shader.GLShader;

public class HorizontalBlurShader extends GLShader
{

	private static final String VERTEX_FILE = "horizontalBlurVertexShader.glsl";
	private static final String FRAGMENT_FILE = "blurFragmentShader.glsl";
	
	private int location_targetWidth;
	
	protected HorizontalBlurShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	protected void loadTargetWidth(float width){
		super.loadFloat(location_targetWidth, width);
	}
	
	@Override
	protected void getAllUniformLocations() {
		location_targetWidth = super.getUniformLocation("targetWidth");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
	
}
