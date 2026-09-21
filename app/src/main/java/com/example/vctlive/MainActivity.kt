package com.example.vctlive

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.vctlive.notification.NotificationHelper
import com.example.vctlive.ui.screens.MainScreen
import com.example.vctlive.ui.theme.VCTLiveTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NotificationHelper.createChannel(this)

        setContent {
            VCTLiveTheme {
                MainScreen()
            }
        }
    }
}
