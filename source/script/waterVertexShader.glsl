#version 400 core

in vec2 position;

out vec4 clipSpace;
out vec2 textureCoordinates;
out vec3 toCameraPosition;
out vec3 fromLightVector;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;
uniform vec3 lightPosition;

uniform vec3 cameraPosition;

const float tiling = 4.0;


void main(void)
{
	vec4 worldPosition = modelMatrix * vec4(position.x, 0.0, position.y, 1.0);
	clipSpace = projectionMatrix * viewMatrix * worldPosition;
	gl_Position = clipSpace;
	textureCoordinates = vec2(position.x / 2.0 + .5, position.y / 2.0 + 0.5) * tiling;
	toCameraPosition = cameraPosition - worldPosition.xyz;
	fromLightVector = worldPosition.xyz - lightPosition;
}