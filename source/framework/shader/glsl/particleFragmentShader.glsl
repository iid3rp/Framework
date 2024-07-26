#version 140

out vec4 out_colour;

in vec2 textureCoords[2];
in float blend;

uniform sampler2D particleTexture;

vec4 color[2];

void main(void){

	color[0] = texture(particleTexture, textureCoords[0]);
	color[1] = texture(particleTexture, textureCoords[1]);

	out_colour = mix(color[0], color[1], blend);

}