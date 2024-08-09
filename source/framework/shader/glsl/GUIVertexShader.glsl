#version 400 core

in vec2 position;

out vec2 textureCoords;
out vec2 texturePosition;

uniform mat4 transformationMatrix;
uniform mat4 invertTransformationMatrix;

void main(void)
{
	gl_Position = invertTransformationMatrix * vec4(position, 0.0, 1.0);
	textureCoords = vec2((position.x + 1.0) / 2.0, 1 - (position.y + 1.0) / 2.0);
	texturePosition = position;
}