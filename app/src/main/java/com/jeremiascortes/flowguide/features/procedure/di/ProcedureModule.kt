package com.jeremiascortes.flowguide.features.procedure.di

import com.jeremiascortes.flowguide.features.procedure.data.repository.ProcedureRepositoryImpl
import com.jeremiascortes.flowguide.features.procedure.domain.repository.ProcedureRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProcedureModule {

    @Binds
    @Singleton
    abstract fun bindProcedureRepository(
        procedureRepository: ProcedureRepositoryImpl
    ): ProcedureRepository
}