#version 400 core

layout(location = 0) in vec2 position;
layout(location = 1) in vec2 textureCoords;

out vec2 texturePosition;
out vec2 passTextureCoords;

uniform mat4 transformationMatrix;
uniform vec2 fontLocation;
uniform vec2 size;
uniform vec2 scale;

void main(void)
{
    gl_Position = transformationMatrix * vec4(position , 0.0, 1.0);

    // Offset the texture coordinates based on the image location
    vec2 multiplier = size / scale;

    // Scale and offset the texture coordinates
    texturePosition = (textureCoords * multiplier) - (fontLocation / scale);
}