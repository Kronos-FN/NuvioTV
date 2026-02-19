package com.nuvio.tv.data.remote.api

import com.nuvio.tv.core.network.createHttpClient
import com.nuvio.tv.data.remote.model.AddonManifest
import com.nuvio.tv.data.remote.model.CatalogResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class AddonApi(private val client: HttpClient = createHttpClient()) {

    suspend fun getManifest(url: String): AddonManifest {
        return client.get(url).body()
    }

    suspend fun getCatalog(url: String): CatalogResponse {
        return client.get(url).body()
    }
}
