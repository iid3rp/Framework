#version 150

out vec4 outColor;

in vec2 blurTextureCoords[11];

uniform sampler2D originalTexture;

void main(void){

	outColor = vec4(0.0);
	outColor += texture(originalTexture, blurTextureCoords[0]) * 0.0093;
    outColor += texture(originalTexture, blurTextureCoords[1]) * 0.028002;
    outColor += texture(originalTexture, blurTextureCoords[2]) * 0.065984;
    outColor += texture(originalTexture, blurTextureCoords[3]) * 0.121703;
    outColor += texture(originalTexture, blurTextureCoords[4]) * 0.175713;
    outColor += texture(originalTexture, blurTextureCoords[5]) * 0.198596;
    outColor += texture(originalTexture, blurTextureCoords[6]) * 0.175713;
    outColor += texture(originalTexture, blurTextureCoords[7]) * 0.121703;
    outColor += texture(originalTexture, blurTextureCoords[8]) * 0.065984;
    outColor += texture(originalTexture, blurTextureCoords[9]) * 0.028002;
    outColor += texture(originalTexture, blurTextureCoords[10]) * 0.0093;

}