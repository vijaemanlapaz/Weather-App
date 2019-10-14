package com.porcelain.customerapp

import okhttp3.RequestBody
import okio.Buffer

class Utils {
    companion object {
        fun bodyToString(request: RequestBody?): String {
            val buffer = Buffer()
            if (request != null) {
                request.writeTo(buffer)
            }
            else
                return ""
            return buffer.readUtf8()
        }
    }
}