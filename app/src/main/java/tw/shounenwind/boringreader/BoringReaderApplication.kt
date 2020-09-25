package tw.shounenwind.boringreader

import androidx.multidex.MultiDexApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class BoringReaderApplication : MultiDexApplication() {

    private val mainViewModelModule = module {
        viewModel {
            MainViewModel()
        }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BoringReaderApplication)
            modules(
                listOf(
                    mainViewModelModule,
                )
            )
        }
    }

}