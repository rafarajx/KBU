#version 440 core

in vec2 f_texCoord;

uniform sampler2D tex;
uniform vec4 u_color;

out vec4 color;

void main() {

	vec4 c = texture2D(tex, f_texCoord);
	if(c.a == 1.0f)
		color = u_color;
	else
		discard;
}