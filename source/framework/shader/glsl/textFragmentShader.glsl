#version 400 core

in vec2 textPos;
in vec2 textureCoords;

out vec4 out_Color;

uniform sampler2D guiTexture;
uniform vec2 pos;
uniform vec2 scale;
uniform mat4 transformationMatrix;
uniform vec2 texturePosition;

void main(void)
{
    vec4 transformedPos = transformationMatrix * vec4(textPos, 0, 1);
    vec2 textPos = transformedPos.xy;
    vec2 position = vec2((textureCoords.x * 2) - 1, (textureCoords.y * -2) + 1);
    vec4 color = texture(guiTexture, textureCoords);
    out_Color = color;

}