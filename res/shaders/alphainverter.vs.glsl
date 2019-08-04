#version 440 core

in vec2 position;
in vec2 texCoord;

out vec2 f_texCoord;

void main() {

    f_texCoord = texCoord;

    gl_Position = vec4(position, -0.01f, 1.0f);
}

