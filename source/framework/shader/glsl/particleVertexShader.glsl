#version 140

in vec2 position;

in mat4 modelViewMatrix;
in vec4 texOffset;
in float blendFactor;

out vec2 textureCoordinates1;
out vec2 textureCoordinates2;
out float blend;

uniform mat4 projectionMatrix;
uniform float numOfRows;

void main(void)
{
	vec2 textureCoordinates = position + vec2 (0.5, 0.5);
	textureCoordinates.y = 1.0 - textureCoordinates.y;

	textureCoordinates /= numOfRows;
	textureCoordinates1 = textureCoordinates + texOffset.xy;
	textureCoordinates2 = textureCoordinates + texOffset.zw;
	blend = blendFactor;

	gl_Position = projectionMatrix * modelViewMatrix * vec4(position, 0.0, 1.0);

}