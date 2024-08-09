package framework.font;

/**
 * Simple data structure class holding information about a certain glyph in the
 * font texture atlas. All sizes are for a font-size of 1.
 * 
 * @author Karl Wimble
 *
 */
public class Character {

	private int id;
	private double xTextureCoordinates;
	private double yTextureCoordinates;
	private double xMaxTextureCoordinates;
	private double yMaxTextureCoordinates;
	private double xOffset;
	private double yOffset;
	private double sizeX;
	private double sizeY;
	private double xAdvance;

	/**
	 * @param id
	 *            - the ASCII value of the character.
	 * @param xTextureCoordinates
	 *            - the x texture coordinate for the top left corner of the
	 *            character in the texture atlas.
	 * @param yTextureCoordinates
	 *            - the y texture coordinate for the top left corner of the
	 *            character in the texture atlas.
	 * @param xTexSize
	 *            - the width of the character in the texture atlas.
	 * @param yTexSize
	 *            - the height of the character in the texture atlas.
	 * @param xOffset
	 *            - the x distance from the curser to the left edge of the
	 *            character's quad.
	 * @param yOffset
	 *            - the y distance from the curser to the top edge of the
	 *            character's quad.
	 * @param sizeX
	 *            - the width of the character's quad in screen space.
	 * @param sizeY
	 *            - the height of the character's quad in screen space.
	 * @param xAdvance
	 *            - how far in pixels the cursor should advance after adding
	 *            this character.
	 */
	protected Character(int id, double xTextureCoordinates, double yTextureCoordinates, double xTexSize, double yTexSize,
			double xOffset, double yOffset, double sizeX, double sizeY, double xAdvance) {
		this.id = id;
		this.xTextureCoordinates = xTextureCoordinates;
		this.yTextureCoordinates = yTextureCoordinates;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.xMaxTextureCoordinates = xTexSize + xTextureCoordinates;
		this.yMaxTextureCoordinates = yTexSize + yTextureCoordinates;
		this.xAdvance = xAdvance;
	}

	protected int getId() {
		return id;
	}

	protected double getxTextureCoord() {
		return xTextureCoordinates;
	}

	protected double getyTextureCoord() {
		return yTextureCoordinates;
	}

	protected double getXMaxTextureCoord() {
		return xMaxTextureCoordinates;
	}

	protected double getYMaxTextureCoord() {
		return yMaxTextureCoordinates;
	}

	protected double getxOffset() {
		return xOffset;
	}

	protected double getyOffset() {
		return yOffset;
	}

	protected double getSizeX() {
		return sizeX;
	}

	protected double getSizeY() {
		return sizeY;
	}

	protected double getxAdvance() {
		return xAdvance;
	}

}
