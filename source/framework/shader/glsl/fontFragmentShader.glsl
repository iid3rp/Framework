#version 400 core

in vec2 passTextureCoordinates;
in vec2 passLocation;

out vec4 outColor;

uniform vec3 color;
uniform sampler2D fontAtlas;

// implementation of bounds stuff
uniform vec2 size;
uniform vec2 translation;
uniform float lineWidth;


// make this uniform :#
const float width = 0.5;
const float edge = 0.1;

// make this uniform :3
const float borderWidth = 0.4;
const float borderEdge = 0.5;

// make this uniform :3
const vec2 offset = vec2(0, 0);

const vec3 outlineColor = vec3(1, 1, 0);

void main(void)
{
    //taken from the context of gui fragment shader
    float sizeX = size.x <= lineWidth? size.x / lineWidth : 1;
    float sizeY = size.y <= lineWidth? size.y / lineWidth : 1;
    vec2 normSize = vec2(sizeX, sizeY);
    vec2 normTop = vec2((translation.x * 2) - 1, normSize.x * 2 - 1);
    vec2 normBot = vec2((translation.y * -2) + 1, normSize.y * -2 + 1);
    //

    if(passLocation.x >= normTop.x && passLocation.x <= normTop.y &&
        passLocation.y <= normBot.x && passLocation.y >= normBot.y)
    {

        float distance = 1.0 - texture(fontAtlas, passTextureCoordinates).a;
        float alpha = 1.0 - smoothstep(width, width + edge, distance);

        float distance2 = 1.0 - texture(fontAtlas, passTextureCoordinates + offset).a;
        float outlineAlpha = 1.0 - smoothstep(borderWidth, borderWidth + borderEdge, distance2);

        float overallAlpha = alpha + (1.0 - alpha) * outlineAlpha;
        vec3 overallColor = mix(outlineColor, color, alpha / overallAlpha);

        outColor = vec4(overallColor, overallAlpha);
    }
    else outColor = vec4(1);


}