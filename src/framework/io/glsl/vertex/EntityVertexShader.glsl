#version 460 core

// initialize the vertex shader
in vec3 pos;
in vec2 textCoord;

// uniforms
uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

// variables for fragment shader
out vec2 passCoord;

void main(void)
{
    gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(pos, 1);
    passCoord = textCoord;
}