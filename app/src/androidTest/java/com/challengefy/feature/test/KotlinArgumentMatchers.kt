@file:Suppress("UNCHECKED_CAST")

package com.challengefy.feature.test

import org.mockito.Mockito

object KotlinArgumentMatchers {

    fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T
}
