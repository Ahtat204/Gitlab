package com.ahtat204.gitlab.domain.di

import com.ahtat204.gitlab.data.remote.repositories.project.ProjectRepository
import com.ahtat204.gitlab.data.remote.repositories.user.UserRepository
import com.ahtat204.gitlab.data.remote.repositories.user.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Singleton

/**
 * Dagger Hilt module that binds [UserRepositoryImpl] to the [UserRepository] interface.
 *
 * ## Overview
 * - Ensures that whenever a [UserRepository] is requested in the DI graph,
 *   Hilt provides an instance of [UserRepositoryImpl].
 * - Installed in the [ViewModelComponent], meaning the bound instance
 *   will live as long as the ViewModel lifecycle.
 *
 * ## Benefits
 * - Promotes clean architecture by depending on the abstraction ([UserRepository])
 *   rather than the concrete implementation.
 * - Simplifies testing: alternative implementations of [UserRepository] can be
 *   swapped in without changing consumer code.
 *
 * ## Usage
 * Inject [UserRepository] into a ViewModel:
 * ```kotlin
 * @HiltViewModel
 * class ProjectViewModel @Inject constructor(
 *     private val repository: ProjectRepository
 * ) : ViewModel() {
 *     // Use repository methods here
 * }
 * ```
 */

@Module
@InstallIn(ViewModelComponent::class)
abstract class UserRepositoryModule {
    /**
     * Binds [UserRepositoryImpl] as the concrete implementation of [UserRepository].<br>
     * Used [Singleton] to avoid creating a new Repository everytime the ViewModel is created since this Dependency is just for fetching data , doesn't have a state to hold
     * @param impl The injected implementation.
     * @return The bound [ProjectRepository] interface.
     *
     */
    @Binds
    abstract fun registerUserRepository(impl: UserRepositoryImpl): UserRepository
}