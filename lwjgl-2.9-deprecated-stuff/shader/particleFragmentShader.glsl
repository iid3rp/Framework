#version 140

out vec4 out_color;

in vec2 textureCoordinates1;
in vec2 textureCoordinates2;
in float blend;

uniform sampler2D particleTexture;

void main(void)
{
	vec4 color1 = texture(particleTexture, textureCoordinates1);
	vec4 color2 = texture(particleTexture, textureCoordinates2);
	out_color = mix(color1, color2, blend);
}