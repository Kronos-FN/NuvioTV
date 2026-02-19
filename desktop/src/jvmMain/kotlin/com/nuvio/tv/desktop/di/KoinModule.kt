package com.nuvio.tv.desktop.di

import com.nuvio.tv.core.network.createHttpClient
import com.nuvio.tv.data.persistence.JsonFileStorage
import com.nuvio.tv.data.remote.api.AddonApi
import com.nuvio.tv.data.remote.api.TraktApi
import com.nuvio.tv.data.repository.AddonRepositoryImpl
import com.nuvio.tv.data.repository.CatalogRepositoryImpl
import com.nuvio.tv.data.repository.LibraryRepositoryImpl
import com.nuvio.tv.data.repository.MetaRepositoryImpl
import com.nuvio.tv.data.repository.StreamRepositoryImpl
import com.nuvio.tv.domain.repository.AddonRepository
import com.nuvio.tv.domain.repository.CatalogRepository
import com.nuvio.tv.domain.repository.LibraryRepository
import com.nuvio.tv.domain.repository.MetaRepository
import com.nuvio.tv.domain.repository.StreamRepository
import com.nuvio.tv.ui.screens.home.HomeViewModel
import com.nuvio.tv.ui.screens.library.LibraryViewModel
import com.nuvio.tv.ui.screens.search.SearchViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.core.context.startKoin
import org.koin.dsl.module

val koinModule = module {
    // Persistence
    single { JsonFileStorage() }

    // Network
    single { createHttpClient() }
    single { AddonApi(get()) }
    single { TraktApi(get()) }

    // Repositories
    single<AddonRepository> { AddonRepositoryImpl(get()) }
    single<CatalogRepository> { CatalogRepositoryImpl(get()) }
    single<MetaRepository> { MetaRepositoryImpl(get(), get()) }
    single<StreamRepository> { StreamRepositoryImpl(get(), get()) }
    single<LibraryRepository> { 
        LibraryRepositoryImpl(get()).also { repo ->
            // Load library data on app start
            CoroutineScope(Dispatchers.IO).launch {
                repo.loadFromStorage()
            }
        }
    }

    // ViewModels
    factory { 
        HomeViewModel(
            addonRepository = get(),
            catalogRepository = get(),
            coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
        ) 
    }
    factory {
        SearchViewModel(
            addonRepository = get(),
            catalogRepository = get(),
            coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
        )
    }
    factory {
        LibraryViewModel(
            libraryRepository = get(),
            coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
        )
    }
}

fun initKoin() {
    startKoin {
        printLogger() 
        modules(koinModule)
    }
}
