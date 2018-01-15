package com.challengefy.base.di.component

import android.content.Context
import com.challengefy.App
import com.challengefy.base.di.module.ActivityBindModule
import com.challengefy.base.di.module.AndroidModule
import com.challengefy.base.di.module.DataModule
import com.challengefy.base.di.module.LoggingModule
import dagger.BindsInstance
import dagger.Component
import dagger.MembersInjector
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            AndroidInjectionModule::class,
            ActivityBindModule::class,
            AndroidModule::class,
            DataModule::class,
            LoggingModule::class
        ]
)
interface ApplicationComponent : MembersInjector<App> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder

        fun build(): ApplicationComponent
    }
}
