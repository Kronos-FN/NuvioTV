package com.nuvio.tv.data.persistence

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import java.io.File

/**
 * Desktop implementation of JSON file-based storage.
 * Stores preferences in the user's home directory under ~/.nuvio/
 */
actual class JsonFileStorage actual constructor() : FileBasedPreferences {
    
    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        encodeDefaults = true
    }
    
    private val storageDir = File(System.getProperty("user.home"), ".nuvio").apply {
        if (!exists()) {
            mkdirs()
        }
    }
    
    private fun getFile(key: String): File {
        return File(storageDir, "$key.json")
    }
    
    override suspend fun <T> read(key: String, serializer: KSerializer<T>): T? = withContext(Dispatchers.IO) {
        try {
            val file = getFile(key)
            if (!file.exists()) return@withContext null
            
            val jsonString = file.readText()
            json.decodeFromString(serializer, jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    override suspend fun <T> write(key: String, value: T, serializer: KSerializer<T>) = withContext(Dispatchers.IO) {
        try {
            val file = getFile(key)
            val jsonString = json.encodeToString(serializer, value)
            file.writeText(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    override suspend fun delete(key: String) = withContext(Dispatchers.IO) {
        try {
            val file = getFile(key)
            if (file.exists()) {
                file.delete()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    override suspend fun clear() = withContext(Dispatchers.IO) {
        try {
            storageDir.listFiles()?.forEach { file ->
                if (file.extension == "json") {
                    file.delete()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    override suspend fun contains(key: String): Boolean = withContext(Dispatchers.IO) {
        getFile(key).exists()
    }
}
