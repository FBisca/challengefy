package com.challengefy.base.activity

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    inline fun <reified T: Fragment> findFragment(): T? {
        return supportFragmentManager.findFragmentByTag(T::class.java.simpleName) as? T?
    }
}
