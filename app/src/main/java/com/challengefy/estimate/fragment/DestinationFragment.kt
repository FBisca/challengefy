package com.challengefy.estimate.fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import com.challengefy.R
import com.challengefy.data.source.location.FusedPositionSource
import com.google.android.gms.location.LocationRequest

class DestinationFragment : Fragment() {

    companion object {
        fun newInstance() = DestinationFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_destination, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imgFindLocation = view.findViewById<View>(R.id.estimate_img_find_location)
        val ctnCard = view.findViewById<ConstraintLayout>(R.id.estimate_ctn_card)
        val cardView = view.findViewById<CardView>(R.id.estimate_card_view)


        val animation = ObjectAnimator.ofFloat(imgFindLocation, "rotation", 0f, 360f).apply {
            interpolator =  OvershootInterpolator()
            duration = 1500
            startDelay = 1000

            addListener(object : AnimatorListenerAdapter() {
                var cancelled = false

                override fun onAnimationCancel(animation: Animator?) {
                    cancelled = true
                }

                override fun onAnimationEnd(animation: Animator) {
                    if (!cancelled) {
                        animation.start()
                    }
                }
            })
            start()
        }

        Handler().postDelayed({
            animation.cancel()

            val set = ConstraintSet().also {
                it.clone(context, R.layout.include_estimate_destination)
            }

            TransitionManager.beginDelayedTransition(cardView)

            cardView.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            set.applyTo(ctnCard)

        }, 4000)
    }
}
