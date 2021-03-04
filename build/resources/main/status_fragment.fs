#version 330

in vec2 outTexCoord;
in vec3 modelVertexPosition;
out vec4 fragColor;

uniform sampler2D sampler;
uniform vec4 color;
uniform int hasTexture;

void main() {
    if (hasTexture == 1)
    {
        fragColor = color * texture(sampler, outTexCoord);
    } else {
        fragColor = color;
    }
}
