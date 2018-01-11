package com.challengefy.estimate.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.challengefy.R
import com.challengefy.base.activity.BaseActivity
import com.challengefy.estimate.fragment.DestinationFragment
import com.challengefy.map.fragment.MapFragment
import com.google.android.gms.maps.GoogleMapOptions

class EstimateActivity : BaseActivity() {

    companion object {
        fun startIntent(context: Context) = Intent(context, EstimateActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estimate)

        supportFragmentManager.beginTransaction()
                .replace(R.id.estimate_container_map, MapFragment.newInstance())
                .commit()

        supportFragmentManager.beginTransaction()
                .replace(R.id.estimate_container_content, DestinationFragment.newInstance())
                .commit()
    }
}
