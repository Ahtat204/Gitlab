package com.ahtat204.gitlab.domain.di

import com.ahtat204.gitlab.data.remote.repositories.project.ProjectRepository
import com.ahtat204.gitlab.data.remote.repositories.project.ProjectRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * Dagger Hilt module that binds [ProjectRepositoryImpl] to the [ProjectRepository] interface.
 *
 * ## Overview
 * - Ensures that whenever a [ProjectRepository] is requested in the DI graph,
 *   Hilt provides an instance of [ProjectRepositoryImpl].
 * - Installed in the [ViewModelComponent], meaning the bound instance
 *   will live as long as the ViewModel lifecycle.
 *
 * ## Benefits
 * - Promotes clean architecture by depending on the abstraction ([ProjectRepository])
 *   rather than the concrete implementation.
 * - Simplifies testing: alternative implementations of [ProjectRepository] can be
 *   swapped in without changing consumer code.
 *
 * ## Usage
 * Inject [ProjectRepository] into a ViewModel:
 * ```kotlin
 * @HiltViewModel
 * class ProjectViewModel @Inject constructor(
 *     private val repository: ProjectRepository
 * ) : ViewModel() {
 *     // Use repository methods here
 * }
 * ```
 * @author Lahcen AHTAT
 */
@Module
@InstallIn(ViewModelComponent::class)
abstract class ProjectRepositoryModule {

    /**
     * Binds [ProjectRepositoryImpl] as the concrete implementation of [ProjectRepository].<br>
     * @param impl The injected implementation.
     * @return The bound [ProjectRepository] interface.
     *
     */
    @Binds
    abstract fun provideProjectRepository(impl: ProjectRepositoryImpl): ProjectRepository
}
