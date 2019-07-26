#version 440 core

in vec2 position;
in vec2 texCoord;

uniform vec2 resolution;
uniform float depth;

out vec2 f_texCoord;

void main() {

	f_texCoord = texCoord;

	gl_Position = vec4(position / resolution * 2.0f - 1.0f, depth, 1.0f);
}

