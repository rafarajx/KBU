#version 440 core

out vec4 color;

uniform vec2 point;
uniform vec2 resolution;

void main() {

    vec4 black = vec4(0.0f, 0.0f, 0.0f, 1.0f);
    vec4 transparent = vec4(0.0f);

    if (length(point - gl_FragCoord.xy * resolution / vec2(1000, 800)) < 150f) {
        color = black;
    } else {
        color = transparent;
    }


}
