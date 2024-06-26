package shader;

import org.lwjgl.util.vector.Matrix4f;

public class GUIShader extends GLShader
{
	
	private static final String VERTEX_FILE = "source/script/guiVertexShader.glsl";
	private static final String FRAGMENT_FILE = "source/script/guiFragmentShader.glsl";
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
