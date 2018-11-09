#version 400 core

in vec2 pass_textureCoordinates;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;

out vec4 out_Color;

uniform sampler2D modelTexture;
uniform vec3 lightColour;

uniform float reflectivity;
uniform float shineDamper;

void main(void){

	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitLightVector = normalize(toLightVector);
	vec3 unitCameraVector = normalize(toCameraVector);
	
	vec3 lightDirection = -unitLightVector;
	vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);
	
	float nDotl = dot(unitNormal, unitLightVector);
	float nBrightness = max(nDotl, 0.2);
	
	float specularFactor = dot(reflectedLightDirection, unitCameraVector);
	specularFactor = max (specularFactor, 0.0);	
	float dampedFactor  = pow(specularFactor, shineDamper);
	vec3 finalSpecular = dampedFactor * reflectivity * lightColour;
	
	vec3 diffuse = nBrightness * lightColour;
	
	out_Color = vec4(diffuse,1.0) * texture(modelTexture, pass_textureCoordinates) + vec4(finalSpecular,1.0);
}