#version 410 core

const int lightAmount = 20;

in vec2 pass_textureCoords;
in vec3 toLightVector[lightAmount];
in vec3 surfaceNormal;
in vec3 toCameraVector;
in float visibility;
in vec4 shadowCoords;

layout(location = 0) out vec4 out_Color;
layout(location = 1) out vec4 brightColor;
layout(location = 2) out vec4 mouseEvent;

uniform sampler2D modelTexture;
uniform sampler2D shadowMap;
uniform sampler2D normalMap;
uniform sampler2D specularMap;
uniform bool hasSpecularMap;

uniform vec3 lightColor[lightAmount];
uniform vec3 attenuation[lightAmount];
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColor;
uniform vec4 mouseEventColor;
uniform vec4 highlightColor;

// make this uniform below:
const float levels = 255;
const int pcfCount = 4;
const float totalTexels = pow((pcfCount * 2 + 1), 2);
const float mapSize = 2048;

// hypothetically we have some kind of normality value
const float normality = 1;

// local variable
vec4 normalMapValue;

vec3 normalTexture()
{
    normalMapValue = texture(normalMap, pass_textureCoords);

    if(normalMapValue.r == 0 || normalMapValue.g == 0 || normalMapValue.b == 0)
        return normalize(surfaceNormal);

    normalMapValue = ((normalMapValue * 2) - 1) * normality;
    return normalize(normalMapValue.rgb);
}

void main(void)
{
    float texelSize = 1 / mapSize;
    float total = 0;

    for(int x = -pcfCount; x <= pcfCount; x++)
    {
        for (int y = -pcfCount; y <= pcfCount; y++)
        {
            float objectNearLight = texture(shadowMap, shadowCoords.xy + vec2(x, y) * texelSize).r;
            if(shadowCoords.z > objectNearLight)
            {
                total += 1;
            }
        }
    }

    total /= totalTexels;
    float lightFactor = 1; //1.0 - (total * shadowCoords.w * .75);

    vec3 unitNormal = normalTexture(); // normalize makes the size of the vector = 1. Only direction of the vector matters here. Size is irrelevant
    vec3 unitVectorToCamera = normalize(toCameraVector);

    vec3 totalDiffuse = vec3(0.0);
    vec3 totalSpecular = vec3(0.0);

    for(int i = 0; i < lightAmount; i++)
    {
        if(lightColor[i] == vec3(0, 0, 0))
            continue;

        float distance = length(toLightVector[i]);
        float setFactor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].z * pow(distance, 2));

        vec3 unitLightVector = normalize(toLightVector[i]);
        float nDotl = dot(unitNormal, unitLightVector);// dot product calculation of 2 vectors. nDotl is how bright this pixel should be. difference of the position and normal vector to the light source

        // cel-shading technique (brightness)
        float brightness = max(nDotl, 0); // clamp the brightness result value to between 0 and 1. values less than 0 are clamped to 0.2. to leave a little more diffuse light
        float level = floor(brightness * levels);
        brightness = level / levels;

        vec3 lightDirection = -unitLightVector; // light direction vector is the opposite of the toLightVector
        vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);// specular reflected light vector
        float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);// determines how bright the specular light should be relative to the "camera" by taking the dot product of the two vectors
        specularFactor = max(specularFactor, 0.0);

        // cel-shading technique (darkness)
        float dampedFactor = pow(specularFactor, shineDamper);// raise specularFactor to the power of the shineDamper value. makes the low specular values even lower but doesnt effect the high specular values too much
        level = floor(dampedFactor * levels);
        dampedFactor = level / levels;

        totalDiffuse = totalDiffuse + (brightness * lightColor[i]) / setFactor;// calculate final color of this pixel by how much light it has
        totalSpecular = totalSpecular + (dampedFactor * reflectivity * lightColor[i]) / setFactor;
    }
    // for shadows later :))
    totalDiffuse = max(totalDiffuse, 0.5) * lightFactor;


    vec4 textureColor = texture(modelTexture, pass_textureCoords);

    if (textureColor.a < 0.5) {
        discard;
    }

    // TODO: make this transitional instead of cutting to two levels :3

    brightColor = vec4(0);
    if(hasSpecularMap)
    {
        vec4 mapInfo = texture(specularMap, pass_textureCoords);
        totalSpecular *= mapInfo.r;
        if(mapInfo.g > 0.5)
        {
            brightColor = textureColor + vec4(totalSpecular, 1);
            totalSpecular = vec3(1);
        }
    }

    out_Color = vec4(totalDiffuse, 1.0) *  textureColor + vec4(totalSpecular, 1.0); // returns color of the pixel from the texture at specified texture coordinates
    out_Color = mix(vec4(skyColor, 1.0), out_Color, visibility);
    out_Color = out_Color + highlightColor;
    mouseEvent = mouseEventColor;
    //brightColor = out_Color;
    //out_Color = normalMapValue;

}
