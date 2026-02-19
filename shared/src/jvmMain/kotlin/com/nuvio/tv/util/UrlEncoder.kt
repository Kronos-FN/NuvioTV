package com.nuvio.tv.util

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

actual object UrlEncoder {
    actual fun encode(value: String): String {
        return URLEncoder.encode(value, StandardCharsets.UTF_8.toString())
    }
}
