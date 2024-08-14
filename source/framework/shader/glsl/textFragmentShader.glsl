#version 400 core

in vec2 texturePosition;
in vec2 passTextureCoords;

out vec4 out_Color;

uniform sampler2D guiTexture;

// make this uniform :#
const float width = 0.5;
const float edge = 0.1;

// make this uniform :3
const float borderWidth = 0;
const float borderEdge = 0;

// make this uniform :3
const vec2 offset = vec2(0, 0);

const vec3 outlineColor = vec3(1, 1, 0);

bool bound()
{
    return !(texturePosition.x < 0.0 || texturePosition.x > 1.0 ||
    texturePosition.y < 0.0 || texturePosition.y > 1.0);
}

void main(void)
{
    if(bound())
    {
        vec4 color = texture(guiTexture, texturePosition);
        out_Color = color;
    }
    else out_Color = vec4(1);

    vec3 color = vec3(1);

    float distance = 1.0 - texture(guiTexture, texturePosition).a;
    float alpha = 1.0 - smoothstep(width, width + edge, distance);

    float distance2 = 1.0 - texture(guiTexture, texturePosition + offset).a;
    float outlineAlpha = 1.0 - smoothstep(borderWidth, borderWidth + borderEdge, distance2);

    float overallAlpha = alpha + (1.0 - alpha) * outlineAlpha;
    vec3 overallColor = mix(outlineColor, color, (alpha / overallAlpha));

    out_Color = vec4(overallColor, overallAlpha);

}