#version 400 core

in vec4 clipSpace;
in vec2 textureCoordinates;
in vec3 toCameraPosition;
in vec3 fromLightVector;
in float distanceToCamera;

out vec4 out_Color;

uniform sampler2D reflection;
uniform sampler2D refraction;
uniform sampler2D duDv;
uniform sampler2D normals;
uniform sampler2D depthMap;
uniform vec3 lightColor;
//uniform float near;
//uniform float far;

uniform float moveFactor;

const float waveStrength = 0.02;
const float shineDamper = 20;
const float reflectivity = .5;

// Function to calculate Fresnel effect
float calculateFresnelEffect(vec3 viewDir, vec3 normal, float distanceToCamera)
{
	float angleFactor = dot(viewDir, normal);
	angleFactor = pow(angleFactor, 2);

	// Increase Fresnel effect based on proximity to water surface
	float proximityFactor = clamp(1.0 - distanceToCamera / 50.0, 0.0, 1.0); // Adjust 50.0 for the desired range
	return mix(angleFactor, 1.0, proximityFactor);
}

void main(void)
{
	vec2 perspective = (clipSpace.xy / clipSpace.w) / 2.0 + 0.5;
	vec2 refractionTextureCoordinates = vec2(perspective.x, perspective.y);
	vec2 reflectionTextureCoordinates = vec2(perspective.x, -perspective.y);

	float near = 1;

	// DID I JUST FIXED A PROBLEM?? NO WAY??
	float far = 100.0;

	float depth;
	depth = texture(depthMap, refractionTextureCoordinates).r;
	float floorDistance = 2.0 * near * far / (far + near - (2.0 * depth - 1.0) * (far - near));

	depth = gl_FragCoord.z;
	float waterDistance = 2.0 * near * far / (far + near - (2.0 * depth - 1.0) * (far - near));
	float waterDepth = floorDistance - waterDistance;

	vec2 distortedTexCoords = texture(duDv, vec2(textureCoordinates.x + moveFactor, textureCoordinates.y)).rg * 0.1;
	distortedTexCoords = textureCoordinates + vec2(distortedTexCoords.x, distortedTexCoords.y + moveFactor);
	vec2 totalDistortion = (texture(duDv, distortedTexCoords).rg * 2.0 - 1.0) * waveStrength * clamp(waterDepth / 20.0, 0.0, 1.0);

	refractionTextureCoordinates += totalDistortion;
	refractionTextureCoordinates = clamp(refractionTextureCoordinates, 0.001, 0.999);

	reflectionTextureCoordinates += totalDistortion;
	reflectionTextureCoordinates.x = clamp(reflectionTextureCoordinates.x, 0.001, 0.999);
	reflectionTextureCoordinates.y = clamp(reflectionTextureCoordinates.y, -0.999, -0.001);

	vec4 reflection = texture(reflection, reflectionTextureCoordinates);
	vec4 refraction = texture(refraction, refractionTextureCoordinates);

	vec4 normalMapColor = texture(normals, distortedTexCoords);
	vec3 normal = vec3(normalMapColor.r * 2.0 - 1.0, normalMapColor.b * 3.0, normalMapColor.g * 2.0 - 1.0);
	normal = normalize(normal);

	vec3 viewFactor = normalize(toCameraPosition);

	// Adjust fresnel effect based on distance to camera
	float fresnelEffect = calculateFresnelEffect(viewFactor, normal, distanceToCamera);

	vec3 reflectedLight = reflect(normalize(fromLightVector), normal);
	float specular = min(dot(reflectedLight, viewFactor), far);
	specular = pow(specular, shineDamper);
	vec3 specularHighlights = lightColor * specular * reflectivity * clamp(waterDepth / 5, 0.0, 1.0);;

	out_Color = mix(reflection, refraction, fresnelEffect);
	out_Color = mix(out_Color, vec4(0, 0.3, 1, 1.0), 0.2) + vec4(specularHighlights, 0.0);
	out_Color.a = clamp(waterDepth / 2, 0.0, 1.0);

	//out_Color = vec4(1);
}