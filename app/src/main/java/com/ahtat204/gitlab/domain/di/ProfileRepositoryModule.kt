package com.ahtat204.gitlab.domain.di

import com.ahtat204.gitlab.data.repositories.profile.ProfileRepository
import com.ahtat204.gitlab.data.repositories.profile.ProfileRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
abstract class ProfileRepositoryModule {
    @Binds
    abstract fun provideProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository
}