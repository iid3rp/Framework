package shadow;

import org.lwjgl.util.vector.Matrix4f;
import shader.GLShader;

public class ShadowShader extends GLShader {
	
	private static final String VERTEX_FILE = "source/shadow/shadowVertexShader.glsl";
	private static final String FRAGMENT_FILE = "source/shadow/shadowFragmentShader.glsl";
	
	private int location_mvpMatrix;

	protected ShadowShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_mvpMatrix = super.getUniformLocation("mvpMatrix");
		
	}
	
	protected void loadMvpMatrix(Matrix4f mvpMatrix){
		super.loadMatrix(location_mvpMatrix, mvpMatrix);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "in_position");
		super.bindAttribute(1, "in_textureCoords");
	}

}
