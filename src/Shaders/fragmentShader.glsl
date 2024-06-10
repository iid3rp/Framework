# version 400 core

// in vec3 color;
// no use anymore

in vec2 passTextureCoordinates;
in vec3 surfaceNormal;
in vec3 lightVector;

out vec4 outColor;

uniform sampler2D textureSampler;
uniform vec3 lightColor;

void main(void)
{
    vec3  unitNormal = normalize(surfaceNormal);
    vec3 unitLightVector = normalize(lightVector);

    float numberDot1 = dot(unitNormal, unitLightVector);
    float brightness = max(numberDot1, 0.0);

    vec3 diffuse = brightness * lightColor;

    outColor = vec4(diffuse, 1.0) * texture(textureSampler, passTextureCoordinates);
}