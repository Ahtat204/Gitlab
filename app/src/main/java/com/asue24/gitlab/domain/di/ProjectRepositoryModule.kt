package com.asue24.gitlab.domain.di

import com.asue24.gitlab.data.repositories.project.ProjectRepository
import com.asue24.gitlab.data.repositories.project.ProjectRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object  ProjectRepositoryModule {
    @Provides
     fun provideProjectRepository(impl: ProjectRepositoryImpl): ProjectRepository=ProjectRepositoryImpl()
}