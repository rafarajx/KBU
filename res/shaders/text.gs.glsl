#version 440 core

layout(points) in;
layout(triangle_strip, max_vertices = 4) out;

uniform vec2 resolution;

out vec2 f_texCoord;

void main() {
	gl_Position = vec4((position + vec2(0, 0)) / resolution - 1.0f, 0.0, 0.0);
	f_texCoord = vec2(0.0f, 0.0f);
	EmitVertex();

	gl_Position = vec4((position + vec2(8, 0)) / resolution - 1.0f, 0.0, 0.0);
	f_texCoord = vec2(1.0f, 0.0f);
	EmitVertex();

	gl_Position = vec4((position + vec2(8, 11)) / resolution - 1.0f, 0.0, 0.0);
	f_texCoord = vec2(1.0f, 1.0f);
	EmitVertex();

	gl_Position = vec4((position + vec2(0, 11)) / resolution - 1.0f, 0.0, 0.0);
	f_texCoord = vec2(0.0f, 1.0f);
	EmitVertex();

	EndPrimitive();
}