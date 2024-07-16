package framework.swing;

import framework.shader.GLShader;
import org.joml.Matrix4f;

public class GUIShader extends GLShader
{
	
	private static final String VERTEX_FILE = "resources/shaders/guiVertexShader.glsl";
	private static final String FRAGMENT_FILE = "resources/shaders/guiFragmentShader.glsl";
	private int locationTransformationMatrix;

	public GUIShader()
	{
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	public void loadTransformation(Matrix4f matrix)
	{
		super.loadMatrix(locationTransformationMatrix, matrix);
	}

	@Override
	protected void getAllUniformLocations()
	{
		locationTransformationMatrix = super.getUniformLocation("transformationMatrix");
	}

	@Override
	protected void bindAttributes()
	{
		super.bindAttribute(0, "position");
	}
	
	
	

}
