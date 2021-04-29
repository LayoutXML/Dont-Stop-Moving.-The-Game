#version 330

const int MAX_POINT_LIGHTS = 10;
const int MAX_SPOT_LIGHTS = 10;

in vec2 outTexCoord;
in vec3 modelVertexNormal;
in vec3 modelVertexPosition;

out vec4 fragColor;

struct Attenuation {
    float constant;
    float linear;
    float exponent;
};

struct PointLight {
    vec3 color;
    vec3 position;
    float intensity;
    Attenuation attenuation;
};

struct SpotLight {
    PointLight pointLight;
    vec3 direction;
    float cutoff;
};

struct DirectionalLight {
    vec3 color;
    vec3 direction;
    float intensity;
};

struct Material {
    vec4 ambient;
    vec4 diffuse;
    vec4 specular;
    int hasTexture;
    float reflectance;
};

uniform sampler2D sampler;
uniform float specular;
uniform vec3 ambient;
uniform Material material;
uniform PointLight pointLights[MAX_POINT_LIGHTS];
uniform SpotLight spotLights[MAX_SPOT_LIGHTS];
uniform DirectionalLight directionalLight;

vec4 ambientColor;
vec4 diffuseColor;
vec4 specularColor;

void setTextureColors(vec2 textureCoordinates) {
    ambientColor = texture(sampler, textureCoordinates);
    diffuseColor = ambientColor;
    specularColor = ambientColor;
}

void setMaterialColors(Material material) {
    ambientColor = material.ambient;
    diffuseColor = material.diffuse;
    specularColor = material.specular;
}

void setColors(Material material, vec2 textureCoordinates) {
    if (material.hasTexture == 1) {
        setTextureColors(textureCoordinates);
    } else {
        setMaterialColors(material);
    }
}

vec4 getDiffuseColor(vec3 color, float intensity, vec3 lightDirection, vec3 normal) {
    float factor = max(dot(normal, lightDirection), 0.0);
    return diffuseColor * vec4(color, 1.0) * intensity * factor;
}

vec4 getSpecularColor(vec3 color, float intensity, vec3 position, vec3 lightDirection, vec3 normal) {
    vec3 cameraDirection = normalize(-position);
    vec3 reflection = normalize(reflect(-lightDirection, normal));
    float factorSpecular = pow(max(dot(cameraDirection, reflection), 0.0), specular);
    return specularColor * intensity * factorSpecular * material.reflectance * vec4(color, 1.0);
}

vec4 calculateColor(vec3 color, float intensity, vec3 position, vec3 lightDirection, vec3 normal) {
    return getDiffuseColor(color, intensity, lightDirection, normal)
        + getSpecularColor(color, intensity, position, lightDirection, normal);
}

vec4 calculatePointLight(PointLight pointLight, vec3 position, vec3 normal) {
    vec3 direction = pointLight.position - position;
    vec3 lightDirection = normalize(direction);
    vec4 color = calculateColor(pointLight.color, pointLight.intensity, pointLight.position, lightDirection, normal);

    float distance = length(direction);
    float attenuation = pointLight.attenuation.constant + pointLight.attenuation.linear * distance + pointLight.attenuation.exponent * distance * distance;
    return color / attenuation;
}

vec4 calculateSpotLight(SpotLight spotLight, vec3 position, vec3 normal) {
    vec3 direction = spotLight.pointLight.position - position;
    vec3 lightDirection = normalize(direction);
    float spotAlfa = dot(-lightDirection, normalize(spotLight.direction));

    vec4 color = vec4(0, 0, 0, 0);
    if (spotAlfa > spotLight.cutoff) {
        color = calculatePointLight(spotLight.pointLight, position, normal);
        color *= (1.0 - (1.0 - spotAlfa) / (1.0 - spotLight.cutoff));
    }
    return color;
}

vec4 calculateDirectionalLight(DirectionalLight light, vec3 position, vec3 normal) {
    return calculateColor(light.color, light.intensity, position, normalize(light.direction), normal);
}

void main()
{
    setColors(material, outTexCoord);

    vec4 diffuseSpecularComposition = calculateDirectionalLight(directionalLight, modelVertexPosition, modelVertexNormal);

    for (int i = 0; i < MAX_POINT_LIGHTS; i++) {
        if (pointLights[i].intensity > 0) {
            diffuseSpecularComposition += calculatePointLight(pointLights[i], modelVertexPosition, modelVertexNormal);
        }
    }

    for (int i = 0; i < MAX_SPOT_LIGHTS; i++) {
        if (spotLights[i].pointLight.intensity > 0) {
            diffuseSpecularComposition += calculateSpotLight(spotLights[i], modelVertexPosition, modelVertexNormal);
        }
    }

    fragColor = ambientColor * vec4(ambient, 1) + diffuseSpecularComposition;
}
