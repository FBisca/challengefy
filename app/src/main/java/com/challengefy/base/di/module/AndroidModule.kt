package com.challengefy.base.di.module

import android.content.Context
import android.os.Build
import dagger.Module
import dagger.Provides
import java.util.*

@Module
class AndroidModule {

    @Provides
    fun providesLocale(context: Context): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales.get(0)
        } else {
            context.resources.configuration.locale
        }
    }
}
