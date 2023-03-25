package kg.rkd.carstestapplication.di

import androidx.room.Room
import kg.rkd.carstestapplication.data.*
import kg.rkd.carstestapplication.data.db.AppDatabase
import kg.rkd.carstestapplication.data.db.StartingCars
import kg.rkd.carstestapplication.domain.CarsInteractor
import kg.rkd.carstestapplication.domain.CarsInteractorBillingDecorator
import kg.rkd.carstestapplication.domain.SettingsInteractor
import kg.rkd.carstestapplication.ui.CarsViewModel
import kg.rkd.carstestapplication.ui.settings.SettingsViewModel
import kg.rkd.carstestapplication.utils.AppDataStore
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

    single { AppDataStore(androidContext()) }

    single<BillingRepository> {
        FakeBillingRepositorySubDurationDecorator(
            FakeBillingRepositoryImpl(
                get()
            ), androidContext()
        )
    }

    single<CarsRepository> { CarsRepositoryImpl(get(), get()) }
    single<CarsRepositoryCarsSavedDecorator> { CarsRepositoryCarsSavedDecoratorImpl(get(), get()) }

    factory<CarsRepository> { CarsRepositoryImpl(get(), get()) }
    factory<CarsInteractor> {
        CarsInteractorImpl(get())
    }
    factory<CarsInteractorBillingDecorator> {
        CarsInteractorBillingDecoratorImpl(
            interactor = get(),
            billingRepository = get(),
            carsRepositoryDecorator = get()
        )
    }
    factory<SettingsInteractor> {
        SettingsInteractorImpl(
            get(),
            get()
        )
    }

    viewModel { CarsViewModel(get()) }
    viewModel { SettingsViewModel(get()) }
}