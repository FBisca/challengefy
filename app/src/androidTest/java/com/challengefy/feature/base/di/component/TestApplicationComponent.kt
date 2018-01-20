package com.challengefy.feature.base.di.component

import android.content.Context
import com.challengefy.base.di.component.ApplicationComponent
import com.challengefy.base.di.module.ActivityBindModule
import com.challengefy.base.di.module.AndroidModule
import com.challengefy.base.di.module.LoggingModule
import com.challengefy.base.di.module.NetworkModule
import com.challengefy.feature.TestApp
import com.challengefy.feature.base.di.module.FakeDataModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            AndroidInjectionModule::class,
            ActivityBindModule::class,
            AndroidModule::class,
            FakeDataModule::class,
            LoggingModule::class
        ]
)
interface TestApplicationComponent : ApplicationComponent {

    fun injectMembers(app: TestApp)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): TestApplicationComponent.Builder

        fun networkModule(networkModule: NetworkModule): TestApplicationComponent.Builder

        fun build(): TestApplicationComponent
    }
}
