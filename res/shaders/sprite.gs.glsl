#version 440 core

layout(points) in;
layout(triangle_strip, max_vertices = 6) out;

void bla(vec4 position){

    gl_Position = position + vec4(-1.0f, -1.0f, 0.0f, 0.0f);
    EmitVertex();
    gl_Position = position + vec4(1.0f, -1.0f, 0.0f, 0.0f);
    EmitVertex();
    gl_Position = position + vec4(1.0f, 1.0f, 0.0f, 0.0f);
    EmitVertex();
    gl_Position = position + vec4(-1.0f, -1.0f, 0.0f, 0.0f);
    EmitVertex();

    EndPrimitive();
}


void main() {

    bla(gl_in[0].gl_Position);


}
