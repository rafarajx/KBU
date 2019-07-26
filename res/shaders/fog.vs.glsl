#version 440 core

in vec2 position;
in float depth;

void main() {



    gl_Position = vec4(position, depth, 1.0f);
}
