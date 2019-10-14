package com.vjmanlapaz.weatherapp.apis

import android.util.Log
import com.porcelain.customerapp.Utils
import okhttp3.Interceptor
import okhttp3.Response
import com.vjmanlapaz.weatherapp.BuildConfig

internal class HeaderInterceptor : Interceptor {
    private val TAG = HeaderInterceptor::class.java.simpleName
    var isDebug: Boolean = false

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request().newBuilder().apply {
            this.addHeader("Content-Type", "application/json")
        }.build()

        if(isDebug) {
            Log.e(TAG, "=== START INTERCEPTOR ===")
            Log.e(TAG, "Url: " + request.url)
            Log.e(TAG, "Method: " + request.method)

            try {
                Log.e(TAG, "\t" + "Body: " + Utils.bodyToString(request.body))
            } catch (e: Exception) {
                Log.e(TAG, e.localizedMessage)
            }

            Log.e(TAG, "---> Request headers <---")
            for (name in request.headers.names()) {
                Log.e(TAG, "\t" + name + " : " + request.header(name))
            }
            Log.e(TAG, "---> Request headers <---")
        }


        /*var response = chain.proceed(request)
        Log.e(TAG, "---> Response headers <---")
        for (name: String in response.headers().names()) {
            Log.e(TAG, "\t" + name + " : " + response.header(name))
        }
        Log.e(TAG, "---> Response headers <---")*/

        val response = chain.proceed(request)

        if(isDebug) {
            Log.e(TAG, "HTTP STATUS: ${response.code}")
            Log.e(TAG, "=== END INTERCEPTOR ===")
        }

        return response
    }
}