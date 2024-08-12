package framework.swing;

import framework.shader.GLShader;
import org.joml.Matrix4f;
import org.joml.Vector2f;

public class GUIShader extends GLShader
{
	
	private static final String VERTEX_FILE = "GUIVertexShader.glsl";
	private static final String FRAGMENT_FILE = "GUIFragmentShader.glsl";
	private int locationTransformationMatrix;
	private int locationSize;
	private int locationPosition;
	private int locationScale;
	private int locationInvertTransformationMatrix;
	private int locationImageLocation;

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
		locationSize = super.getUniformLocation("size");
		locationPosition = super.getUniformLocation("pos");
		locationScale = super.getUniformLocation("scale");
		locationInvertTransformationMatrix = super.getUniformLocation("invertTransformationMatrix");
		locationImageLocation = super.getUniformLocation("imageLocation");
	}

	@Override
	protected void bindAttributes()
	{
		super.bindAttribute(0, "position");
	}


	public void loadSize(Vector2f size)
	{
		super.loadVector(locationSize, size);
	}

	public void loadPosition(Vector2f position)
	{
		super.loadVector(locationPosition, position);
	}

	public void loadScale(Vector2f scale)
	{
		super.loadVector(locationScale, scale);
	}

	public void loadInvertTransformationMatrix(Matrix4f invMatrix)
	{
		super.loadMatrix(locationInvertTransformationMatrix, invMatrix);
	}

	public void loadImageLocation(Vector2f imageLocation)
	{
		super.loadVector(locationImageLocation, imageLocation);
	}
}
