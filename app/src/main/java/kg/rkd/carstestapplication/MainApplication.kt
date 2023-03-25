package kg.rkd.carstestapplication

import android.app.Application
import android.content.Context
import androidx.work.*
import kg.rkd.carstestapplication.data.UnSubWorker
import kg.rkd.carstestapplication.di.koinModule
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val workConfig = Configuration.Builder()
            .setWorkerFactory(AppWorkerFactory())
            .build()
        WorkManager.initialize(this, workConfig)

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(koinModule)
        }
    }

    inner class AppWorkerFactory() : WorkerFactory() {
        override fun createWorker(
            appContext: Context,
            workerClassName: String,
            workerParameters: WorkerParameters
        ): ListenableWorker? {
            return when (workerClassName) {
                UnSubWorker::class.java.name -> UnSubWorker(
                    appContext, workerParameters, getKoin().get()
                )
                else -> null
            }
        }

    }
}