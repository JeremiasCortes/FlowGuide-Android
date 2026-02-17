package com.jeremiascortes.flowguide.features.auth.di

/**
 * ============================================================================
 * CAPA: DI (Dependency Injection) - Hilt Module
 * ============================================================================
 *
 * Módulo de Hilt que configura las dependencias de la feature de autenticación.
 *
 * QUÉ HACE ESTE ARCHIVO:
 * - Conecta la interfaz (AuthRepository) con su implementación (AuthRepositoryImpl)
 * - Le dice a Hilt cómo crear las instancias necesarias
 *
 * ANOTACIONES:
 * - @Module: Indica que esto es un módulo de Hilt
 * - @InstallIn(SingletonComponent::class): El módulo vive mientras la app
 * - @Binds: Conecta interfaz con implementación
 * - @Singleton: Solo una instancia en toda la app
 *
 * POR QUÉ NECESITAMOS ESTO:
 * AuthViewModel depende de AuthRepository (interfaz), no de AuthRepositoryImpl.
 * Hilt necesita saber qué implementación usar cuando alguien pide AuthRepository.
 * Este módulo le dice: "Cuando pidan AuthRepository, diles AuthRepositoryImpl".
 *
 * PARA NUEVAS FEATURES:
 * Copia este patrón para cada feature que tenga repositorios.
 * ============================================================================
 */

import com.jeremiascortes.flowguide.features.auth.data.repository.AuthRepositoryImpl
import com.jeremiascortes.flowguide.features.auth.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    /**
     * Vincula la implementación con la interfaz.
     * Cuando alguien pida AuthRepository, Hilt proporcionará AuthRepositoryImpl.
     */
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
}
