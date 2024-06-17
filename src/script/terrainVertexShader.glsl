#version 400 core

in vec3 position;
in vec2 textureCoordinates;
in vec3 normal;

out vec2 passTextureCoordinates;

//out vec3 color;
//apparently the variable is not used

out vec3 surfaceNormal;
out vec3 lightVector;
out vec3 cameraVector;

out float visibility;

uniform mat4 transform;
uniform mat4 projection;
uniform mat4 view;

uniform vec3 lightPosition;

const float density = 0;
const float gradient = 10;

void main(void)
{
    vec4 worldPosition = transform * vec4(position, 1.0);
    vec4 positionRelativeToCamera = view * worldPosition;

    gl_Position =  projection * positionRelativeToCamera;

    //color = vec3(position.x + .5, 1.0, position.y + .5);
    // no use somehow...

    passTextureCoordinates = textureCoordinates;

    surfaceNormal = (transform * vec4(normal, 0.0)).xyz;
    lightVector = lightPosition - worldPosition.xyz;

    cameraVector = (inverse(view) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;

    float distance = length(positionRelativeToCamera.xyz);
    visibility = exp(-pow((distance * density), gradient));
    visibility - clamp(visibility, 0.0, 1.0);

}