package com.davinci.notasapp.data.remote

import com.davinci.notasapp.data.local.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val session: SessionManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val token = session.getToken()
        val request = if (token != null) {
            original.newBuilder().header("Authorization", "Bearer $token").build()
        } else original
        return chain.proceed(request)
    }
}