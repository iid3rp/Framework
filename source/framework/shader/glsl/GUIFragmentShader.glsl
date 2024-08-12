#version 400 core

in vec2 textureCoords;
in vec2 texturePosition;

out vec4 out_Color;

uniform sampler2D guiTexture;
uniform sampler2D clipTexture;
uniform vec2 size;
uniform vec2 pos;
uniform vec2 scale;
uniform mat4 transformationMatrix;
uniform mat4 invertTransformationMatrix;
uniform vec2 imageLocation;

void main(void)
{
	// Transform the texture position back to its original space using the inverse transformation matrix
	//vec4 transformedPosition = transformationMatrix * vec4(texturePosition, 0, 1.0);
	vec2 textPos = texturePosition;

	float sizeX = size.x <= scale.x? size.x / scale.x : 1;
	float sizeY = size.y <= scale.y? size.y / scale.y : 1;
	vec2 normSize = vec2(sizeX, sizeY);
	vec2 normTop = vec2((pos.x * 2) - 1, (normSize.x * 2) - 1);
	vec2 normBot = vec2((pos.y * -2) + 1, (normSize.y * -2) + 1);

	// size must be based on the position
	if(textPos.x >= normTop.x && textPos.x <= normTop.y &&
	   textPos.y <= normBot.x && textPos.y >= normBot.y)
	{
		vec4 color = texture(guiTexture, textureCoords);
		vec4 clip = texture(clipTexture, textureCoords);
		color = color * (1 - clip.b);
		out_Color = color;
		// debug: out_Color = vec4(.4, .2, .4, .4f);
	}
	else out_Color = vec4(1);
}