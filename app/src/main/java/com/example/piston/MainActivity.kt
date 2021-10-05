package com.example.piston

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.data.entities.Constants
import com.example.data.entities.Screen
import com.example.piston.ui.Quize.QuizPageManger
import com.google.accompanist.pager.ExperimentalPagerApi


class MainActivity : ComponentActivity() {
    private val viewModel: ViewModel by viewModels()


    @ExperimentalFoundationApi
    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getTheoryListFromDb()
        viewModel.getPracticalListFromDb()
        viewModel.getAllBoardsListFromDb()

        setContent {
            Ui()
        }
    }


    @ExperimentalFoundationApi
    @ExperimentalPagerApi
    @Composable
    fun Ui() {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val (showBottom, showBottomClick) = remember {
            mutableStateOf(true)
        }
        Scaffold(

            bottomBar = {
                if (
                    navBackStackEntry?.destination?.route == "Home" ||
                    navBackStackEntry?.destination?.route == "Lessons" ||
                    navBackStackEntry?.destination?.route == "Quizes" ||
                    navBackStackEntry?.destination?.route == "More"
                ) {
                    if (showBottom)
                        BottomNavigation {
                            val currentDestination = navBackStackEntry?.destination

                            Constants.BottomNavigationItems.forEach { screen ->
                                BottomNavigationItem(
                                    modifier = Modifier.background(colorResource(id = R.color.white_deep)),
                                    icon = {
                                        Icon(
                                            ImageVector.vectorResource(id = screen.icon),
                                            contentDescription = null,
                                            modifier = Modifier.size(60.dp)
                                        )
                                    },
                                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                    onClick = {
                                        navController.navigate(screen.route) {

                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }

                                            launchSingleTop = true

                                            restoreState = true
                                        }
                                    },
                                    selectedContentColor = colorResource(id = R.color.light_blue),
                                    unselectedContentColor = colorResource(id = R.color.light_gray)

                                )
                            }
                        }
                }
            }

        ) {
            NavHost(navController = navController, startDestination = Screen.Home.route) {
                composable(Screen.Home.route) {  }
                composable(Screen.Lessons.route) { Lessons(navController) }
                composable(Screen.Quizes.route) {
                    QuizPageManger(showBottom = showBottomClick)
                }
                composable(Screen.More.route) { More(navController) }
                composable("aboutUs") {
                    aboutUs(navController)
                }
                composable(
                    "Lessons_menu/{courses_name}",
                    arguments = listOf(navArgument("courses_name") {
                        type = NavType.StringType
                    })
                ) { navBackStackEntry ->
                    if (navBackStackEntry.arguments?.getString("courses_name") == "لیست دوره های تئوری") {
                        LessonsList(
                            navController,
                            navBackStackEntry.arguments?.getString("courses_name"),
                            viewModel.getTheoryList()
                        )
                    }
                    if (navBackStackEntry.arguments?.getString("courses_name") == "لیست دوره های عملی") {
                        LessonsList(
                            navController,
                            navBackStackEntry.arguments?.getString("courses_name"),
                            viewModel.getPracticalList()
                        )

                    }
                    if (navBackStackEntry.arguments?.getString("courses_name") == "لیست دروس") {
                        BoardTable(list = viewModel.getAllBoards(), navController = navController)
                    }

                }
                composable(
                    "reading_page/{index}/{type}",
                    arguments = listOf(navArgument("index") {
                        type = NavType.IntType
                    },
                        navArgument("type") {
                            type = NavType.IntType
                        })
                ) {
                    if (it.arguments?.getInt("type") == 3) {
                        ReadingPage(
                            navController,
                            it.arguments!!.getInt("index"),
                            viewModel.getTheoryList()
                        )
                    }
                    if (it.arguments?.getInt("type") == 7) {
                        ReadingPage(
                            navController,
                            it.arguments!!.getInt("index"),
                            viewModel.getPracticalList()
                        )

                    }
                }
            }
        }
    }
}