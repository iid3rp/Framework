#version 460 core
#define MAX_LIGHTS 100 // Define your maximum number of lights here

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

    // matrices
    mat4 lightSpaceMatrix;
};

struct Texture {
    sampler2D diffuse;
    sampler2D normal;
    sampler2D specular;
    bool hasDiffuse;
    bool hasNormal;
    bool hasSpecular;
};

struct Material {
    float shineDamp;
    float reflectivity;
};

// from the vertex shader
in toFrag {
    vec2 tex;
    vec3 surface[MAX_LIGHTS];
    vec3 toLight[MAX_LIGHTS];
} fin;

in vec2 passTex;
in vec3 surfaceNorm;
in vec3 toLight;

// uniforms
uniform sampler2D modelTexture;
uniform vec4 backgroundColor;
uniform bool hasTexture;
uniform Light lights[MAX_LIGHTS];
uniform int lightCount;
uniform float ambient;

// output color (final)
out vec4 outColor;

// global variables

vec4 paint(bool flag)
{
    if(flag)
        return texture(modelTexture, passTex);
    else
        return normalize(backgroundColor);
}

vec4 processLighting(void)
{
//    for(int i = 0; i < lightCount; i++)
//    {
//
//    }
    vec3 unitNormal = normalize(surfaceNorm);
    vec3 unitLightVector = normalize(toLight);

    float lightDot = dot(unitNormal, unitLightVector);
    float brightness = max(lightDot, 0); // should be ambient here
    vec3 diffuse = brightness * normalize(lights[0].color);

    return vec4(diffuse, 1);
}

void main(void)
{
    outColor = paint(hasTexture);
    outColor *= processLighting();
}