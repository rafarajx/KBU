#version 440 core

in vec2 position;
in vec4 color;

uniform vec2 resolution;
uniform vec2 camera;
uniform float depth;

out vec4 f_color;

void main() {

    f_color = color;

    gl_Position = vec4((position + camera) / resolution * 2.0f - 1.0f, depth, 1.0f);

}
