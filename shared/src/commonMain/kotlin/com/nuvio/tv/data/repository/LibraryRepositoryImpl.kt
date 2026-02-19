package com.nuvio.tv.data.repository

import com.nuvio.tv.data.persistence.JsonFileStorage
import com.nuvio.tv.domain.model.ContentType
import com.nuvio.tv.domain.model.LibraryEntry
import com.nuvio.tv.domain.model.LibraryListTab
import com.nuvio.tv.domain.repository.LibraryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer

/**
 * Local file-based implementation of LibraryRepository.
 * Stores library data in JSON files.
 */
class LibraryRepositoryImpl(
    private val storage: JsonFileStorage
) : LibraryRepository {
    
    private val _libraryEntries = MutableStateFlow<List<LibraryEntry>>(emptyList())
    private val _customLists = MutableStateFlow<List<LibraryListTab>>(emptyList())
    
    init {
        // Load data on initialization - done in init block for simplicity
        // In production, this should be called explicitly
    }
    
    /**
     * Load library data from storage.
     * Should be called when repository is initialized.
     */
    suspend fun loadFromStorage() = withContext(Dispatchers.IO) {
        try {
            val entries = storage.read(LIBRARY_ENTRIES_KEY, serializer<List<LibraryEntry>>()) ?: emptyList()
            _libraryEntries.value = entries
            
            val lists = storage.read(CUSTOM_LISTS_KEY, serializer<List<LibraryListTab>>()) ?: emptyList()
            _customLists.value = lists
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private suspend fun saveLibraryEntries() {
        storage.write(LIBRARY_ENTRIES_KEY, _libraryEntries.value, serializer())
    }
    
    private suspend fun saveCustomLists() {
        storage.write(CUSTOM_LISTS_KEY, _customLists.value, serializer())
    }
    
    override fun getLibraryItems(type: ContentType?, listKey: String?): Flow<List<LibraryEntry>> {
        return _libraryEntries.asStateFlow().map { entries ->
            entries.filter { entry ->
                val typeMatches = type == null || entry.type == type
                val listMatches = listKey == null || entry.listKey == listKey
                typeMatches && listMatches
            }
        }
    }
    
    override suspend fun addToLibrary(entry: LibraryEntry) = withContext(Dispatchers.IO) {
        val current = _libraryEntries.value.toMutableList()
        // Remove existing entry with same ID and listKey
        current.removeAll { it.id == entry.id && it.listKey == entry.listKey }
        current.add(entry)
        _libraryEntries.value = current
        saveLibraryEntries()
    }
    
    override suspend fun removeFromLibrary(id: String, listKey: String?) = withContext(Dispatchers.IO) {
        val current = _libraryEntries.value.toMutableList()
        current.removeAll { 
            it.id == id && (listKey == null || it.listKey == listKey) 
        }
        _libraryEntries.value = current
        saveLibraryEntries()
    }
    
    override suspend fun isInLibrary(id: String, listKey: String?): Boolean {
        return _libraryEntries.value.any { 
            it.id == id && (listKey == null || it.listKey == listKey) 
        }
    }
    
    override fun getCustomLists(): Flow<List<LibraryListTab>> {
        return _customLists.asStateFlow()
    }
    
    override suspend fun createCustomList(name: String): LibraryListTab = withContext(Dispatchers.IO) {
        val key = "custom_${System.currentTimeMillis()}"
        val newList = LibraryListTab(key, name, isCustom = true)
        val current = _customLists.value.toMutableList()
        current.add(newList)
        _customLists.value = current
        saveCustomLists()
        newList
    }
    
    override suspend fun deleteCustomList(key: String) = withContext(Dispatchers.IO) {
        val current = _customLists.value.toMutableList()
        current.removeAll { it.key == key }
        _customLists.value = current
        saveCustomLists()
        
        // Also remove all entries from this list
        val entries = _libraryEntries.value.toMutableList()
        entries.removeAll { it.listKey == key }
        _libraryEntries.value = entries
        saveLibraryEntries()
    }
    
    override suspend fun renameCustomList(key: String, newName: String) = withContext(Dispatchers.IO) {
        val current = _customLists.value.toMutableList()
        val index = current.indexOfFirst { it.key == key }
        if (index >= 0) {
            current[index] = current[index].copy(name = newName)
            _customLists.value = current
            saveCustomLists()
        }
    }
    
    override suspend fun clearLibrary() = withContext(Dispatchers.IO) {
        _libraryEntries.value = emptyList()
        _customLists.value = emptyList()
        saveLibraryEntries()
        saveCustomLists()
    }
    
    companion object {
        private const val LIBRARY_ENTRIES_KEY = "library_entries"
        private const val CUSTOM_LISTS_KEY = "custom_lists"
    }
}
