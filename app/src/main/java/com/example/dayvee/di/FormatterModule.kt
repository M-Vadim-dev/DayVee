package com.example.dayvee.di

import com.example.dayvee.domain.formatter.DurationFormatter
import com.example.dayvee.domain.formatter.DurationFormatterImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FormatterModule {

    @Binds
    @Singleton
    abstract fun bindDurationFormatter(impl: DurationFormatterImpl): DurationFormatter

}
