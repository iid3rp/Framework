#version 330

in vec2 passTextureCoordinates;

out vec4 outColor;

uniform vec3 color;
uniform sampler2D fontAtlas;


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
    float distance = 1.0 - texture(fontAtlas, passTextureCoordinates).a;
    float alpha = 1.0 - smoothstep(width, width + edge, distance);

    float distance2 = 1.0 - texture(fontAtlas, passTextureCoordinates + offset).a;
    float outlineAlpha = 1.0 - smoothstep(borderWidth, borderWidth + borderEdge, distance2);

    float overallAlpha = alpha + (1.0 - alpha) * outlineAlpha;
    vec3 overallColor = mix(outlineColor, color, alpha / overallAlpha);

    outColor = vec4(overallColor, overallAlpha);


}