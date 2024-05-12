package com.yawki.di

import com.yawki.navigator.ComposeNavigator
import com.yawki.navigator.YawKiComposeNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigationModule {
    @Binds
    @Singleton
    abstract fun provideComposeNavigator(yawkiComposeNavigator: YawKiComposeNavigator): ComposeNavigator
}