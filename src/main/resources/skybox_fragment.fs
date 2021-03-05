#version 330

in vec2 outTexCoord;
in vec3 modelVertexPosition;
out vec4 fragColor;

uniform sampler2D sampler;
uniform vec3 ambient;

void main()
{
    fragColor = vec4(ambient, 1) * texture(sampler, outTexCoord);
}
