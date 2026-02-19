package com.nuvio.tv.util

import java.net.URLEncoder

actual object UrlEncoder {
    actual fun encode(value: String): String =
        URLEncoder.encode(value, "UTF-8").replace("+", "%20")
}
