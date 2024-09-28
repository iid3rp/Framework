#version 400 core

in vec2 texturePosition;
in vec2 coords;
in vec2 passTextureCoords;

out vec4 out_Color;

uniform sampler2D guiTexture;
uniform sampler2D clipTexture;

bool bound()
{
	return !(texturePosition.x < 0.0 || texturePosition.x > 1.0 ||
	texturePosition.y < 0.0 || texturePosition.y > 1.0);
}

void main(void)
{
	if(bound())
	{
		vec4 color = texture(guiTexture, texturePosition);
		vec4 clip = texture(clipTexture, texturePosition);
		color = color * (1 - clip.b);
		out_Color = color;
	}
	else out_Color = vec4(1);
}