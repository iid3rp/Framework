#version 410 core

const int lightAmount = 20;

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector[lightAmount];
in vec3 toCameraVector;
in float visibility;

layout(location = 0) out vec4 out_Color;

uniform sampler2D backgroundTexture;
uniform sampler2D rTexture;
uniform sampler2D gTexture;
uniform sampler2D bTexture;
uniform sampler2D blendMap;

uniform vec3 lightColor[lightAmount];
uniform vec3 attenuation[lightAmount];
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColor;

void main(void) {

    // multitexturing
    vec4 blendMapColor = texture(blendMap, pass_textureCoords);
    float backTextureAmount = 1 - (blendMapColor.r + blendMapColor.g + blendMapColor.b);
    vec2 tiledCoordinates = pass_textureCoords * 40.0;
    vec4 backgroundTextureColor = texture(backgroundTexture, tiledCoordinates) * backTextureAmount;
    vec4 rTextureColor = texture(rTexture, tiledCoordinates) * blendMapColor.r;
    vec4 gTextureColor = texture(gTexture, tiledCoordinates) * blendMapColor.g;
    vec4 bTextureColor = texture(bTexture, tiledCoordinates) * blendMapColor.b;
    vec4 totalColor = backgroundTextureColor + rTextureColor + gTextureColor + bTextureColor;

    vec3 unitNormal = normalize(surfaceNormal); // normalize makes the size of the vector = 1. Only direction of the vector matters here. Size is irrelevant
    vec3 unitVectorToCamera = normalize(toCameraVector);
    vec3 totalDiffuse = vec3(0.0);
    vec3 totalSpecular = vec3(0.0);

    for(int i = 0; i < lightAmount; i++)
    {
        float distance = length(toLightVector[i]);
        float setFactor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].z * pow(distance, 2));

        vec3 unitLightVector = normalize(toLightVector[i]);
        float nDot1 = dot(unitNormal, unitLightVector); // dot product calculation of 2 vectors. nDotl is how bright this pixel should be. difference of the position and normal vector to the light source

        float brightness = max(nDot1, 0); // clamp the brightness result value to between 0 and 1. values less than 0 are clamped to 0.2. to leave a little more diffuse light
        vec3 diffuse = brightness * lightColor[i]; // calculate final color of this pixel by how much light it has

        vec3 lightDirection = -unitLightVector; // light direction vector is the opposite of the toLightVector
        vec3 reflectedLightDirection = reflect(lightDirection, unitNormal); // specular reflected light vector

        float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);    // determines how bright the specular light should be relative to the "camera" by taking the dot product of the two vectors
        specularFactor = max(specularFactor, 0.0);
        float dampFactor = pow(specularFactor, shineDamper);

        // raise specularFactor to the power of the shineDamper value. makes the low specular values even lower but doesnt effect the high specular values too much
        totalDiffuse = totalDiffuse + (brightness * lightColor[i]) / setFactor;
        totalSpecular = totalSpecular + (dampFactor * reflectivity * lightColor[i]) / setFactor;
    }
    // for shadows later :))
    //totalDiffuse = max(totalDiffuse * lightFactor, 0.5);
    totalDiffuse = max(totalDiffuse, 0.5);

    out_Color = vec4(totalDiffuse, 1.0) *  totalColor + vec4(totalSpecular, 1.0);        // returns color of the pixel from the texture at specified texture coordinates
    out_Color = mix(vec4(skyColor, 1.0), out_Color, visibility);
}
