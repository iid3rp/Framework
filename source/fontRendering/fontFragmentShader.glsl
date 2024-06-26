#version 330

in vec2 passTextureCoordinates;

out vec4 outColor;

uniform vec3 color;
uniform sampler2D fontAtlas;

void main(void)
{
    outColor = vec4(color, texture(fontAtlas, passTextureCoordinates).a);

}