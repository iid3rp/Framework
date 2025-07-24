#version 460 core

// from the vertex shader
in vec2 passCoord;

// uniforms
uniform sampler2D texture;

// output color (final)
out vec4 outColor;

void main(void)
{
    outColor = texture(texture, passCoord);
}