#version 400 core

const int lightAmount = 10;

uniform mat4 transform;
uniform mat4 projection;
uniform mat4 view;
uniform vec3 lightPosition[lightAmount];
uniform float useFakeLighting;
uniform float numberOfRows;
uniform vec2 offset;

in vec3 position;
in vec2 textureCoordinates;
in vec3 normal;

out vec2 passTextureCoordinates;
out vec3 surfaceNormal;
out vec3 lightVector[lightAmount];
out vec3 cameraVector;
out float visibility;
out vec4 shadowCoords;

uniform vec4 plane;
uniform mat4 toShadowMapSpace;

const float density = 0;
const float gradient = 100000;
const float shadowDistance = 150.0;
const float transitionDistance = 100.0;

void main(void)
{
    vec4 worldPosition = transform * vec4(position, 1.0);
    vec4 positionRelativeToCamera = view * worldPosition;
    gl_Position =  projection * positionRelativeToCamera;

    shadowCoords = toShadowMapSpace * worldPosition;

    gl_ClipDistance[3] = dot(worldPosition, plane);

    vec3 model = normal;
    if(useFakeLighting > 0.5)
    {
        model = vec3(0.0, 1, 0.0);
    }

    passTextureCoordinates = (textureCoordinates / numberOfRows) + offset;

    surfaceNormal = (transform * vec4(normal, 0.0)).xyz;

    for(int i = 0; i < lightAmount; i++)
    {
        lightVector[i] = lightPosition[i] - worldPosition.xyz;
    }

    cameraVector = (inverse(view) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;

    float distance = length(positionRelativeToCamera.xyz);
    visibility = exp(-pow((distance * density), gradient));
    visibility = clamp(visibility, 0.0, 1.0);

    distance = distance - (shadowDistance - transitionDistance);
    distance = distance / transitionDistance;
    shadowCoords.w = clamp(1.0 - distance, 0.0, 1.0);

}