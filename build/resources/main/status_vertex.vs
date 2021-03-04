#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec2 texture;
layout (location=2) in vec3 vertexNormal;

out vec2 outTexCoord;

uniform mat4 matrix;

void main()
{
    gl_Position = matrix * vec4(position, 1.0);
    outTexCoord = texture;
}
