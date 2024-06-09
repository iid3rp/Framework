#version 400 core

in vec3 position;
in vec2 textureCoordinates;

out vec2 passTextureCoordinates;

//out vec3 color;
//apparently the variable is not used

uniform mat4 transform;
uniform mat4 projection;
uniform mat4 view;

void main(void)
{
    gl_Position =  projection * view * transform * vec4(position.x, position.y , position.z, 1.0);

    //color = vec3(position.x + .5, 1.0, position.y + .5);
    // no use somehow...

    passTextureCoordinates = textureCoordinates;

}