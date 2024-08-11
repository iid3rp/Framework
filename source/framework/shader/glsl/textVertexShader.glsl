#version 400 core

in vec2 position;

out vec2 textPos;
out vec2 textureCoords;

uniform mat4 transformationMatrix;
uniform mat4 scaleMatrix;
uniform vec2 pos;
uniform vec2 offset;

void main(void)
{
    // putting the final position as the final size itself
    gl_Position = transformationMatrix * vec4(position, 0.0, 1.0);
    textureCoords = vec2((position.x + 1.0) / 2.0, 1 - (position.y + 1.0) / 2.0);
    textPos = position;
}