#version 440 core

in vec2 f_texCoord;

uniform sampler2D tex;

out vec4 color;

void main() {

	vec4 c = texture2D(tex, f_texCoord);

	if(c.a < 0.01f)
		discard;
	color = c;
}