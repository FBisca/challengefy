@file:Suppress("NOTHING_TO_INLINE")

package com.challengefy.base.util

import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.view.View
import com.challengefy.base.activity.BaseActivity
import java.lang.ref.WeakReference
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

inline fun <T : View> BaseActivity.bind(@IdRes id: Int) = ActivityViewBinder<T>(id)
inline fun <T : View> Fragment.bind(@IdRes id: Int) = FragmentViewBinder<T>(id)

class ActivityViewBinder<out T: View>(@IdRes private val id: Int) : ReadOnlyProperty<BaseActivity, T> {

    private var reference: T? = null

    override fun getValue(thisRef: BaseActivity, property: KProperty<*>): T {
        synchronized(this) {
            if (reference == null) {
                reference = thisRef.findViewById(id)
            }

            return reference ?: throw IllegalArgumentException("Property not initialized")
        }
    }
}

class FragmentViewBinder<out T: View>(@IdRes private val id: Int) : ReadOnlyProperty<Fragment, T> {

    private var viewReference: WeakReference<View>? = null
    private var reference: T? = null

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        synchronized(this) {
            val fragmentView = thisRef.view ?: throw IllegalAccessException("Fragment View is null")

            val view = viewReference?.get()
            if (reference == null || view != fragmentView) {
                viewReference = WeakReference(fragmentView)
                reference = fragmentView.findViewById(id)
            }

            return reference ?: throw IllegalArgumentException("Property not initialized")
        }
    }
}
