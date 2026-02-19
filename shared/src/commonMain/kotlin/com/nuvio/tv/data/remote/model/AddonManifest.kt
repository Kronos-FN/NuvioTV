package com.nuvio.tv.data.remote.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*

@Serializable
data class AddonManifest(
    @SerialName("id") val id: String,
    @SerialName("version") val version: String,
    @SerialName("name") val name: String,
    @SerialName("description") val description: String? = null,
    @SerialName("logo") val logo: String? = null,
    @SerialName("background") val background: String? = null,
    @SerialName("types") val types: List<String> = emptyList(),
    @SerialName("resources") val resources: List<ManifestResource> = emptyList(),
    @SerialName("catalogs") val catalogs: List<ManifestCatalog> = emptyList()
)

@Serializable(with = ManifestResourceSerializer::class)
data class ManifestResource(
    val name: String,
    val types: List<String> = emptyList(),
    val idPrefixes: List<String>? = null
)

object ManifestResourceSerializer : KSerializer<ManifestResource> {
    override val descriptor = JsonElement.serializer().descriptor

    override fun deserialize(decoder: Decoder): ManifestResource {
        val input = decoder as? JsonDecoder ?: throw SerializationException("Only JSON supported")
        val element = input.decodeJsonElement()
        
        return if (element is JsonPrimitive) {
            ManifestResource(name = element.content)
        } else {
            val obj = element.jsonObject
            ManifestResource(
                name = obj["name"]?.jsonPrimitive?.content ?: "",
                types = obj["types"]?.jsonArray?.map { it.jsonPrimitive.content } ?: emptyList(),
                idPrefixes = obj["idPrefixes"]?.jsonArray?.map { it.jsonPrimitive.content }
            )
        }
    }

    override fun serialize(encoder: Encoder, value: ManifestResource) {
        val output = encoder as? JsonEncoder ?: throw SerializationException("Only JSON supported")
        output.encodeJsonElement(buildJsonObject {
            put("name", value.name)
            put("types", JsonArray(value.types.map { JsonPrimitive(it) }))
            value.idPrefixes?.let { put("idPrefixes", JsonArray(it.map { JsonPrimitive(it) })) }
        })
    }
}

@Serializable
data class ManifestCatalog(
    @SerialName("type") val type: String,
    @SerialName("id") val id: String,
    @SerialName("name") val name: String? = null,
    @SerialName("extra") val extra: List<ManifestExtra> = emptyList()
)

@Serializable
data class ManifestExtra(
    @SerialName("name") val name: String,
    @SerialName("isRequired") val isRequired: Boolean = false,
    @SerialName("options") val options: List<String>? = null
)
