#version 410 core

const int lightAmount = 20;

// inside variables (connect)...
in vec3 position;
in vec2 textureCoords;
in vec3 normal;
in vec3 tangent;
// ------------------------------

// outside variables...
out vec2 pass_textureCoords;
out vec3 toLightVector[lightAmount];
out vec3 toCameraVector;
out vec3 surfaceNormal; // just to check if the texture for normal is null, it will depend on the surface instead.
out float visibility;
out vec4 shadowCoords;
// --------------------

// uniform variables...
uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 toShadowMapSpace;
uniform vec3 lightPosition[lightAmount];
uniform float useFakeLighting;
uniform float numberOfRowsInTextureAtlas;
uniform sampler2D normalMap;
uniform vec2 offset;
uniform vec4 plane;
// ---------------------

// constant variables (might be uniform one day)...
const float fogDensity = 1 / 100000;
const float fogGradient = .2;
// -------------------------------------------------

// local variables and initializers...
vec4 worldPosition;
mat4 modelViewMatrix;
vec4 ref = texture(normalMap, pass_textureCoords);
bool hasNormal = ref.r == 0 || ref.g == 0 || ref.b == 0;
mat3 toTangentSpace;
// -----------------------------------

vec4 relativeToCamera()
{
    return hasNormal? modelViewMatrix * vec4(position, 1.0)
                     : viewMatrix * worldPosition;
}

void main(void)
{
    worldPosition = transformationMatrix * vec4(position, 1.0);
    shadowCoords = toShadowMapSpace * worldPosition;

    gl_ClipDistance[1] = dot(worldPosition, plane);

    modelViewMatrix = viewMatrix * transformationMatrix;
    vec4 positionRelativeToCamera = relativeToCamera();
    gl_Position = projectionMatrix * positionRelativeToCamera;


    pass_textureCoords = (textureCoords / numberOfRowsInTextureAtlas) + offset;

    vec3 actualNormal = normal;

    if (useFakeLighting > 0.5) {
        actualNormal = vec3(0.0, 1.0, 0.0); // set this normal to point directly up the y axis
    }

    if(hasNormal)
    {
        surfaceNormal = (modelViewMatrix * vec4(actualNormal, 0.0)).xyz;// swizzle it! get the xyz components from the resulting 4d vector
        vec3 norm = normalize(surfaceNormal);
        vec3 tan = normalize((modelViewMatrix * vec4(tangent, 0.0)).xyz);
        vec3 biTan = normalize(cross(norm, tan));

        toTangentSpace = mat3
        (
            tan.x, biTan.x, norm.x,
            tan.y, biTan.y, norm.y,
            tan.z, biTan.z, norm.z
        );
    }
    else surfaceNormal = (transformationMatrix * vec4(normal, 0.0)).xyz;

    for (int i = 0; i < lightAmount; i++)
    {
        toLightVector[i] = lightPosition[i] - worldPosition.xyz;// world position is a 4d vector. again, use a swizzle to get a 3d vector from it
    }

    // it has a tangent space, hypothetically...
    if(hasNormal)
        toCameraVector = toTangentSpace * (-positionRelativeToCamera.xyz);
    // This shader does not have the "camera" position, but the view matrix
    // position is the inverse of the camera, so the inverse of the view matrix
    // is the camera position. multiply this matrix by an  empty 4d matrix and
    // subract the worldPosition of the vertex gives the distance between them
    else
        toCameraVector = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;

    // Calculate fog
    float distance = length(positionRelativeToCamera.xyz);
    visibility = exp(-pow((distance * fogDensity), fogGradient));
    visibility = clamp(visibility, 0.0, 1.0);
}
