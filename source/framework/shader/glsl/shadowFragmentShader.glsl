#version 400 core

out vec4 outColor;
in vec2 textureCoords;

uniform sampler2D modelTexture;

void main(void)
{

	float alpha = texture(modelTexture, textureCoords).a;
	if(alpha == 0)
	{
		discard;
	}

	outColor = vec4(1.0);
	
}