#version 400

//uniforms
uniform mat4 u_projTrans;

// Input values
in vec4 a_position;
in vec2 a_texCoord0;

// Output values
out vec2 FragmentTexcoord;

void main()
{
  gl_Position = u_projTrans * a_position;
  FragmentTexcoord = a_texCoord0;
}