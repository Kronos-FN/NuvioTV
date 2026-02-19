package com.nuvio.tv.domain.repository

import com.nuvio.tv.domain.model.ContentType
import com.nuvio.tv.domain.model.LibraryEntry
import com.nuvio.tv.domain.model.LibraryListTab
import kotlinx.coroutines.flow.Flow

/**
 * Repository for managing library entries (saved content).
 */
interface LibraryRepository {
    /**
     * Get all library entries, optionally filtered by type and list.
     * @param type The content type to filter by (null = all types)
     * @param listKey The list key to filter by (null = all lists)
     * @return Flow of library entries
     */
    fun getLibraryItems(type: ContentType? = null, listKey: String? = null): Flow<List<LibraryEntry>>
    
    /**
     * Add an item to the library.
     * @param entry The library entry to add
     */
    suspend fun addToLibrary(entry: LibraryEntry)
    
    /**
     * Remove an item from the library.
     * @param id The ID of the item to remove
     * @param listKey The list to remove from (null = default library)
     */
    suspend fun removeFromLibrary(id: String, listKey: String? = null)
    
    /**
     * Check if an item is in the library.
     * @param id The ID of the item to check
     * @param listKey The list to check (null = any list)
     * @return true if the item is in the library
     */
    suspend fun isInLibrary(id: String, listKey: String? = null): Boolean
    
    /**
     * Get all custom lists.
     * @return Flow of list tabs
     */
    fun getCustomLists(): Flow<List<LibraryListTab>>
    
    /**
     * Create a new custom list.
     * @param name The name of the list
     * @return The created list tab
     */
    suspend fun createCustomList(name: String): LibraryListTab
    
    /**
     * Delete a custom list.
     * @param key The key of the list to delete
     */
    suspend fun deleteCustomList(key: String)
    
    /**
     * Rename a custom list.
     * @param key The key of the list to rename
     * @param newName The new name
     */
    suspend fun renameCustomList(key: String, newName: String)
    
    /**
     * Clear all library entries (local mode only).
     */
    suspend fun clearLibrary()
}
