# version 400 core

// in vec3 color;
// no use anymore

in vec2 passTextureCoordinates;
in vec3 surfaceNormal;
in vec3 lightVector;
in vec3 cameraVector;
in float visibility;

out vec4 outColor;

uniform sampler2D background;
uniform sampler2D red;
uniform sampler2D green;
uniform sampler2D blue;
uniform sampler2D blendMap;


uniform vec3 lightColor;
uniform float damper;
uniform float reflectivity;
uniform vec3 skyColor;

void main(void)
{
    vec4 blendMapColor = texture(blendMap, passTextureCoordinates);

    float backTextureAmount = 1 - (blendMapColor.r + blendMapColor.g + blendMapColor.b);
    vec2 tiledCoordinates = passTextureCoordinates * 40.0;
    vec4 backgroundTextureColor = texture(background, tiledCoordinates) * backTextureAmount;

    vec4 redColor = texture(red, tiledCoordinates) * blendMapColor.r;
    vec4 greenColor = texture(green, tiledCoordinates) * blendMapColor.g;
    vec4 blueColor = texture(blue, tiledCoordinates) * blendMapColor.b;

    vec4 totalColor = backgroundTextureColor + redColor + greenColor + blueColor;

    vec3 unitNormal = normalize(surfaceNormal);
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

    outColor = vec4(diffuse, 1.0) * totalColor + vec4(finalSpecular, 1.0);
    outColor = mix(vec4(skyColor, 1.0), outColor, visibility);
}