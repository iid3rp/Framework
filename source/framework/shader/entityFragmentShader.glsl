# version 400 core

const int lightAmount = 10;

uniform sampler2D texture;
uniform sampler2D specular;
uniform float usesSpecular;
uniform vec3 lightColor[lightAmount];
uniform vec3 attenuation[lightAmount];
uniform float damper;
uniform float reflectivity;
uniform vec3 skyColor;
uniform sampler2D shadowMap;

in vec2 passTextureCoordinates;
in vec3 surfaceNormal;
in vec3 lightVector[lightAmount];
in vec3 cameraVector;
in vec4 shadowCoords;
in float visibility;

out vec4 outColor;

// make this uniform soon!!
const float mapSize = 4096;
const float levels = 10000;
const int pcfCount = 4;
const float totalTexels = pow((pcfCount * 2 + 1), 2);

void main(void)
{
    float texelSize = 1 / mapSize;
    float total = 0;

    for(int x = -pcfCount; x <= pcfCount; x++)
    {
        for (int y = -pcfCount; y <= pcfCount; y++)
        {
            float objectNearLight = texture(shadowMap, shadowCoords.xy + vec2(x, y) * texelSize).r;
            if(shadowCoords.z > objectNearLight + 0.002)
            {
                total += 1;
            }
        }
    }

    total /= totalTexels;
    float lightFactor = 1.0 - (total * shadowCoords.w);

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
    totalDiffuse = max(totalDiffuse * lightFactor, 0.3);

    vec4 textureColor = texture(texture, passTextureCoordinates);

    if(textureColor.a < 0.5)
    {
        discard;
    }

    // TODO: make this transitional instead of cutting to two levels :3
    if(usesSpecular > .5)
    {
        vec4 mapInfo = texture(specular, passTextureCoordinates);
        totalSpecular *= mapInfo.r;
        if(mapInfo.g > 0.5)
        {
            totalSpecular = vec3(1);
        }
    }

    outColor = vec4(totalDiffuse, 1.0) * textureColor + vec4(totalSpecular, 1.0);
    outColor = mix(vec4(skyColor, 1.0), outColor, visibility);
}