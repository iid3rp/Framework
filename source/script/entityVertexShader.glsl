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

uniform vec4 plane;

void main(void)
{
    vec4 worldPosition = transform * vec4(position, 1.0);

    gl_ClipDistance[3] = dot(worldPosition, plane);

    gl_Position =  projection * view * worldPosition;

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

}