package jp.maple.exammap.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import jp.maple.exammap.ui.theme.ColorGrayText
import jp.maple.exammap.ui.theme.ColorWhite
import jp.maple.exammap.ui.theme.OrangeMain

sealed class BottomBarItem(val route: String, val title: String, val icon: ImageVector) {
    object Home : BottomBarItem("home", "ホーム", Icons.Default.Home)
    object Exams : BottomBarItem("exams", "試験", Icons.AutoMirrored.Filled.List)
    object Records : BottomBarItem("records", "学習記録", Icons.Default.BarChart)
    object Settings : BottomBarItem("settings", "設定", Icons.Default.Settings)
}

@Composable
fun BottomBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
) {
    val items = listOf(
        BottomBarItem.Home,
        BottomBarItem.Exams,
        BottomBarItem.Records,
        BottomBarItem.Settings
    )

    NavigationBar(
        containerColor = ColorWhite
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(item.route) },
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                label = { Text(text = item.title) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = OrangeMain,
                    selectedTextColor = OrangeMain,
                    unselectedIconColor = ColorGrayText,
                    unselectedTextColor = ColorGrayText,
                    indicatorColor = ColorWhite
                )
            )
        }
    }
}