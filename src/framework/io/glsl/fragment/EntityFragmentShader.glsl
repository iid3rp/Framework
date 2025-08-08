#version 410 core
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
in vec2 passTex;
in vec3 surfaceNorm;
in vec4 worldPos;
in vec3 toCamera;

// uniforms
uniform sampler2D modelTexture;
uniform vec4 backgroundColor;
uniform bool hasTexture;
uniform Light lights[MAX_LIGHTS];
uniform int lightCount;
uniform float ambient;
uniform Material material;

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

vec3 highlight(vec3 normal, int lightIndex)
{
    vec3 unitCamera = normalize(toCamera);
    vec3 reflectLight = reflect(-unitCamera, normal);

    float specular = max(dot(reflectLight, unitCamera), 0);
    float damp = pow(specular, material.shineDamp);

    return damp * lights[lightIndex].color;
}

vec4 processLighting(void)
{
    vec3 unitLightVector;
    vec3 diffuse;
    for(int i = 0; i < 1; i++)
    {
        vec3 unitLightVector = normalize(lights[i].pos - worldPos.xyz);
        vec3 unitNormal = normalize(surfaceNorm);

        float lightDot = dot(unitNormal, unitLightVector);
        float brightness = max(lightDot, .2);

        diffuse += (brightness * normalize(lights[i].color)) + highlight(unitNormal, i) * material.reflectivity;
    }
    return vec4(diffuse, 1);
}

void main(void)
{
    outColor = paint(hasTexture);
    outColor *= processLighting();
}