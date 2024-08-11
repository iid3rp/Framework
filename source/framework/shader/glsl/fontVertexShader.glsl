#version 330

in vec2 position;
in vec2 textureCoordinates;

out vec2 passTextureCoordinates;
out vec2 passLocation;
out vec2 textureCoords;

uniform vec2 translation;

void main(void)
{
    gl_Position = vec4(position + translation * vec2(2.0, -2.0), 0.0, 1.0);
    passTextureCoordinates = textureCoordinates;
    passLocation = position;
}