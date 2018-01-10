package com.challengefy.base.di.component

import android.content.Context
import com.challengefy.App
import dagger.BindsInstance
import dagger.Component
import dagger.MembersInjector
import dagger.android.AndroidInjectionModule

@Component(
        modules = [
            AndroidInjectionModule::class
        ]
)
interface ApplicationComponent : MembersInjector<App>{



    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder
        fun build(): ApplicationComponent
    }
}