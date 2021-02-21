#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec2 texture;
layout (location=2) in vec3 normal;

out vec2 outTexCoord;
out vec3 modelVertexNormal;
out vec3 modelVertexPosition;

uniform mat4 projection;
uniform mat4 models;

void main()
{
    vec4 modelPosition = models * vec4(position, 1.0);
    gl_Position = projection * modelPosition;

    outTexCoord = texture;
    modelVertexNormal = normalize(models * vec4(normal, 0.0)).xyz;
    modelVertexPosition = modelPosition.xyz;
}
