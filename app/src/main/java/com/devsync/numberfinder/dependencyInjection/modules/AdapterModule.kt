package com.devsync.numberfinder.dependencyInjection.modules

import android.content.Context
import com.devsync.numberfinder.ui.adapters.CompaniesAdapter
import com.devsync.numberfinder.ui.adapters.SimCountryAdapter
import com.devsync.numberfinder.ui.interfaces.BlockedNumClickListener
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class) // or FragmentComponent based on scope
object AdapterModule {

    @Provides
    fun provideSimCountryAdapter(
        context: Context,
        listener: BlockedNumClickListener
    ): SimCountryAdapter {
        return SimCountryAdapter(context, ArrayList(), listener)
    }

    @Provides
    fun provideCompaniesAdapter(
        context: Context,
        listener: BlockedNumClickListener
    ): CompaniesAdapter {
        return CompaniesAdapter(context, ArrayList(), listener)
    }
}
