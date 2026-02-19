package com.nuvio.tv.data.mapper

import com.nuvio.tv.data.remote.model.StreamResponseDto
import com.nuvio.tv.domain.model.Stream

fun StreamResponseDto.toDomain(addonName: String, addonLogo: String?): Stream {
    return Stream(
        name = name,
        title = title,
        description = description,
        url = url,
        ytId = ytId,
        infoHash = infoHash,
        fileIdx = fileIdx,
        externalUrl = externalUrl,
        behaviorHints = null, // simplified
        addonName = addonName,
        addonLogo = addonLogo
    )
}
