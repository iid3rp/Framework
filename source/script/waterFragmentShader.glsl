#version 400 core

in vec4 clipSpace;
in vec2 textureCoordinates;
in vec3 toCameraPosition;
in vec3 fromLightVector;

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

void main(void)
{
	vec2 perspective = (clipSpace.xy / clipSpace.w) / 2.0 + 0.5;
	vec2 refractionTextureCoordinates = vec2(perspective.x, perspective.y);
	vec2 reflectionTextureCoordinates = vec2(perspective.x, -perspective.y);

	float near = 0.1;

	// DID I JUST FIXED A PROBLEM?? NO WAY??
	float far = 1000.0;

	float depth = texture(depthMap, refractionTextureCoordinates).r;
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
	float refractionFactor = dot(viewFactor, normal);
	refractionFactor = pow(refractionFactor, 2);

	vec3 reflectedLight = reflect(normalize(fromLightVector), normal);
	float specular = max(dot(reflectedLight, viewFactor), 0.0);
	specular = pow(specular, shineDamper);
	vec3 specularHighlights = lightColor * specular * reflectivity * clamp(waterDepth / 5, 0.0, 1.0);;

	out_Color = mix(reflection, refraction, refractionFactor);
	out_Color = mix(out_Color, vec4(0, 0.3, 0.5, 1.0), 0.2) + vec4(specularHighlights, 0.0);
	out_Color.a = clamp(waterDepth / 5, 0.0, 1.0);
}