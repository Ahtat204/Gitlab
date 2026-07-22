package com.ahtat204.gitlab.domain.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OnlineApolloClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OfflineApolloClient