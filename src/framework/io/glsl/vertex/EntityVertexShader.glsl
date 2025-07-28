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

struct Matrix {
    mat4 transform;
    mat4 projection;
    mat4 view;
};

// initialize the vertex shader
in vec3 pos;
in vec2 tex;
in vec3 normal;

// uniforms
uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform Light lights[MAX_LIGHTS];


// variables for fragment shader
out vec2 passTex;
out vec3 surfaceNorm;
out vec3 toLight;

// global variables
vec4 worldPos;

vec4 worldPosition(void)
{
    return transformationMatrix * vec4(pos, 1);
}

vec4 clipPosition(void)
{
    return projectionMatrix * viewMatrix * worldPos;
}

vec3 surface(void)
{
    return (transformationMatrix * vec4(normal, 0)).xyz;
}

vec3 lightVector(void)
{
    return lights[0].pos - worldPos.xyz;
}

void main(void)
{
    worldPos = worldPosition();
    gl_Position = clipPosition();

    passTex = tex;
    surfaceNorm = surface();
    toLight = lightVector();
}