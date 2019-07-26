#version 440 core

in vec2 f_texCoord;

uniform sampler2D tex;
uniform vec3 u_color;

out vec4 color;

void main() {

    color = texture2D(tex, f_texCoord);
}