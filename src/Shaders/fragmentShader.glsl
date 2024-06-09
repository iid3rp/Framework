# version 400 core

// in vec3 color;
// no use anymore

in vec2 passTextureCoordinates;

out vec4 outColor;

uniform sampler2D textureSampler;

void main(void)
{
    outColor = texture(textureSampler, passTextureCoordinates);
}