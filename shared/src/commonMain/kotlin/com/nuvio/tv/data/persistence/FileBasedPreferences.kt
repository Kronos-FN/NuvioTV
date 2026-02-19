package com.nuvio.tv.data.persistence

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

/**
 * Platform-agnostic interface for file-based preferences storage.
 * Platform-specific implementations should be provided in androidMain and desktopMain.
 */
interface FileBasedPreferences {
    /**
     * Read a value from storage.
     * @param key The key to read
     * @param serializer The serializer for the type
     * @return The value, or null if not found
     */
    suspend fun <T> read(key: String, serializer: KSerializer<T>): T?
    
    /**
     * Write a value to storage.
     * @param key The key to write
     * @param value The value to write
     * @param serializer The serializer for the type
     */
    suspend fun <T> write(key: String, value: T, serializer: KSerializer<T>)
    
    /**
     * Delete a value from storage.
     * @param key The key to delete
     */
    suspend fun delete(key: String)
    
    /**
     * Clear all stored preferences.
     */
    suspend fun clear()
    
    /**
     * Check if a key exists in storage.
     * @param key The key to check
     * @return true if the key exists
     */
    suspend fun contains(key: String): Boolean
}

/**
 * Generic preference store that uses FileBasedPreferences for persistence.
 * @param T The type of the preference value
 * @param key The storage key for this preference
 * @param defaultValue The default value if no value is stored
 * @param serializer The serializer for type T
 * @param storage The file-based storage implementation
 */
class PersistentPreferenceStore<T>(
    private val key: String,
    private val defaultValue: T,
    private val serializer: KSerializer<T>,
    private val storage: FileBasedPreferences
) {
    private val _value = MutableStateFlow(defaultValue)
    val value: Flow<T> = _value.asStateFlow()
    
    suspend fun load() {
        val stored = storage.read(key, serializer)
        if (stored != null) {
            _value.value = stored
        }
    }
    
    suspend fun save(value: T) {
        _value.value = value
        storage.write(key, value, serializer)
    }
    
    suspend fun update(transform: (T) -> T) {
        val newValue = transform(_value.value)
        save(newValue)
    }
    
    suspend fun reset() {
        save(defaultValue)
    }
    
    fun currentValue(): T = _value.value
}

/**
 * JSON-based file storage implementation.
 * Platform-specific path resolution should be provided.
 */
expect class JsonFileStorage() : FileBasedPreferences {
    override suspend fun <T> read(key: String, serializer: KSerializer<T>): T?
    override suspend fun <T> write(key: String, value: T, serializer: KSerializer<T>)
    override suspend fun delete(key: String)
    override suspend fun clear()
    override suspend fun contains(key: String): Boolean
}
