# version 400 core

const int lightAmount = 10;

uniform sampler2D textureSampler;
uniform vec3 lightColor[lightAmount];
uniform vec3 attenuation[lightAmount];
uniform float damper;
uniform float reflectivity;

in vec2 passTextureCoordinates;
in vec3 surfaceNormal;
in vec3 lightVector[lightAmount];
in vec3 cameraVector;

out vec4 outColor;

const float levels = 10;

void main(void)
{
    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitVectorToCamera = normalize(cameraVector);

    vec3 totalDiffuse = vec3(0.0);
    vec3 totalSpecular = vec3(0.0);

    for(int i = 0; i < lightAmount; i++)
    {
        float distance = length(lightVector[i]);
        float setFactor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].z * pow(distance, 2));

        vec3 unitLightVector = normalize(lightVector[i]);
        float numberDot1 = dot(unitNormal, unitLightVector);

        float brightness = max(numberDot1, 0.0);
        float level = floor(brightness * levels);
        brightness = level / levels;

        vec3 lightDirection = -unitLightVector;
        vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);
        float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
        specularFactor = max(specularFactor, 0.0);

        float dampFactor = pow(specularFactor, damper);
        level = floor(dampFactor * levels);
        dampFactor = level / levels;

        totalDiffuse = totalDiffuse + (brightness * lightColor[i]) / setFactor;
        totalSpecular = totalSpecular + (dampFactor * reflectivity * lightColor[i]) / setFactor;
    }
    totalDiffuse = max(totalDiffuse, 0.2);

    vec4 textureColor = texture(textureSampler, passTextureCoordinates);

    if(textureColor.a < 0.5)
    {
        discard;
    }
    outColor = vec4(totalDiffuse, 1.0) * textureColor + vec4(totalSpecular, 1.0);
}