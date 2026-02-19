package com.nuvio.tv.data.repository

import com.nuvio.tv.core.network.NetworkResult
import com.nuvio.tv.core.network.safeApiCall
import com.nuvio.tv.data.mapper.toDomain
import com.nuvio.tv.data.remote.api.AddonApi
import com.nuvio.tv.domain.model.Addon
import com.nuvio.tv.domain.repository.AddonRepository
import com.nuvio.tv.util.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*

class AddonRepositoryImpl(
    private val api: AddonApi
) : AddonRepository {

    private val installedUrls = MutableStateFlow<List<String>>(
        listOf("https://v3-cinemeta.strem.io") // Default addon for testing
    )

    private val manifestCache = mutableMapOf<String, Addon>()

    override fun getInstalledAddons(): Flow<List<Addon>> =
        installedUrls.flatMapLatest { urls ->
            flow {
                val cached = urls.mapNotNull { manifestCache[it] }
                if (cached.isNotEmpty()) emit(cached)

                val fresh = coroutineScope {
                    urls.map { url ->
                        async {
                            when (val result = fetchAddon(url)) {
                                is NetworkResult.Success -> result.data
                                else -> manifestCache[url]
                            }
                        }
                    }.awaitAll().filterNotNull()
                }
                emit(fresh)
            }.flowOn(Dispatchers.IO)
        }

    override suspend fun fetchAddon(baseUrl: String): NetworkResult<Addon> {
        val cleanUrl = baseUrl.trimEnd('/')
        val manifestUrl = "$cleanUrl/manifest.json"
        
        return when (val result = safeApiCall { api.getManifest(manifestUrl) }) {
            is NetworkResult.Success -> {
                val addon = result.data.toDomain(cleanUrl)
                manifestCache[cleanUrl] = addon
                NetworkResult.Success(addon)
            }
            is NetworkResult.Error -> {
                Logger.w("AddonRepository", "Failed to fetch addon: $cleanUrl")
                result
            }
            NetworkResult.Loading -> NetworkResult.Loading
        }
    }

    override suspend fun addAddon(url: String) {
        val cleanUrl = url.trimEnd('/')
        installedUrls.update { (it + cleanUrl).distinct() }
    }

    override suspend fun removeAddon(url: String) {
        val cleanUrl = url.trimEnd('/')
        installedUrls.update { it - cleanUrl }
    }

    override suspend fun setAddonOrder(urls: List<String>) {
        installedUrls.value = urls
    }
}
