#version 410 core

in vec2 pass_Position;

uniform vec2 position;

void main()
{
    gl_Position = vec4(position, 0, 0);
    pass_Position = position;
}