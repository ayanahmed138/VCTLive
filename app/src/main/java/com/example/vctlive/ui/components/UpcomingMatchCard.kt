package com.example.vctlive.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.vctlive.model.UpcomingMatch


@Composable
fun UpcomingMatchCard(
    match: UpcomingMatch,
    isFollowed: Boolean,
    onFollowClick: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A1D22)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            // Tournament
            Text(
                text = "🏆 ${match.event}",
                color = Color(0xFFB0B0B0),
                style = MaterialTheme.typography.labelMedium
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Teams
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                TeamBlock(
                    name = match.team1,
                    logo = match.team1Logo,
                    modifier = Modifier.width(110.dp)
                )

                Text(
                    text = "VS",
                    color = Color(0xFFFF4655),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                TeamBlock(
                    name = match.team2,
                    logo = match.team2Logo,
                    modifier = Modifier.width(110.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column {

                    Text(
                        text = match.startsIn,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = match.series,
                        color = Color.Gray
                    )
                }

                Button(
                    onClick = onFollowClick,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (isFollowed) "⭐ Following" else "🔔 Follow"
                    )
                }
            }
        }
    }
}
