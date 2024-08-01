#version 410 core

in vec2 pass_Position;
out vec4 outColor;

uniform sampler2D texture;
uniform vec2 textureSize; // Size of the texture

void main()
{
    // Convert pass_Position from pixel coordinates to normalized texture coordinates
    vec2 texCoords = pass_Position / textureSize;

    // Sample the texture using the normalized texture coordinates
    vec4 color = texture(texture, texCoords);
    outColor = color;
}