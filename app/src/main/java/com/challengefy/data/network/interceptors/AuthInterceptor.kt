package com.challengefy.data.network.interceptors

import com.challengefy.BuildConfig
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.util.*
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
        private val locale: Locale
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(interceptRequest(chain.request()))
    }

    private fun interceptRequest(request: Request): Request {
        return request
                .newBuilder()
                .apply {
                    addHeader("Authorization", "Bearer ${BuildConfig.AUTH_TOKEN}")
                    addHeader("Accept-Language", locale.toLanguageTag())
                }
                .build()
    }
}
