package com.github.armkrtchyan.data.di

import com.github.armkrtchyan.common.retrofit.BaseInterceptorsHelper
import com.github.armkrtchyan.data.InterceptorHelper
import com.github.armkrtchyan.data.repositories.RatesRepositoryImpl
import com.github.armkrtchyan.data.services.ServiceFactory
import com.github.armkrtchyan.domain.repositories.RatesRepository
import org.koin.dsl.module

val dataModule = module {
    single<com.github.armkrtchyan.data.IDataSource> { com.github.armkrtchyan.data.DataSource(mNetworkConnection = get()) }
}
val repositoriesModule = module {
    factory { ServiceFactory.createRatesService(retrofit = get()) }
    single<RatesRepository> { RatesRepositoryImpl(mDataSource = get(), mService = get()) }
}
val interceptorModule = module {
    single<BaseInterceptorsHelper> { InterceptorHelper() }
}