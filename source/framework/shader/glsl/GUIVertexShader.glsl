#version 400 core

in vec2 position;

out vec2 textureCoords;
out vec2 texturePosition;

uniform mat4 transformationMatrix;
uniform mat4 invertTransformationMatrix;
uniform vec2 imageLocation;
uniform vec2 size;

void main(void)
{
	gl_Position = transformationMatrix * vec4(position, 0.0, 1.0);
	vec2 imgLoc = imageLocation;
	textureCoords = vec2(((position.x + 1.0) / 2.0), (1 - (position.y + 1.0) / 2.0));
	textureCoords = vec2(textureCoords.x + imgLoc.x, textureCoords.y - imgLoc.y);
	texturePosition = position;
}