package jp.maple.exammap.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import jp.maple.exammap.ui.component.BottomBar
import jp.maple.exammap.ui.screen.*
import jp.maple.exammap.ui.viewmodel.ExamViewModel

@Composable
fun AppNavigation(examViewModel: ExamViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomBarRoutes = listOf("home", "exams", "records", "settings")

    Scaffold(
        bottomBar = {
            if (currentRoute in bottomBarRoutes) {
                BottomBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo("home") { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("home") {
                HomeScreen(
                    viewModel = examViewModel,
                    onAllExamsClick = { navController.navigate("exams") },
                    onExamClick = { id -> navController.navigate("exam_detail/$id") },
                    onAddExamClick = { navController.navigate("add_exam") }
                )
            }
            composable("exams") {
                ExamListScreen(
                    viewModel = examViewModel,
                    onExamClick = { id -> navController.navigate("exam_detail/$id") }
                )
            }
            composable("records") {
                // 仮の配置。実際の画面構造に合わせて引き渡してください
                Text("学習記録画面（プレースホルダー）")
            }
            composable("settings") {
                // 仮の配置。実際の画面構造に合わせて引き渡してください
                Text("設定画面（プレースホルダー）")
            }
            composable("add_exam") {
                // 仮の配置。実際の画面構造に合わせて引き渡してください
                Text("試験追加画面（プレースホルダー）")
            }
            composable(
                route = "exam_detail/{examId}",
                arguments = listOf(navArgument("examId") { type = NavType.StringType })
            ) { backStackEntry ->
                val examId = backStackEntry.arguments?.getString("examId") ?: ""
                // 仮の配置。実際の画面構造に合わせて引き渡してください
                Text("試験詳細画面（プレースホルダー） ID: $examId")
            }
        }
    }
}