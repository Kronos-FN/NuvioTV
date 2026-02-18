import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

val koinModule = module {
    // Define your dependencies here.
    single { SomeDependency() }
}

fun initializeKoin() {
    loadKoinModules(koinModule)
}