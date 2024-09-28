#version 140

in vec2 position;

in mat4 modelViewMatrix;
in vec4 textureOffsets;
in float blendFactor;

out vec2 textureCoords[2];
out float blend;

uniform mat4 projectionMatrix;
uniform float numOfRows;


void main(void){

	// the coordinates based on the position
	vec2 coords = position + vec2(0.5, 0.5);
	coords.y = 1 - coords.y;
	coords /= numOfRows;

	// the texture coordinates to transition the animation
	textureCoords[0] = coords + textureOffsets.xy;
	textureCoords[1] = coords + textureOffsets.zw;
	blend = blendFactor;

	// position of the vertex
	gl_Position = projectionMatrix * modelViewMatrix * vec4(position, 0.0, 1.0);

}