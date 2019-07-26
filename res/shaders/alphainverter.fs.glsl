#version 440 core

in vec2 f_texCoord;

uniform sampler2D tex;

out vec4 color;

void main() {

    vec4 tex = texture2D(tex, f_texCoord);

    vec4 black = vec4(0.0f, 0.0f, 0.0f, 1.0f);
    vec4 transparent = vec4(0.0f);
    vec4 red = vec4(1.0f, 0.0f, 0.0f, 1.0f);


    if (tex == black) {
        discard;
    } else {
        color = black;
    }

    //tex.a = 1.0f - tex.a;

    //color = vec4(1.0f);
}