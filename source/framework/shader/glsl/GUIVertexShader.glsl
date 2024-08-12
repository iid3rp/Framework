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
	vec2 imgLoc = imageLocation / size;
	textureCoords = vec2((position.x - imgLoc.x + 1.0) / 2.0, 1 - (position.y + imgLoc.y + 1.0) / 2.0);
	texturePosition = vec2(position.x - imgLoc.x, position.y + imgLoc.y);
}