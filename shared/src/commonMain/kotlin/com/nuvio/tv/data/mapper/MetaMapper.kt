package com.nuvio.tv.data.mapper

import com.nuvio.tv.data.remote.model.MetaDto
import com.nuvio.tv.data.remote.model.VideoDto
import com.nuvio.tv.data.remote.model.MetaLinkDto
import com.nuvio.tv.domain.model.*

fun MetaDto.toDomain(): Meta {
    return Meta(
        id = id,
        type = ContentType.fromString(type),
        rawType = type,
        name = name,
        poster = poster,
        posterShape = parsePosterShape(posterShape),
        background = background,
        logo = logo,
        description = description,
        releaseInfo = releaseInfo,
        imdbRating = imdbRating?.toFloatOrNull(),
        genres = genres ?: emptyList(),
        runtime = runtime,
        director = director ?: emptyList(),
        writer = parseWriters(writer, writers),
        cast = cast ?: emptyList(),
        videos = videos?.map { it.toDomain() } ?: emptyList(),
        country = country,
        awards = awards,
        language = language,
        links = links?.map { it.toDomain() } ?: emptyList(),
        castMembers = emptyList(),
        productionCompanies = emptyList(),
        networks = emptyList()
    )
}

fun VideoDto.toDomain(): Video {
    return Video(
        id = id,
        title = title ?: name ?: "Episode ${episode ?: ""}",
        released = released,
        thumbnail = thumbnail,
        season = season,
        episode = episode,
        overview = overview ?: description
    )
}

fun MetaLinkDto.toDomain(): MetaLink {
    return MetaLink(
        name = name,
        category = category,
        url = url ?: ""
    )
}

private fun parsePosterShape(shape: String?): PosterShape {
    return when (shape?.lowercase()) {
        "square" -> PosterShape.SQUARE
        "landscape" -> PosterShape.LANDSCAPE
        else -> PosterShape.POSTER
    }
}

private fun parseWriters(writer: List<String>?, writers: List<String>?): List<String> {
    return writer ?: writers ?: emptyList()
}
