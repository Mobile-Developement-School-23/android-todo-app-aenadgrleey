package com.aenadgrleey.auth.data.di

import com.aenadgrleey.auth.data.provider.AuthProviderImpl
import com.aenadgrleey.auth.data.repository.AuthRepositoryImpl
import com.aenadgrleey.auth.domain.AuthProvider
import com.aenadgrleey.auth.domain.AuthRepository
import com.aenadgrleey.auth.domain.MutableAuthProvider
import dagger.Binds
import dagger.Module

@Module
abstract class AuthModule {

    @Binds
    @AuthProviderScope
    abstract fun bindsAuthRepository(authRepository: AuthRepositoryImpl): AuthRepository

    @Binds
    @AuthProviderScope
    abstract fun bindsMutableAuthProvider(authProvider: AuthProviderImpl): MutableAuthProvider

    @Binds
    @AuthProviderScope
    abstract fun bindsAuthProvider(authProvider: AuthProviderImpl): AuthProvider
}