package com.example.vctlive.ui.util

import com.example.vctlive.network.RetrofitInstance

/**
 * Turns whatever the API gives us into a full URL that Coil can load:
 *  - "/team-icons/123.webp"      -> our own server  (BASE_URL + path)
 *  - "//owcdn.net/img/abc.png"   -> https://owcdn.net/img/abc.png
 *  - "https://..."               -> unchanged
 *  - null / blank                -> null (the UI shows a placeholder)
 */
fun String?.toLogoUrl(): String? {
    if (this.isNullOrBlank()) return null

    return when {
        startsWith("http") -> this
        startsWith("//") -> "https:$this"
        startsWith("/") -> RetrofitInstance.BASE_URL.trimEnd('/') + this
        else -> this
    }
}
