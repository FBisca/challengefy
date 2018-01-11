package com.challengefy

import android.content.Context
import com.challengefy.base.di.scope.ActivityScope
import com.challengefy.estimate.activity.EstimateActivity
import javax.inject.Inject

@ActivityScope
class Navigator @Inject constructor(
        private val context: Context
) {

    fun goToEstimateScreen() {
        context.startActivity(EstimateActivity.startIntent(context))
    }
}
