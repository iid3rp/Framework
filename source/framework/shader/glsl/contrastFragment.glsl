#version 140

in vec2 textureCoords;

out vec4 outColor;

uniform sampler2D colorTecture;

// make this uniform:
const float contrast = 0.3;

void main(void){

	outColor = texture(colorTecture, textureCoords).rgba;
	outColor.rgb = (outColor.rgb - 0.5) * (1 + contrast) + 0.5;

}