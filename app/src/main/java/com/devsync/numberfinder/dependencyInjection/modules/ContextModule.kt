package com.devsync.numberfinder.dependencyInjection.modules

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)  // Singleton scope for application-wide context
object ContextModule {

    @Provides
    @ApplicationContext
    fun provideApplicationContext(app: Application): Context {
        return app.applicationContext
    }

    @Provides
    @ActivityContext
    fun provideActivityContext(activity: AppCompatActivity): Context {
        return activity
    }
}
