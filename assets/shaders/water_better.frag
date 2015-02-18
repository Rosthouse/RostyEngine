#version 400

// Uniform values
uniform sampler2D u_texture;
uniform float Time;
uniform float WaveSpeed;
uniform float Amplitude;

// Input values
in vec2 FragmentTexcoord;

// Output values
out vec4 OutputColor;

float verticalDistortion(vec2 uv){
  return (cos((uv.y + (Time * 0.04 * WaveSpeed)) * 45.0) * Amplitude * 0.002) +
    (cos((uv.y + (Time * 0.1 * WaveSpeed)) * 10.0) *  Amplitude* 0.002);
}

float horizontalDistortion(vec2 uv){
  return (sin((uv.y + (Time * 0.07* WaveSpeed)) * 15.0) *  Amplitude * 0.002) +
    (sin((uv.y + (Time * 0.1* WaveSpeed)) * 15.0) *  Amplitude* 0.002);
}


void main()
{
  vec2 uv = FragmentTexcoord.xy;
  
  uv.y += verticalDistortion(uv);
  uv.x += horizontalDistortion(uv);  

  vec4 texColor = texture2D(u_texture, uv);
  OutputColor = texColor;
}