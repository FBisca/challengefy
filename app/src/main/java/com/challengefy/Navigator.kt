package com.challengefy

import android.content.Context
import com.challengefy.base.di.scope.ActivityScope
import javax.inject.Inject

@ActivityScope
class Navigator @Inject constructor(
        private val context: Context
) {

    fun goToEstimateScreen() {
        // TODO Put the correct start activity
    }
}
