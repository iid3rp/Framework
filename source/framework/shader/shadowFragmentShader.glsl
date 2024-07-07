#version 400 core

out vec4 out_colour;
in vec2 textureCoords;

uniform sampler2D modelTexture;

void main(void)
{

	float alpha = texture(modelTexture, textureCoords).a;
	if(alpha < .5)
	{
		discard;
	}

	out_colour = vec4(1.0);
	
}