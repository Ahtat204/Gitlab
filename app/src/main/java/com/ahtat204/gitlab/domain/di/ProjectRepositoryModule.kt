package com.ahtat204.gitlab.domain.di

import com.ahtat204.gitlab.data.remote.repositories.graphql.ApolloGraphQLRepository
import com.ahtat204.gitlab.data.remote.repositories.graphql.GraphQlRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * Dagger Hilt module that binds [ApolloGraphQLRepository] to the [GraphQlRepository] interface.
 *
 * ## Overview
 * - Ensures that whenever a [GraphQlRepository] is requested in the DI graph,
 *   Hilt provides an instance of [ApolloGraphQLRepository].
 * - Installed in the [ViewModelComponent], meaning the bound instance
 *   will live as long as the ViewModel lifecycle.
 *
 * ## Benefits
 * - Promotes clean architecture by depending on the abstraction ([GraphQlRepository])
 *   rather than the concrete implementation.
 * - Simplifies testing: alternative implementations of [GraphQlRepository] can be
 *   swapped in without changing consumer code.
 *
 * ## Usage
 * Inject [GraphQlRepository] into a ViewModel:
 * ```kotlin
 * @HiltViewModel
 * class ProjectViewModel @Inject constructor(
 *     private val repository: GraphQlRepository
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
     * Binds [ApolloGraphQLRepository] as the concrete implementation of [GraphQlRepository].<br>
     * @param impl The injected implementation.
     * @return The bound [GraphQlRepository] interface.
     *
     */
    @Binds
    abstract fun provideProjectRepository(impl: ApolloGraphQLRepository): GraphQlRepository
}
