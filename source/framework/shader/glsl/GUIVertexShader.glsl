#version 400 core

in vec2 position;

out vec2 textureCoords;
out vec2 texturePosition;

uniform mat4 transformationMatrix;
uniform mat4 invertTransformationMatrix;
uniform vec2 imageLocation;

void main(void)
{
	gl_Position = transformationMatrix * vec4(position, 0.0, 1.0);
	textureCoords = vec2((position.x + 1.0) / 2.0, 1 - (position.y + 1.0) / 2.0);
	vec2 imgPos = vec2(imageLocation.x * 2 - 1, imageLocation.y * -2 + 1);
	texturePosition = position + imgPos;
}