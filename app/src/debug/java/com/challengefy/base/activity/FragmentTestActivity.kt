package com.challengefy.base.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.FrameLayout
import dagger.android.AndroidInjector
import dagger.android.support.HasSupportFragmentInjector

class FragmentTestActivity : BaseActivity(), HasSupportFragmentInjector {

    private var injectionListener: ((Fragment) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(FrameLayout(this))
    }

    fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, fragment)
                .commit()
    }

    fun setInjectionListener(injection: (Fragment) -> Unit) {
        injectionListener = injection
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return AndroidInjector {
            injectionListener?.invoke(it)
        }
    }
}
