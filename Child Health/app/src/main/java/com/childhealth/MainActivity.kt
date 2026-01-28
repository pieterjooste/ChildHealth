package com.childhealth

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.childhealth.ui.theme.ChildHealthTheme
import com.childhealth.viewmodel.AppViewModel
import com.childhealth.views.ContentRoute
import com.childhealth.views.ContentScreen
import com.childhealth.views.LaunchRoute
import com.childhealth.views.LaunchScreen
import com.childhealth.views.SheetRoute
import com.childhealth.views.SheetScreen
import com.childhealth.views.TopicRoute
import com.childhealth.views.TopicScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            ChildHealthTheme {

                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val appViewModel: AppViewModel = viewModel()

                    LaunchedEffect(Unit) {
                        appViewModel.loadTopics(applicationContext)
                    }
                    AppNavigator(navController = rememberNavController(), appViewModel = appViewModel)
                }
            }
        }
    }
}

@Composable
fun AppNavigator(navController: NavHostController, appViewModel: AppViewModel) {

    NavHost(
        navController = navController,
        startDestination = LaunchRoute
    ) {

        composable<LaunchRoute> {
            LaunchScreen(navController = navController)
        }

        composable<ContentRoute> {
            ContentScreen(navController = navController, appViewModel = appViewModel)
        }

        composable<TopicRoute> { backStackEntry ->
            val routeArgs = try {
                backStackEntry.toRoute<TopicRoute>()
            } catch (e: IllegalArgumentException) {
                Log.e("AppNavigator", "TopicRoute: Failed to parse arguments.", e)
                navController.popBackStack()
                return@composable
            }
            val topicId = routeArgs.name
            val topicItem by appViewModel.getTopicById(topicId).collectAsState(initial = null)
            val isLoadingTopics by appViewModel.isLoading.collectAsState()

            if (topicItem != null) {
                TopicScreen(
                    navController = navController,
                    viewModel = appViewModel,
                    topic = topicItem!!
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(8.dp))
                        if (isLoadingTopics) {
                            Text("Loading topic details...")
                        } else {
                            Text("Topic '$topicId' not found or still loading.")
                        }
                    }
                }
            }
        }

        composable<SheetRoute> { backStackEntry ->
            val routeArgs = try {
                backStackEntry.toRoute<SheetRoute>()
            } catch (e: IllegalArgumentException) {
                Log.e("AppNavigator", "SheetRoute: Failed to parse arguments.", e)
                navController.popBackStack()
                return@composable
            }

            val parentTopicId = routeArgs.name
            val sheetTitle = routeArgs.title
            val sheet by appViewModel.getSheetByTitle(parentTopicId, sheetTitle).collectAsState(initial = null)


            if (sheet != null) {
                SheetScreen(
                    navController = navController,
                    viewModel = appViewModel,
                    sheet = sheet!!,
                    parentTopicId = parentTopicId
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Sheet '$sheetTitle' not found or still loading.")
                    }
                }
            }
        }
    }
}

// --- Helper functions for navigation ---

fun NavHostController.navigateToTopic(topicId: String) {
    this.navigate(TopicRoute(name = topicId))
}

// Updated to take sheetTitle
fun NavHostController.navigateToSheet(topicId: String, sheetTitle: String) {
    this.navigate(SheetRoute(name = topicId, title = sheetTitle))
}


