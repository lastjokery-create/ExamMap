package jp.maple.exammap.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import jp.maple.exammap.ui.component.BottomBar
import jp.maple.exammap.ui.screen.AddExamScreen
import jp.maple.exammap.ui.screen.AcquiredQualificationsScreen
import jp.maple.exammap.ui.screen.ArchiveScreen
import jp.maple.exammap.ui.screen.AddRecordScreen
import jp.maple.exammap.ui.screen.EditRecordScreen
import jp.maple.exammap.ui.screen.ExamDetailScreen
import jp.maple.exammap.ui.screen.ExamListScreen
import jp.maple.exammap.ui.screen.HomeScreen
import jp.maple.exammap.ui.screen.LearningScreen
import jp.maple.exammap.ui.screen.SettingsScreen
import jp.maple.exammap.viewmodel.ExamViewModel
import jp.maple.exammap.viewmodel.StudyRecordViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val examViewModel: ExamViewModel = viewModel()
    val recordViewModel: StudyRecordViewModel = viewModel()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val showBottomBar = currentRoute != Screen.AddExam.route &&
        currentRoute != Screen.ExamDetail.route &&
        currentRoute != Screen.AddRecord.route &&
        currentRoute != Screen.EditRecord.route &&
        currentRoute != Screen.AcquiredQualifications.route &&
        currentRoute != Screen.Archive.route

    Scaffold(
        bottomBar = {
            if (showBottomBar) BottomBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    exams = examViewModel.exams,
                    onAddExamClick = { navController.navigate(Screen.AddExam.route) },
                    onExamClick = { exam ->
                        navController.navigate(Screen.ExamDetail.createRoute(exam.id))
                    },
                    onResultSelected = { exam, status ->
                        examViewModel.setExamResult(exam, status)
                    }
                )
            }

            composable(
                route = Screen.ExamDetail.route,
                arguments = listOf(navArgument("examId") { type = NavType.StringType })
            ) { entry ->
                val exam = entry.arguments?.getString("examId")?.let(examViewModel::findExamById)
                if (exam == null) {
                    Text("試験が見つかりません")
                } else {
                    ExamDetailScreen(
                        exam = exam,
                        onBackClick = { navController.popBackStack() },
                        onSaveClick = {
                            examViewModel.updateExam(it)
                            navController.popBackStack()
                        },
                        onDeleteClick = {
                            examViewModel.archiveExam(it)
                            navController.popBackStack()
                        }
                    )
                }
            }

            composable(Screen.AddExam.route) {
                AddExamScreen(
                    onBackClick = { navController.popBackStack() },
                    onRegisterClick = { name, shortName, date, precision ->
                        examViewModel.addExam(name, shortName, date, precision)
                        navController.popBackStack()
                    }
                )
            }

            composable(Screen.Exams.route) {
                ExamListScreen(
                    exams = examViewModel.exams,
                    onAddExamClick = { navController.navigate(Screen.AddExam.route) },
                    onExamClick = { exam ->
                        navController.navigate(Screen.ExamDetail.createRoute(exam.id))
                    },
                    onAcquiredQualificationsClick = {
                        navController.navigate(Screen.AcquiredQualifications.route)
                    },
                    onArchiveClick = {
                        navController.navigate(Screen.Archive.route)
                    }
                )
            }

            composable(Screen.AcquiredQualifications.route) {
                AcquiredQualificationsScreen(
                    exams = examViewModel.exams,
                    onBackClick = { navController.popBackStack() },
                    onExamClick = { exam ->
                        navController.navigate(Screen.ExamDetail.createRoute(exam.id))
                    }
                )
            }

            composable(Screen.Archive.route) {
                ArchiveScreen(
                    exams = examViewModel.exams,
                    onBackClick = { navController.popBackStack() },
                    onRestoreClick = { examViewModel.restoreExam(it) }
                )
            }

            composable(Screen.Records.route) {
                LearningScreen(
                    exams = examViewModel.exams,
                    records = recordViewModel.records,
                    onAddRecordClick = { exam ->
                        navController.navigate(Screen.AddRecord.createRoute(exam.id))
                    },
                    onRecordClick = { record ->
                        navController.navigate(Screen.EditRecord.createRoute(record.id))
                    }
                )
            }

            composable(
                route = Screen.AddRecord.route,
                arguments = listOf(navArgument("examId") { type = NavType.StringType })
            ) { entry ->
                val exam = entry.arguments?.getString("examId")?.let(examViewModel::findExamById)
                if (exam == null) {
                    Text("試験が見つかりません")
                } else {
                    AddRecordScreen(
                        exam = exam,
                        onBackClick = { navController.popBackStack() },
                        onRegisterClick = { pastExamId, studyDate, score, maxScore, passed, memo ->
                            recordViewModel.addRecord(
                                examId = exam.id,
                                pastExamId = pastExamId,
                                studyDate = studyDate,
                                score = score,
                                maxScore = maxScore,
                                passed = passed,
                                memo = memo
                            )
                            navController.popBackStack()
                        }
                    )
                }
            }

            composable(
                route = Screen.EditRecord.route,
                arguments = listOf(navArgument("recordId") { type = NavType.StringType })
            ) { entry ->
                val record = entry.arguments?.getString("recordId")
                    ?.let(recordViewModel::findRecordById)
                val exam = record?.examId?.let(examViewModel::findExamById)

                if (record == null || exam == null) {
                    Text("学習記録が見つかりません")
                } else {
                    EditRecordScreen(
                        exam = exam,
                        record = record,
                        onBackClick = { navController.popBackStack() },
                        onSaveClick = { pastExamId, studyDate, score, maxScore, passed, memo ->
                            recordViewModel.updateRecord(
                                original = record,
                                pastExamId = pastExamId,
                                studyDate = studyDate,
                                score = score,
                                maxScore = maxScore,
                                passed = passed,
                                memo = memo
                            )
                            navController.popBackStack()
                        },
                        onDeleteClick = {
                            recordViewModel.deleteRecord(record)
                            navController.popBackStack()
                        }
                    )
                }
            }

            composable(Screen.Settings.route) {
                SettingsScreen()
            }
        }
    }
}
