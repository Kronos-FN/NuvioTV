package com.nuvio.tv.data.repository

import com.nuvio.tv.core.network.NetworkResult
import com.nuvio.tv.core.network.safeApiCall
import com.nuvio.tv.data.mapper.toDomain
import com.nuvio.tv.data.remote.api.AddonApi
import com.nuvio.tv.domain.model.CatalogRow
import com.nuvio.tv.domain.model.ContentType
import com.nuvio.tv.domain.repository.CatalogRepository
import com.nuvio.tv.util.Logger
import com.nuvio.tv.util.UrlEncoder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CatalogRepositoryImpl(
    private val api: AddonApi
) : CatalogRepository {
    companion object {
        private const val TAG = "CatalogRepository"
    }

    private val catalogCache = mutableMapOf<String, CatalogRow>()

    override fun getCatalog(
        addonBaseUrl: String,
        addonId: String,
        addonName: String,
        catalogId: String,
        catalogName: String,
        type: String,
        skip: Int,
        extraArgs: Map<String, String>,
        supportsSkip: Boolean
    ): Flow<NetworkResult<CatalogRow>> = flow {
        val cacheKey = "${addonId}_${type}_${catalogId}_$skip"

        val cached = catalogCache[cacheKey]
        if (cached != null) {
            emit(NetworkResult.Success(cached))
        } else {
            emit(NetworkResult.Loading)
        }

        val url = buildCatalogUrl(addonBaseUrl, type, catalogId, skip, extraArgs)
        Logger.d(TAG, "Fetching catalog: $url")

        when (val result = safeApiCall { api.getCatalog(url) }) {
            is NetworkResult.Success -> {
                val items = result.data.metas.map { it.toDomain() }
                Logger.d(TAG, "Catalog fetch success, items: ${items.size}")

                val catalogRow = CatalogRow(
                    addonId = addonId,
                    addonName = addonName,
                    addonBaseUrl = addonBaseUrl,
                    catalogId = catalogId,
                    catalogName = catalogName,
                    type = ContentType.fromString(type),
                    rawType = type,
                    items = items,
                    isLoading = false,
                    hasMore = supportsSkip && items.isNotEmpty(),
                    currentPage = skip / 100,
                    supportsSkip = supportsSkip
                )
                catalogCache[cacheKey] = catalogRow
                if (cached?.items != catalogRow.items) {
                    emit(NetworkResult.Success(catalogRow))
                }
            }
            is NetworkResult.Error -> {
                Logger.w(TAG, "Catalog fetch failed: ${result.message}")
                if (cached == null) {
                    emit(result)
                }
            }
            NetworkResult.Loading -> {}
        }
    }

    private fun buildCatalogUrl(
        baseUrl: String,
        type: String,
        catalogId: String,
        skip: Int,
        extraArgs: Map<String, String>
    ): String {
        val cleanBaseUrl = baseUrl.trimEnd('/')
        val allArgs = extraArgs.toMutableMap()
        if (!allArgs.containsKey("skip") && skip > 0) {
            allArgs["skip"] = skip.toString()
        }
        val encodedArgs = allArgs.entries.joinToString("&") { (k, v) ->
            "${UrlEncoder.encode(k)}=${UrlEncoder.encode(v)}"
        }
        val path = if (encodedArgs.isNotEmpty()) {
            "/catalog/$type/$catalogId/$encodedArgs.json"
        } else {
            "/catalog/$type/$catalogId.json"
        }
        return "$cleanBaseUrl$path"
    }
}
