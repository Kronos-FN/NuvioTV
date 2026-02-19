package com.nuvio.tv.desktop.di

import com.nuvio.tv.core.network.createHttpClient
import com.nuvio.tv.data.remote.api.AddonApi
import com.nuvio.tv.data.repository.AddonRepositoryImpl
import com.nuvio.tv.data.repository.CatalogRepositoryImpl
import com.nuvio.tv.data.repository.MetaRepositoryImpl
import com.nuvio.tv.data.repository.StreamRepositoryImpl
import com.nuvio.tv.domain.repository.AddonRepository
import com.nuvio.tv.domain.repository.CatalogRepository
import com.nuvio.tv.domain.repository.MetaRepository
import com.nuvio.tv.domain.repository.StreamRepository
import com.nuvio.tv.ui.screens.home.HomeViewModel
import com.nuvio.tv.ui.screens.search.SearchViewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

val koinModule = module {
    // Network
    single { createHttpClient() }
    single { AddonApi(get()) }

    // Repositories
    single<AddonRepository> { AddonRepositoryImpl(get()) }
    single<CatalogRepository> { CatalogRepositoryImpl(get()) }
    single<MetaRepository> { MetaRepositoryImpl(get(), get()) }
    single<StreamRepository> { StreamRepositoryImpl(get(), get()) }

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
}

fun initKoin() {
    startKoin {
        printLogger() 
        modules(koinModule)
    }
}
