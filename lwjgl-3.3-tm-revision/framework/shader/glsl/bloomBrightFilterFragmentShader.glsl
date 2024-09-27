#version 150

// local variable
vec4 color = vec4(0);
float brightness = 0;

in vec2 textureCoords;

out vec4 out_Colour;

uniform sampler2D colourTexture;
uniform int luma;

// the constants for the luma effect:
const int Y_UV = 1;
const int ITU_R_BT_709 = 2;
const int ITU_R_BT_2100 = 3;
const int COLOR_LENGTH = 4;
const int COLOR_IN_THIRDS = 5;

vec4 getEdgeColor(float edge)
{
    if(brightness > edge)
    {
        return color;
    }
    else return vec4(0);
}

vec4 getTransitionalColor(void)
{
    return color * brightness;
}

// some code in this method comes from a citation
// from a wikipedia source.  |  Grayscale!
// https://en.wikipedia.org/wiki/Grayscale#Luma_coding_in_video_systems
float setLuma(int arg)
{
    switch(arg)
    {
        case Y_UV:
            return (color.r * 0.299) + (color.g * 0.587) + (color.b * 0.114);
        case ITU_R_BT_709:
            return (color.r * 0.2126) + (color.g * 0.7152) + (color.b * 0.0722);
        case ITU_R_BT_2100:
            return (color.r * 0.2627) + (color.g * 0.6780) + (color.b * 0.0593);
        case COLOR_LENGTH:
            return length(color);
        case COLOR_IN_THIRDS:
            return (color.r + color.g + color.b) / 3;
    }
    return length(color);
}

void main(void)
{
    color = texture(colourTexture, textureCoords);
    brightness = setLuma(COLOR_IN_THIRDS);
    //out_Colour = getTransitionalColor();
    out_Colour = getEdgeColor(.45f);
}