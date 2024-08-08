#version 400 core

in vec2 textureCoords;
in vec2 texturePosition;

out vec4 out_Color;

uniform sampler2D guiTexture;
uniform sampler2D clipTexture;
uniform vec2 size;
uniform vec2 pos;
uniform vec2 scale;

void main(void)
{
	// pos with size must be within bounds of the textureCoords...
	// top left position vec2[-1, 1];
	// pos must be
	// tex.x + pos.x + size.x;
	// tex.y - pos.y - size.y;
	// vec2 rectangleWidth = vec2(tex.x + pos.x, tex.x + pos.x + size.x);
	float sizeX = size.x <= scale.x? size.x / scale.x : 1;
	float sizeY = size.y <= scale.y? size.y / scale.y : 1;
	vec2 normSize = vec2(sizeX, sizeY);
	vec2 normTop = vec2((pos.x * 2) - 1, normSize.x * 2 - 1);
	vec2 normBot = vec2((pos.y * -2) + 1, normSize.y * -2 + 1);

	// size must be based on the position
	if(texturePosition.x >= normTop.x && texturePosition.x <= normTop.y &&
	   texturePosition.y <= normBot.x && texturePosition.y >= normBot.y)
	{
		vec4 color = texture(guiTexture,textureCoords);
		vec4 clip = texture(clipTexture, textureCoords);
		color = color * (clip.b);
		out_Color = color;
	}
	else out_Color = vec4(1);
}