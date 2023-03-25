package kg.rkd.carstestapplication.di

import androidx.room.Room
import kg.rkd.carstestapplication.MainActivity
import kg.rkd.carstestapplication.data.*
import kg.rkd.carstestapplication.data.db.AppDatabase
import kg.rkd.carstestapplication.data.db.StartingCars
import kg.rkd.carstestapplication.domain.CarsInteractor
import kg.rkd.carstestapplication.domain.CarsInteractorBillingDecorator
import kg.rkd.carstestapplication.ui.CarsViewModel
import kg.rkd.carstestapplication.utils.AppPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val koinModule = module {

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "cars_db")
            .addCallback(StartingCars(androidContext()))
            .fallbackToDestructiveMigration()
            .build()
    }
    factory { get<AppDatabase>().carDao() }

    single { AppPreferences(androidContext()) }

    single<BillingRepository> { BillingRepositoryFakeImpl(get()) }

    single<CarsRepository> { CarsRepositoryImpl(get()) }
    single<CarsRepositoryDecorator> { CarsRepositoryDecoratorImpl(get(), get()) }

    factory<CarsInteractor> {
        CarsInteractorImpl(get())
    }
    factory<CarsInteractorBillingDecorator> {
        CarsInteractorImplWithBilling(
            interactor = get(),
            billingRepository = get(),
            carsRepositoryDecorator = get()
        )
    }

    viewModel { CarsViewModel(get()) }

}