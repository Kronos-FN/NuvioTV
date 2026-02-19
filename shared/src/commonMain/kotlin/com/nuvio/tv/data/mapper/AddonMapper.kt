package com.nuvio.tv.data.mapper

import com.nuvio.tv.data.remote.model.*
import com.nuvio.tv.domain.model.*

fun AddonManifest.toDomain(baseUrl: String): Addon {
    return Addon(
        id = id,
        name = name,
        version = version,
        description = description,
        logo = logo ?: background,
        baseUrl = baseUrl,
        catalogs = catalogs.map { it.toDomain() },
        types = types.map { ContentType.fromString(it) },
        resources = resources.map { it.toDomain() }
    )
}

fun ManifestCatalog.toDomain(): CatalogDescriptor {
    return CatalogDescriptor(
        type = ContentType.fromString(type),
        rawType = type,
        id = id,
        name = name ?: id,
        extra = extra.map { it.toDomain() }
    )
}

fun ManifestExtra.toDomain(): CatalogExtra {
    return CatalogExtra(
        name = name,
        isRequired = isRequired,
        options = options
    )
}

fun ManifestResource.toDomain(): AddonResource {
    return AddonResource(
        name = name,
        types = types,
        idPrefixes = idPrefixes
    )
}
