package framework.post_processing.bloom;

import framework.shader.GLShader;

public class BrightFilterShader extends GLShader
{
	
	private static final String VERTEX_FILE = "bloomVertexShader.glsl";
	private static final String FRAGMENT_FILE = "bloomBrightFilterFragmentShader.glsl";
	
	public BrightFilterShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {	
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}
