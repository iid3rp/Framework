package framework.water;

public class WaterTile {
	
	public static final float TILE_SIZE = 800;
	
	private float height;
	private float x,z;
	
	public WaterTile(float centerX, float centerZ, float height){
		this.x = .5f;
		this.z = .5f;
		this.height = height;
	}

	public float getHeight() {
		return height;
	}

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}

}
