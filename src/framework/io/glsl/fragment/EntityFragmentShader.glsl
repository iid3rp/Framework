#version 460 core

// structures
struct Light {
    vec3 pos;
    vec3 color;
    float intensity;

    // attenuation
    float constant;
    float linear;
    float quadratic;
    float distance; // < 0 means infinite distance
};

// from the vertex shader
in vec2 passCoord;

// uniforms
uniform vec4 backgroundColor;
uniform bool hasTexture;
uniform sampler2D texture;
uniform Light[] lights;

// output color (final)
out vec4 outColor;

vec4 paint(bool flag)
{
    if(flag)
        return texture(texture, passCoord);
    else
        return backgroundColor / 255;
}

void main(void)
{
    outColor = paint(hasTexture);
    outColor = vec4(1);
}

