#version 140

in vec2 position;

out vec2 textureCoordinates;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

void main(void)
{

	textureCoordinates = position + vec2 (0.5, 0.5);
	textureCoordinates.y = 1.0 - textureCoordinates.y;
	gl_Position = projectionMatrix * modelViewMatrix * vec4(position, 0.0, 1.0);

}