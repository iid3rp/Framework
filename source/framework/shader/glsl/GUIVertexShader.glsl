#version 400 core

in vec2 position;
in vec2 textureCoords;

out vec2 texturePosition;
out vec2 passTextureCoords;
out vec2 coords;

uniform mat4 transformationMatrix;
uniform vec2 imageLocation;
uniform vec2 size;
uniform vec2 scale;

void main(void)
{
	gl_Position = transformationMatrix * vec4(position , 0.0, 1.0);

	// Offset the texture coordinates based on the image location
	vec2 offset = imageLocation / size;

	// Scale and offset the texture coordinates
	texturePosition = vec2(textureCoords / scale) - offset;

	//texturePosition = textureCoords - (imageLocation / size);
}