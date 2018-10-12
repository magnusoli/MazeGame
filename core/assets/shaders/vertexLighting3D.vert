
#ifdef GL_ES
precision mediump float;
#endif

attribute vec3 a_position;
attribute vec3 a_normal;

uniform mat4 u_modelMatrix;
uniform mat4 u_viewMatrix;
uniform mat4 u_projectionMatrix;

uniform vec4 u_eyePosition;

uniform vec4 u_globalAmbient;

uniform vec4 u_lightPosition;
uniform vec4 u_lightColor;
uniform vec4 u_materialDiffuse;
uniform vec4 u_materialSpecular;

uniform float u_materialShininess;

uniform vec4 u_materialEmission;
varying vec4 v_color;

void main()
{
	vec4 position = vec4(a_position.x, a_position.y, a_position.z, 1.0);
	position = u_modelMatrix * position;

	vec4 normal = vec4(a_normal.x, a_normal.y, a_normal.z, 0.0);
	normal = u_modelMatrix * normal;
	
	vec4 v = u_eyePosition - position;
	
	
	vec4 s = u_lightPosition - position;
	
	vec4 h = s + v;
	
	float lambert = max(0,dot(normal, s) / (length(normal)*  length(s)));
	float phong = max(0, dot(normal, h) / (length(normal)*  length(h)));
	
	vec4 diffuseColor = lambert * u_lightColor * u_materialDiffuse;
	vec4 specularColor =  pow(phong, u_materialShininess) * u_lightColor * u_materialSpecular; 
	
	vec4 lightCalcColor = diffuseColor + specularColor;
	v_color =  u_globalAmbient * u_materialDiffuse + lightCalcColor + u_materialEmission;
	
	

	
	position = u_viewMatrix * position;
	//normal = u_viewMatrix * normal;

	//v_color = (dot(normal, vec4(0,0,1,0)) / length(normal)) * u_color;

	gl_Position = u_projectionMatrix * position;
}