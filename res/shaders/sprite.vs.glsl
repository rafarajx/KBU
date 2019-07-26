#version 440 core

in vec2 position;
in vec2 texCoord;
in float depth;

uniform vec2 resolution;
uniform vec2 camera;

out vec2 f_texCoord;
out float f_depth;

void main() {

	f_texCoord = texCoord;

	vec2 xy = (position + camera) / resolution * 2.0f - 1.0f;

	gl_Position = vec4(xy, depth, 1.0f);
}