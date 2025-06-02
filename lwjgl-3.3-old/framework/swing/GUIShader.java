package framework.swing;

import framework.lang.Mat4;
import framework.lang.Vec2;
import framework.shader.GLShader;

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
	private int locationDisplayMatrix;

	public GUIShader()
	{
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	public void loadTransformation(Mat4 matrix)
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
		locationDisplayMatrix = super.getUniformLocation("displayMatrix");
	}

	@Override
	protected void bindAttributes()
	{
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}


	public void loadSize(Vec2 size)
	{
		super.loadVector(locationSize, size);
	}

	public void loadPosition(Vec2 position)
	{
		super.loadVector(locationPosition, position);
	}

	public void loadScale(Vec2 scale)
	{
		super.loadVector(locationScale, scale);
	}

	public void loadInvertTransformationMatrix(Mat4 invMatrix)
	{
		super.loadMatrix(locationInvertTransformationMatrix, invMatrix);
	}

	public void loadImageLocation(Vec2 imageLocation)
	{
		super.loadVector(locationImageLocation, imageLocation);
	}

	public void loadDisplayMatrix(Mat4 displayMatrix)
	{
		super.loadMatrix(locationDisplayMatrix, displayMatrix);
	}
}
