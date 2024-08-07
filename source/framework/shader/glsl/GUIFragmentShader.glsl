#version 400 core

in vec2 textureCoords;

out vec4 out_Color;

uniform sampler2D guiTexture;
uniform vec2 size;
uniform vec2 pos;

void main(void)
{
	// pos with size must be within bounds of the textureCoords...
	// top left position vec2[-1, 1];
	// pos must be
	// tex.x + pos.x + size.x;
	// tex.y - pos.y - size.y;
	// vec2 rectangleWidth = vec2(tex.x + pos.x, tex.x + pos.x + size.x);

	// size must be based on the position
	out_Color =
		texture(guiTexture,textureCoords);
}