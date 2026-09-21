package com.example.vctlive.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.vctlive.ui.util.toLogoUrl

@Composable
fun TeamLogo(
    logo: String?,
    name: String,
    size: Dp = 56.dp
) {
    val url = logo.toLogoUrl()

    if (url == null) {

        // No logo for this team yet: grey circle with the first letter
        Box(
            modifier = Modifier
                .size(size)
                .background(Color(0xFF2A2E35), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = name.take(1).uppercase(),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = (size.value / 2.5f).sp
            )
        }

    } else {

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(url)
                .crossfade(true)
                .build(),
            contentDescription = name,
            modifier = Modifier.size(size)
        )
    }
}
