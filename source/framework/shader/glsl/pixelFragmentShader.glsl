#version 330 core

out vec4 outColor;

in vec2 textureCoords;

uniform sampler2D screenTexture;

void main()
{
    outColor = texture(screenTexture, textureCoords);
    outColor = vec4(1);
}