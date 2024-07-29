#version 400

in vec3 textureCoords;

layout(location = 0) out vec4 outColor;
layout(location = 1) out vec4 brightColor;

uniform samplerCube cubeMap1;
uniform samplerCube cubeMap2;
uniform float blendFactor;

const float lowerLimit = -200.0;
const float upperLimit = -100.0;
const float levels = 10000;


void main(void)
{
    vec4 texture1 = texture(cubeMap1, textureCoords);
    vec4 texture2 = texture(cubeMap2, textureCoords);
    vec4 finalColor = mix(texture1, texture2, blendFactor);

    float amount = (finalColor.r + finalColor.g + finalColor.b) / 3.0;
    amount = floor(amount * levels) / levels;

    outColor = finalColor;
    brightColor = finalColor;
}