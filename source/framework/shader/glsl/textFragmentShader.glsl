#version 400 core

in vec2 texturePosition;
in vec2 passTextureCoords;

out vec4 out_Color;

uniform sampler2D guiTexture;
uniform float width;
uniform float edge;
uniform float borderWidth;
uniform float borderEdge;
uniform vec2 offset;
uniform vec3 outlineColor;
uniform vec3 foregroundColor;

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

    float distance = 1.0 - texture(guiTexture, texturePosition).a;
    float alpha = 1.0 - smoothstep(width, width + edge, distance);

    float distance2 = 1.0 - texture(guiTexture, texturePosition + offset).a;
    float outlineAlpha = 1.0 - smoothstep(borderWidth, borderWidth + borderEdge, distance2);

    float overallAlpha = alpha + (1.0 - alpha) * outlineAlpha;
    vec3 overallColor = mix(outlineColor, foregroundColor, (alpha / overallAlpha));

    out_Color = vec4(overallColor, overallAlpha);

}