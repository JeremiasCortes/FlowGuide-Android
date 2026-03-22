package com.jeremiascortes.flowguide.features.settings.di

import com.jeremiascortes.flowguide.features.settings.data.repository.SettingsRepositoryImpl
import com.jeremiascortes.flowguide.features.settings.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsModule{

    @Binds
    @Singleton
    abstract fun bindSettingRepository(
        settingsRepository: SettingsRepositoryImpl
    ): SettingsRepository
}