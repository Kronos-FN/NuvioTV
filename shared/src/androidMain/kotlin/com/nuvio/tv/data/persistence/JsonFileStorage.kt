package com.nuvio.tv.data.persistence

import kotlinx.serialization.KSerializer

/**
 * Android implementation of JSON file-based storage.
 * This is a stub for Android - actual implementation would use DataStore or similar.
 */
actual class JsonFileStorage actual constructor() : FileBasedPreferences {
    
    override suspend fun <T> read(key: String, serializer: KSerializer<T>): T? {
        // TODO: Implement for Android using DataStore
        return null
    }
    
    override suspend fun <T> write(key: String, value: T, serializer: KSerializer<T>) {
        // TODO: Implement for Android using DataStore
    }
    
    override suspend fun delete(key: String) {
        // TODO: Implement for Android
    }
    
    override suspend fun clear() {
        // TODO: Implement for Android
    }
    
    override suspend fun contains(key: String): Boolean {
        // TODO: Implement for Android
        return false
    }
}
