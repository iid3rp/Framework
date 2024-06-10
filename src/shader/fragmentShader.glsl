# version 400 core

// in vec3 color;
// no use anymore

in vec2 passTextureCoordinates;
in vec3 surfaceNormal;
in vec3 lightVector;
in vec3 cameraVector;

out vec4 outColor;

uniform sampler2D textureSampler;
uniform vec3 lightColor;

uniform float damper;
uniform float reflectivity;

void main(void)
{
    vec3  unitNormal = normalize(surfaceNormal);
    vec3 unitLightVector = normalize(lightVector);

    float numberDot1 = dot(unitNormal, unitLightVector);
    float brightness = max(numberDot1, 0.2);

    vec3 diffuse = brightness * lightColor;

    vec3 unitVectorToCamera = normalize(cameraVector);
    vec3 lightDirection = -unitLightVector;
    vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);

    float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
    specularFactor = max(specularFactor, 0.0);
    float dampFactor = pow(specularFactor, damper);
    vec3 finalSpecular = dampFactor * reflectivity * lightColor;

    outColor = vec4(diffuse, 1.0) * texture(textureSampler, passTextureCoordinates) + vec4(finalSpecular, 1.0);
}