package com.example.vctlive.ui.util

fun extractMatchId(matchPage: String): String {
    return matchPage.split("/")[0]
}