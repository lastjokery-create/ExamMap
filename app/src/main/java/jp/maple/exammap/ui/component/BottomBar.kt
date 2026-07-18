package jp.maple.exammap.ui.component

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import jp.maple.exammap.ui.navigation.Screen

@Composable
fun BottomBar(
    navController: NavHostController
) {

    val backStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry.value?.destination?.route
    NavigationBar {

        NavigationBarItem(
            selected = currentRoute == Screen.Home.route,
            onClick = {
                navController.navigate(Screen.Home.route)
            },
            icon = { Text("🏠") },
            label = { Text("ホーム") }
        )

        NavigationBarItem(
            selected = currentRoute == Screen.Exams.route,
            onClick = {
                navController.navigate(Screen.Exams.route)
            },
            icon = { Text("📝") },
            label = { Text("試験") }
        )

        NavigationBarItem(
            selected = currentRoute == Screen.Records.route,
            onClick = {
                navController.navigate(Screen.Records.route)
            },
            icon = { Text("📊") },
            label = { Text("学習") }
        )

        NavigationBarItem(
            selected = currentRoute == Screen.Settings.route,
            onClick = {
                navController.navigate(Screen.Settings.route)
            },
            icon = { Text("⚙") },
            label = { Text("設定") }
        )

    }
}