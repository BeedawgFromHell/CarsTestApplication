package kg.rkd.carstestapplication.di

import androidx.room.Room
import kg.rkd.carstestapplication.data.*
import kg.rkd.carstestapplication.data.db.AppDatabase
import kg.rkd.carstestapplication.data.db.StartingCars
import kg.rkd.carstestapplication.domain.CarsInteractor
import kg.rkd.carstestapplication.ui.home.CarsViewModel
import kg.rkd.carstestapplication.utils.AppPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val koinModule = module {

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "cars_db")
            .addCallback(StartingCars(androidContext()))
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<AppDatabase>().carDao() }
    single { get<AppDatabase>().pictureDao() }

    factory { AppPreferences(androidContext()) }

    factory<BillingRepository> { BillingRepositoryFakeImpl(get()) }
    factory<CarsRepository> { CarsRepositoryImpl(get(), get()) }
    factory<CarsInteractor> {
        CarsInteractorImplWithBilling(
            interactor = CarsInteractorImpl(get()),
            billingRepository = get()
        )
    }

    viewModel { CarsViewModel(get()) }
}