import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.LiveTv
import androidx.compose.material.icons.outlined.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Live : BottomNavItem(
        route = "live",
        label = "Live",
        icon = Icons.Outlined.LiveTv
    )

    object Upcoming : BottomNavItem(
        route = "upcoming",
        label = "Upcoming",
        icon = Icons.Outlined.CalendarMonth
    )

    object Following : BottomNavItem(
        route = "following",
        label = "Following",
        icon = Icons.Outlined.Star
    )
}
